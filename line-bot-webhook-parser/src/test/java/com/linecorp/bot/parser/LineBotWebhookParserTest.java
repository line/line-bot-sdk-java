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

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.springframework.util.StreamUtils;

import com.linecorp.bot.model.event.CallbackRequest;

public class LineBotWebhookParserTest {

    @Test
    public void testParseWithDefaultDeserializeOption() throws Exception {
        final byte[] json = readResource("callback/unknown-message.json");
        final LineBotWebhookParser parser = new LineBotWebhookParser();
        // skip unknown message event with warn logging
        final CallbackRequest req = parser.parse(json);
        assertThat(req).isNotNull();
        assertThat(req.getEvents()).hasSize(2);
        assertThat(req.getEvents().get(0).getSource().getSenderId())
                .isEqualTo("u00000000000000000000000000000000");
        assertThat(req.getEvents().get(1).getSource().getSenderId())
                .isEqualTo("u00000000000000000000000000000002");
    }

    @Test
    public void testParseWithNonSkipNullOption() throws Exception {
        final byte[] json = readResource("callback/unknown-message.json");
        final LineBotWebhookParser parser = new LineBotWebhookParser(
                new LineBotDeserializerOption(
                        LineBotDeserializerOption.getWarnLoggingAndReturnNullHandler(),
                        false
                )
        );
        final CallbackRequest req = parser.parse(json);
        assertThat(req).isNotNull();
        assertThat(req.getEvents()).hasSize(3);
        assertThat(req.getEvents().get(0).getSource().getSenderId())
                .isEqualTo("u00000000000000000000000000000000");
        assertThat(req.getEvents().get(1)).isNull();
        assertThat(req.getEvents().get(2).getSource().getSenderId())
                .isEqualTo("u00000000000000000000000000000002");
    }

    @Test(expected = LineBotWebhookParseException.class)
    public void testParseWithThrowExceptionOption() throws Exception {
        final byte[] json = readResource("callback/unknown-message.json");
        final LineBotWebhookParser parser = new LineBotWebhookParser(
                new LineBotDeserializerOption(
                        LineBotDeserializerOption.getThrowExceptionHandler(),
                        false
                )
        );
        parser.parse(json);
    }

    private byte[] readResource(String resourceName) throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            return StreamUtils.copyToByteArray(inputStream);
        }
    }
}