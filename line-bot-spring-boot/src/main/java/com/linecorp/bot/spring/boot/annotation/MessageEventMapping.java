package com.linecorp.bot.spring.boot.annotation;

import static com.linecorp.bot.spring.boot.annotation.MessageEventMapping.PRIORITY;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;

/**
 * Specialized for {@link MessageEvent} version of {@link EventMapping}
 *
 * @see EventMapping
 * @see LineMessageHandler
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventMapping(value = MessageEvent.class, priority = PRIORITY)
public @interface MessageEventMapping {
    int PRIORITY = 100;

    @AliasFor(annotation = EventMapping.class, attribute = "message")
    Class<? extends MessageContent>[] value();
}
