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

import java.lang.annotation.Annotation;
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

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.ReplyEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineBotDestination;
import com.linecorp.bot.spring.boot.annotation.LineBotMessages;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

/**
 * Dispatcher for LINE Message Event Handling.
 *
 * <p>Dispatch target method is collected by Spring's bean.
 *
 * <p>Event endpoint is configurable by {@code line.bot.callback-path} parameter.
 *
 * <h2>Event Handler method rule.</h2>
 *
 * <p>The class and method with following rules are collected by LINE Messaging Event handler.
 *
 * <ul>
 * <li>Class annotated with {@link LineMessageHandler}</li>
 * <li>Method annotated with {@link EventMapping}.</li>
 * </ul>
 */
@Slf4j
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

        return new HandlerMethod(consumer, method, mapping);
    }

    @Value
    static class HandlerMethod {
        Predicate<Event> supportType;
        Object object;
        Method handler;
        int priority;

        HandlerMethod(Object object, Method handler, EventMapping mapping) {
            this.object = object;
            this.handler = handler;

            // TODO: Support more flexible argument resolver. Like MVC's argument resolver?
            int parameterCount = handler.getParameterCount();
            if (parameterCount == 1) {
                final Type type = handler.getGenericParameterTypes()[0];
                this.supportType = new EventPredicate(type);
                this.priority = calcPriority(mapping, type);
            } else if (parameterCount == 2) {
                Annotation[] parameterAnnotations = handler.getParameterAnnotations()[0];
                Preconditions.checkState(
                        Arrays.stream(parameterAnnotations)
                              .filter(it -> it instanceof LineBotDestination)
                              .count() == 1,
                        "1st argument of the event hnadler should have @LineBotDestination annotation"
                        + " when the method have 2 arguments."
                );

                final Type type = handler.getGenericParameterTypes()[1];
                this.supportType = new EventPredicate(type);
                this.priority = calcPriority(mapping, type);
            } else {
                throw new IllegalStateException("Number of parameter should be 1 or 2."
                                                + " But " + Arrays.toString(handler.getParameterTypes()));
            }
        }

        private int calcPriority(final EventMapping mapping, final Type type) {
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

        Object invoke(String destination, Event event) throws Exception {
            int count = handler.getParameterCount();
            if (count == 1) {
                return handler.invoke(object, event);
            } else if (count == 2) {
                return handler.invoke(object, destination, event);
            } else {
                throw new IllegalStateException();
            }
        }
    }

    @PostMapping("${line.bot.handler.path:/callback}")
    public void callback(@LineBotDestination String destination, @LineBotMessages List<Event> events) {
        events.forEach(event -> {
            this.dispatch(destination, event);
        });
    }

    @VisibleForTesting
    void dispatch(String destination, Event event) {
        try {
            dispatchInternal(destination, event);
        } catch (InvocationTargetException e) {
            log.error("InvocationTargetException occurred.", e);
        } catch (Error | Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void dispatchInternal(final String destination, final Event event) throws Exception {
        final HandlerMethod handlerMethod = eventConsumerList
                .stream()
                .filter(consumer -> consumer.getSupportType().test(event))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("Unsupported event type. " + event));
        final Object returnValue = handlerMethod.invoke(destination, event);

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
                   && (messageContentType == null
                       || event instanceof MessageEvent
                          && filterByType(messageContentType, ((MessageEvent<?>) event).getMessage()));
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
