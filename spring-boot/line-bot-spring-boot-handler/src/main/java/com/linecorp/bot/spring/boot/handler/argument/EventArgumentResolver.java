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

package com.linecorp.bot.spring.boot.handler.argument;

import java.lang.reflect.Type;

import com.linecorp.bot.webhook.model.Event;

public class EventArgumentResolver implements ArgumentResolver {
    private final Class<?> klass;

    public EventArgumentResolver(Type type) {
        this.klass = (Class<?>) type;
    }

    @Override
    public String toString() {
        return "EventArgumentResolver{"
                + "klass=" + klass
                + '}';
    }

    @Override
    public boolean isSupported(Event event) {
        return klass.isAssignableFrom(event.getClass());
    }

    @Override
    public Object resolve(String destination, Event event) {
        return event;
    }
}
