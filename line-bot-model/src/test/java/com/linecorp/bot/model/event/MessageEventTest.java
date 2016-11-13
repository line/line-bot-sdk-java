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

package com.linecorp.bot.model.event;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.junit.Test;

import com.linecorp.bot.model.event.message.MessageContent;

public class MessageEventTest {
    @Test
    public void constructor2ndParameterBinaryCompatibilityTest() {
        Constructor<?> constructor = MessageEvent.class.getConstructors()[0];

        Class<?>[] parameterTypes = constructor.getParameterTypes();
        assertThat(parameterTypes).hasSize(4);
        assertThat(parameterTypes[2]).isEqualTo(MessageContent.class)
                                     .isNotEqualTo(Object.class);
    }

    @Test
    public void getMessageBinaryCompatibilityTest() throws NoSuchMethodException {
        Method method = MessageEvent.class.getMethod("getMessage");

        assertThat(method.getReturnType()).isEqualTo(MessageContent.class)
                                          .isNotEqualTo(Object.class);
    }
}
