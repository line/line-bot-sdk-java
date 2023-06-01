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

package com.linecorp.bot.webhook.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

class UnknownFallbackTest {
    ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);

    @ParameterizedTest
    @MethodSource("targets")
    void testUnknownFallback(Class<?> klass, Class<?> target) throws JsonProcessingException {
        Object object = objectMapper.readValue("""
                {"type":"great-new-thing"}
                """, klass);
        assertThat(object).isInstanceOf(target);
    }

    public static Stream<Arguments> targets() {
        return Stream.of(
                arguments(Event.class, UnknownEvent.class),
                arguments(Mentionee.class, UnknownMentionee.class),
                arguments(MessageContent.class, UnknownMessageContent.class),
                arguments(ModuleContent.class, UnknownModuleContent.class),
                arguments(Source.class, UnknownSource.class),
                arguments(ThingsContent.class, UnknownThingsContent.class)
        );
    }
}
