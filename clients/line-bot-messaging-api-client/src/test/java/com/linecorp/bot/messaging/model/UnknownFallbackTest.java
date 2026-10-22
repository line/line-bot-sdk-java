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

package com.linecorp.bot.messaging.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.cfg.EnumFeature;
import tools.jackson.databind.json.JsonMapper;

class UnknownFallbackTest {
    ObjectMapper objectMapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(EnumFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
            .build();

    @ParameterizedTest
    @MethodSource("targets")
    void testUnknownFallback(Class<?> klass, Class<?> target) throws JacksonException {
        Object object = objectMapper.readValue("""
                {"type":"great-new-thing"}
                """, klass);
        assertThat(object).isInstanceOf(target);
    }

    public static Stream<Arguments> targets() {
        return Stream.of(
                arguments(Action.class, UnknownAction.class),
                arguments(DemographicFilter.class, UnknownDemographicFilter.class),
                arguments(FlexBoxBackground.class, UnknownFlexBoxBackground.class),
                arguments(FlexComponent.class, UnknownFlexComponent.class),
                arguments(FlexContainer.class, UnknownFlexContainer.class),
                arguments(ImagemapAction.class, UnknownImagemapAction.class),
                arguments(Message.class, UnknownMessage.class),
                arguments(Recipient.class, UnknownRecipient.class),
                arguments(Template.class, UnknownTemplate.class)
        );
    }
}
