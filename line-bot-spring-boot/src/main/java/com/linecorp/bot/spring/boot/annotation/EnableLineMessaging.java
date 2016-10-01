package com.linecorp.bot.spring.boot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.linecorp.bot.spring.boot.support.LineMessageHandlerSupport;

/**
 * Adding this annotation to an {@code @Configuration} class imports the
 * {@link LineMessageHandlerSupport}.
 *
 * @see LineMessageHandlerSupport
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableLineMessaging {}
