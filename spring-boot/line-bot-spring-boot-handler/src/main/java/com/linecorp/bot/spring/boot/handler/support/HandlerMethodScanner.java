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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ReflectionUtils;

import com.linecorp.bot.spring.boot.handler.annotation.EventMapping;

public class HandlerMethodScanner {
    private static final Comparator<HandlerMethod> HANDLER_METHOD_PRIORITY_COMPARATOR =
            Comparator.comparing(HandlerMethod::priority).reversed();

    public List<HandlerMethod> scan(Collection<Object> objects) {
        return objects
                .stream()
                .flatMap(this::scanObjectWithoutSorting)
                .sorted(HANDLER_METHOD_PRIORITY_COMPARATOR)
                .collect(Collectors.toList());
    }

    Stream<HandlerMethod> scanObjectWithoutSorting(Object object) {
        final Method[] uniqueDeclaredMethods =
                ReflectionUtils.getUniqueDeclaredMethods(object.getClass());

        return Arrays.stream(uniqueDeclaredMethods)
                .map(method -> getMethodHandlerMethodFunction(object, method))
                .filter(Objects::nonNull);
    }

    private HandlerMethod getMethodHandlerMethodFunction(Object consumer, Method method) {
        final EventMapping mapping = AnnotatedElementUtils.getMergedAnnotation(method, EventMapping.class);
        if (mapping == null) {
            return null;
        }

        return HandlerMethod.of(consumer, method, mapping);
    }
}
