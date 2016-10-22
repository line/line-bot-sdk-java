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

package com.linecorp.bot.parser;

import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.linecorp.bot.model.event.CallbackRequest;

public class LineBotWebhookParser {
    private final ObjectReader objectReader;

    public LineBotWebhookParser() {
        this(LineBotDeserializerOption.getDefault());
    }

    public LineBotWebhookParser(LineBotDeserializerOption deserializerOption) {
        this.objectReader = buildObjectReader(deserializerOption);
    }

    public CallbackRequest parse(byte[] body) throws LineBotWebhookParseException {
        try {
            return objectReader.readValue(body);
        } catch (IOException e) {
            throw new LineBotWebhookParseException("failed to parse webhook body", e);
        }
    }

    private static ObjectReader buildObjectReader(
            LineBotDeserializerOption deserializerOption
    ) {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Register JSR-310(java.time.temporal.*) module and read number as millsec.
        objectMapper.registerModule(new JavaTimeModule())
                    .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);

        final LineBotCallbackRequestDeserializer deserializer =
                new LineBotCallbackRequestDeserializer(deserializerOption);
        final LineBotWebhookParserModule
                lineBotWebhookParserModule = new LineBotWebhookParserModule(deserializer);
        objectMapper.registerModule(lineBotWebhookParserModule);

        return objectMapper.readerFor(CallbackRequest.class);
    }
}
