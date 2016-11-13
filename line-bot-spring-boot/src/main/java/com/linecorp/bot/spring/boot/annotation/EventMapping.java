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
