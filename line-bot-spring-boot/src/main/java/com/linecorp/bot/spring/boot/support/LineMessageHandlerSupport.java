package com.linecorp.bot.spring.boot.support;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.springframework.core.annotation.AnnotatedElementUtils.getMergedAnnotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Ordering;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.spring.boot.annotation.DefaultEventMapping;
import com.linecorp.bot.spring.boot.annotation.EnableLineMessaging;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineBotMessages;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.linecorp.bot.spring.boot.annotation.MessageEventMapping;

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
 *     <li>Method annotated with {@link EventMapping}
 *     (or it's child like {@link MessageEventMapping} or {@link DefaultEventMapping})</li>
 * </ul>
 */
@Slf4j
@RestController
@ConditionalOnBean(annotation = EnableLineMessaging.class)
public class LineMessageHandlerSupport {
    private final ConfigurableApplicationContext applicationContext;

    volatile List<HandlerMethod> eventConsumerList;

    @Autowired
    public LineMessageHandlerSupport(final ConfigurableApplicationContext applicationContext) {
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
                .flatMap((Object consumer) -> {
                    final Method[] uniqueDeclaredMethods =
                            ReflectionUtils.getUniqueDeclaredMethods(consumer.getClass());

                    return stream(uniqueDeclaredMethods)
                            .map((Method method) -> {
                                final EventMapping mapping = getMergedAnnotation(method, EventMapping.class);
                                if (mapping == null) {
                                    return null;
                                }

                                @SuppressWarnings("unchecked")
                                final Class<? extends Event> supportEvent = mapping.value();

                                final Predicate<Event> predicate = new EventPredicate(mapping);
                                return new HandlerMethod(predicate, consumer, method, mapping.priority());
                            })
                            .filter(Objects::nonNull);
                })
                .sorted(Ordering.natural().onResultOf(HandlerMethod::getPriority).reverse())
                .collect(toList());

        collect.forEach(item -> log.info("{} > {}", item.getSupportType(),
                                         item.getHandler().toGenericString()));

        eventConsumerList = collect;
    }

    @Value
    public static class HandlerMethod {
        Predicate<Event> supportType;
        Object object;
        Method handler;
        int priority;
    }

    @PostMapping("${line.bot.callback-path:/callback}")
    public void callback(@LineBotMessages List<Event> events) {
        events.forEach(this::dispatch);
    }

    private void dispatch(Event event) {
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
        handlerMethod.getHandler().invoke(handlerMethod.getObject(), event);
    }

    private static class EventPredicate implements Predicate<Event> {
        private final EventMapping mapping;
        private final List<Class<? extends Event>> supportEvent;

        @SuppressWarnings("unchecked")
        EventPredicate(final EventMapping mapping) {
            final Class<? extends Event> event = mapping.value();

            if (mapping.message().length != 0) {
                Preconditions.checkState(event == Event.class || event == MessageEvent.class,
                                         "Message class specified but event class is not MessageEvent");
                this.supportEvent = singletonList(MessageEvent.class);
            } else {
                this.supportEvent = asList(event);
            }

            this.mapping = mapping;
        }

        @Override
        public boolean test(final Event event) {
            return supportEvent.stream().anyMatch(i -> i.isAssignableFrom(event.getClass()))
                   && (mapping.message().length == 0 ||
                       event instanceof MessageEvent &&
                       filterByType(mapping.message(), ((MessageEvent<?>) event).getMessage()));
        }

        private static boolean filterByType(final Class<?>[] clazz, final Object content) {
            if (clazz.length == 0) {
                return true;
            }

            return stream(clazz)
                    .anyMatch(messageClass -> messageClass
                            .isAssignableFrom(content.getClass()));
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("event = ");

            if (mapping.message().length != 0) {
                sb.append(MessageEvent.class.getSimpleName())
                  .append("<")
                  .append(Stream.of(mapping.message()).map(Class::getSimpleName).collect(joining(" | ")))
                  .append(">");
            } else {
                sb.append(supportEvent.stream().map(Class::getSimpleName).collect(joining(",")));
            }

            return sb.toString();
        }
    }
}
