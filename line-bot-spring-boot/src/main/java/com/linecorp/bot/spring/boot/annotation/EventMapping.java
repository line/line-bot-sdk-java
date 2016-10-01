package com.linecorp.bot.spring.boot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;

/**
 * Indicates that an annotated method is a LINE Messaging Event Handler.
 *
 * Note: Only {@link EventMapping @EventMapping} annotated method
 * in the class annotated by {@link LineMessageHandler @LineMessageHandler} is enabled.
 */
@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface EventMapping {
    /**
     * {@link Event} class annotated method handle.
     */
    Class<? extends Event> value();

    /**
     * {@link MessageContent} class annotated method handle.
     *
     * When specify this value, please set {@link MessageEvent} in {@link #value()},
     * or consider using {@link MessageEventMapping}.
     *
     * @see MessageEventMapping
     */
    Class<? extends MessageContent>[] message() default {};

    /**
     * Priority of this mapping. Bigger mapping is preferentially searched and matched.
     */
    int priority() default 0;
}
