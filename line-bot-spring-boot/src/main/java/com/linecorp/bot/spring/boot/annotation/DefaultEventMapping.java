package com.linecorp.bot.spring.boot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;

/**
 * Specialized for {@link MessageEvent} version to specify Fallback Mapping.
 *
 * This mappings matches all {@link Event} instances at lowest priority.
 *
 * @see EventMapping
 * @see LineMessageHandler
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventMapping(value = Event.class, priority = Integer.MIN_VALUE)
public @interface DefaultEventMapping {
}
