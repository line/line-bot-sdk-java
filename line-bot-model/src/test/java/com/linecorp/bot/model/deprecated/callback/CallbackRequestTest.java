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

package com.linecorp.bot.model.deprecated.callback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.v2.event.CallbackRequest;
import com.linecorp.bot.model.v2.event.MessageEvent;

public class CallbackRequestTest {
    @Test
    public void testMessage() throws IOException {
        InputStream resource = getClass().getClassLoader().getResourceAsStream("callback-request.json");
        String json = IOUtils.toString(resource);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CallbackRequest callbackRequest = objectMapper.readValue(json, CallbackRequest.class);
        assertEquals(1, callbackRequest.getEvents().size());
        com.linecorp.bot.model.v2.event.Event event = callbackRequest.getEvents().get(0);
        assertThat(event).isInstanceOf(MessageEvent.class);

        com.linecorp.bot.model.v2.event.MessageEvent messageEvent = (MessageEvent) event;
        assertThat(messageEvent.getReplyToken())
                .isEqualTo("ABCDEF-12345678901");
    }
}
