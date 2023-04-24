/*
 * Copyright 2023 LINE Corporation
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

package com.linecorp.bot.spring.boot.handler.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.linecorp.bot.spring.boot.handler.annotation.EventMapping;
import com.linecorp.bot.spring.boot.handler.argument.ArgumentResolver;
import com.linecorp.bot.spring.boot.handler.argument.EventArgumentResolver;
import com.linecorp.bot.spring.boot.handler.argument.LineBotDestinationArgumentResolver;
import com.linecorp.bot.spring.boot.handler.argument.MessageContentArgumentResolver;
import com.linecorp.bot.spring.boot.web.argument.annotation.LineBotDestination;
import com.linecorp.bot.webhook.model.Event;
import com.linecorp.bot.webhook.model.MessageContent;

record HandlerMethod(
        List<ArgumentResolver> argumentResolvers,
        Object object,
        Method handler,
        int priority
) {
    public static HandlerMethod of(Object object, Method handler, EventMapping mapping) {
        List<ArgumentResolver> argumentResolvers = new ArrayList<>();

        int parameterCount = handler.getParameterCount();
        final Type[] types = handler.getGenericParameterTypes();
        Annotation[][] parameterAnnotationsArray = handler.getParameterAnnotations();
        for (int i = 0; i < parameterCount; i++) {
            final Type type = types[i];
            Annotation[] parameterAnnotations = parameterAnnotationsArray[i];

            argumentResolvers.add(createArgumentResolver(type, parameterAnnotations));
        }

        return new HandlerMethod(
                argumentResolvers,
                object,
                handler,
                calcPriority(mapping, types)
        );
    }

    private static ArgumentResolver createArgumentResolver(Type type, Annotation[] parameterAnnotations) {
        if (Arrays.stream(parameterAnnotations)
                .filter(it -> it instanceof LineBotDestination)
                .count() == 1) {
            // @LineBotDestination
            return new LineBotDestinationArgumentResolver();
        } else if (type instanceof Class<?>) {
            if (Event.class.isAssignableFrom((Class<?>) type)) {
                return new EventArgumentResolver(type);
            } else if (MessageContent.class.isAssignableFrom((Class<?>) type)) {
                return new MessageContentArgumentResolver(type);
            } else {
                throw new IllegalArgumentException("Unsupported type set for @EventMapping: " + type);
            }
        } else {
            throw new IllegalArgumentException("Unsupported type set for @EventMapping: " + type);
        }
    }

    private static int calcPriority(final EventMapping mapping, final Type[] types) {
        if (mapping.priority() != EventMapping.DEFAULT_PRIORITY_VALUE) {
            return mapping.priority();
        }

        return Arrays.stream(types)
                .mapToInt(
                        type -> {
                            if (type == String.class) {
                                return EventMapping.DEFAULT_PRIORITY_FOR_STRING;
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
                ).sum();
    }

    Object invoke(String destination, Event event) throws Exception {
        Object[] args = this.argumentResolvers().stream()
                .map(argumentResolver -> argumentResolver.resolve(destination, event))
                .toArray();
        return handler.invoke(object, args);
    }

    public boolean isSupported(Event event) {
        return this.argumentResolvers.stream()
                .allMatch(argumentResolver -> argumentResolver.isSupported(event));
    }
}
