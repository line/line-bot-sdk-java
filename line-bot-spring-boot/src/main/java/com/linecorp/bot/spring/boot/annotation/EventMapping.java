package com.linecorp.bot.spring.boot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.common.annotations.Beta;

/**
 * Indicates that an annotated method is a LINE Messaging Event Handler.
 *
 * Note: Only {@link EventMapping @EventMapping} annotated method
 * in the class annotated by {@link LineMessageHandler @LineMessageHandler} is enabled.
 */
@Beta
@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface EventMapping {
    int DEFAULT_PRIORITY_VALUE = -1;
    int DEFAULT_PRIORITY_FOR_EVENT_IFACE = 0;
    int DEFAULT_PRIORITY_FOR_IFACE = 100;
    int DEFAULT_PRIORITY_FOR_CLASS = 200;
    int DEFAULT_PRIORITY_FOR_PARAMETRIZED_TYPE = 300;

    /**
     * Priority of this mapping. Bigger mapping is preferentially searched and matched.
     */
    int priority() default DEFAULT_PRIORITY_VALUE;
}
