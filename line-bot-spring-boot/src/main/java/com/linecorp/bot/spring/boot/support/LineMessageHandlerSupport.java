/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.linecorp.bot.spring.boot.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.ReplyEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineBotMessages;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

/**
 * Dispatcher for LINE Message Event Handling.
 *
 * Dispatch target method is collected by Spring's bean.
 *
 * Event endpoint is configurable by {@code line.bot.callback-path} parameter.
 *
 * <h2>Event Handler method rule.</h2>
 *
 * The class and method with following rules are collected by LINE Messaging Event handler.
 *
 * <ul>
 *     <li>Class annotated with {@link LineMessageHandler}</li>
 *     <li>Method annotated with {@link EventMapping}.</li>
 * </ul>
 */
@Slf4j
@Beta
@RestController
@Import(ReplyByReturnValueConsumer.Factory.class)
@ConditionalOnProperty(name = "line.bot.handler.enabled", havingValue = "true", matchIfMissing = true)
public class LineMessageHandlerSupport {
    private static final Comparator<HandlerMethod> HANDLER_METHOD_PRIORITY_COMPARATOR =
            Comparator.comparing(HandlerMethod::getPriority).reversed();
    private final ReplyByReturnValueConsumer.Factory returnValueConsumerFactory;
    private final ConfigurableApplicationContext applicationContext;

    volatile List<HandlerMethod> eventConsumerList;

    @Autowired
    public LineMessageHandlerSupport(
            final ReplyByReturnValueConsumer.Factory returnValueConsumerFactory,
            final ConfigurableApplicationContext applicationContext) {
        this.returnValueConsumerFactory = returnValueConsumerFactory;
        this.applicationContext = applicationContext;

        applicationContext.addApplicationListener(event -> {
            if (event instanceof ContextRefreshedEvent) {
                refresh();
            }
        });
    }

    @VisibleForTesting
    void refresh() {
        final Map<String, Object> handlerBeanMap =
                applicationContext.getBeansWithAnnotation(LineMessageHandler.class);

        final List<HandlerMethod> collect = handlerBeanMap
                .values().stream()
                .flatMap((Object bean) -> {
                    final Method[] uniqueDeclaredMethods =
                            ReflectionUtils.getUniqueDeclaredMethods(bean.getClass());

                    return Arrays.stream(uniqueDeclaredMethods)
                                 .map(method -> getMethodHandlerMethodFunction(bean, method))
                                 .filter(Objects::nonNull);
                })
                .sorted(HANDLER_METHOD_PRIORITY_COMPARATOR)
                .collect(Collectors.toList());

        log.info("Registered LINE Messaging API event handler: count = {}", collect.size());
        collect.forEach(item -> log.info("Mapped \"{}\" onto {}",
                                         item.getSupportType(), item.getHandler().toGenericString()));

        eventConsumerList = collect;
    }

    private HandlerMethod getMethodHandlerMethodFunction(Object consumer, Method method) {
        final EventMapping mapping = AnnotatedElementUtils.getMergedAnnotation(method, EventMapping.class);
        if (mapping == null) {
            return null;
        }

        Preconditions.checkState(method.getParameterCount() == 1,
                                 "Number of parameter should be 1. But {}",
                                 (Object[]) method.getParameterTypes());
        // TODO: Support more than 1 argument. Like MVC's argument resolver?

        final Type type = method.getGenericParameterTypes()[0];

        final Predicate<Event> predicate = new EventPredicate(type);
        return new HandlerMethod(predicate, consumer, method,
                                 getPriority(mapping, type));
    }

    private int getPriority(final EventMapping mapping, final Type type) {
        if (mapping.priority() != EventMapping.DEFAULT_PRIORITY_VALUE) {
            return mapping.priority();
        }

        if (type == Event.class) {
            return EventMapping.DEFAULT_PRIORITY_FOR_EVENT_IFACE;
        }

        if (type instanceof Class) {
            return ((Class<?>) type).isInterface()
                   ? EventMapping.DEFAULT_PRIORITY_FOR_IFACE
                   : EventMapping.DEFAULT_PRIORITY_FOR_CLASS;
        }

        if (type instanceof ParameterizedType) {
            return EventMapping.DEFAULT_PRIORITY_FOR_PARAMETRIZED_TYPE;
        }

        throw new IllegalStateException();
    }

    @Value
    static class HandlerMethod {
        Predicate<Event> supportType;
        Object object;
        Method handler;
        int priority;
    }

    @PostMapping("${line.bot.handler.path:/callback}")
    public void callback(@LineBotMessages List<Event> events) {
        events.forEach(this::dispatch);
    }

    @VisibleForTesting
    void dispatch(Event event) {
        try {
            dispatchInternal(event);
        } catch (InvocationTargetException e) {
            log.trace("InvocationTargetException occurred.", e);
            log.error(e.getCause().getMessage(), e.getCause());
        } catch (Error | Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void dispatchInternal(final Event event) throws Exception {
        final HandlerMethod handlerMethod = eventConsumerList
                .stream()
                .filter(consumer -> consumer.getSupportType().test(event))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("Unsupported event type. " + event));
        final Object returnValue = handlerMethod.getHandler().invoke(handlerMethod.getObject(), event);

        handleReturnValue(event, returnValue);
    }

    private void handleReturnValue(final Event event, final Object returnValue) {
        if (returnValue != null) {
            returnValueConsumerFactory.createForEvent(event)
                                      .accept(returnValue);
        }
    }

    private static class EventPredicate implements Predicate<Event> {
        private final Class<?> supportEvent;
        private final Class<? extends MessageContent> messageContentType;

        @SuppressWarnings("unchecked")
        EventPredicate(final Type mapping) {
            if (mapping == ReplyEvent.class) {
                supportEvent = ReplyEvent.class;
                messageContentType = null;
            } else if (mapping instanceof Class) {
                Preconditions.checkState(Event.class.isAssignableFrom((Class<?>) mapping),
                                         "Handler argument type should BE-A Event. But {}",
                                         mapping.getClass());
                supportEvent = (Class<? extends Event>) mapping;
                messageContentType = null;
            } else {
                final ParameterizedType parameterizedType = (ParameterizedType) mapping;
                supportEvent = (Class<? extends Event>) parameterizedType.getRawType();
                messageContentType =
                        (Class<? extends MessageContent>)
                                ((ParameterizedType) mapping).getActualTypeArguments()[0];
            }
        }

        @Override
        public boolean test(final Event event) {
            return supportEvent.isAssignableFrom(event.getClass())
                   && (messageContentType == null ||
                       event instanceof MessageEvent &&
                       filterByType(messageContentType, ((MessageEvent<?>) event).getMessage()));
        }

        private static boolean filterByType(final Class<?> clazz, final Object content) {

            return clazz.isAssignableFrom(content.getClass());
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();

            sb.append('[');
            if (messageContentType != null) {
                sb.append(MessageEvent.class.getSimpleName())
                  .append('<')
                  .append(messageContentType.getSimpleName())
                  .append('>');
            } else {
                sb.append(supportEvent.getSimpleName());
            }
            sb.append(']');

            return sb.toString();
        }
    }
}
