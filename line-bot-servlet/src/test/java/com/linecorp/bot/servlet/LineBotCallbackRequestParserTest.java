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

package com.linecorp.bot.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.mock.web.MockHttpServletRequest;

import com.google.common.io.ByteStreams;

import com.linecorp.bot.model.event.CallbackRequest;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.parser.LineSignatureValidator;

public class LineBotCallbackRequestParserTest {
    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Spy
    private final LineSignatureValidator lineSignatureValidator = new LineSignatureValidator(
            "SECRET".getBytes(StandardCharsets.UTF_8));

    private LineBotCallbackRequestParser lineBotCallbackRequestParser;

    @Before
    public void before() {
        lineBotCallbackRequestParser = new LineBotCallbackRequestParser(lineSignatureValidator);
    }

    @Test
    public void testMissingHeader() {
        final MockHttpServletRequest request = new MockHttpServletRequest();

        assertThatThrownBy(() -> lineBotCallbackRequestParser.handle(request))
                .isInstanceOf(LineBotCallbackException.class)
                .hasMessage("Missing 'X-Line-Signature' header");
    }

    @Test
    public void testInvalidSignature() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Line-Signature", "SSSSIGNATURE");
        request.setContent("{}".getBytes(StandardCharsets.UTF_8));

        assertThatThrownBy(() -> lineBotCallbackRequestParser.handle(request))
                .isInstanceOf(LineBotCallbackException.class)
                .hasMessage("Invalid API signature");
    }

    @Test
    public void testNullRequest() {
        final byte[] requestBody = "null".getBytes(StandardCharsets.UTF_8);

        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Line-Signature", "SSSSIGNATURE");
        request.setContent(requestBody);

        doReturn(true).when(lineSignatureValidator).validateSignature(requestBody, "SSSSIGNATURE");

        assertThatThrownBy(() -> lineBotCallbackRequestParser.handle(request))
                .isInstanceOf(LineBotCallbackException.class)
                .hasMessage("Invalid content");
    }

    @Test
    public void testCallRequest() throws Exception {
        final InputStream resource = getClass().getClassLoader().getResourceAsStream("callback-request.json");
        final byte[] requestBody = ByteStreams.toByteArray(resource);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Line-Signature", "SSSSIGNATURE");
        request.setContent(requestBody);

        doReturn(true).when(lineSignatureValidator).validateSignature(requestBody, "SSSSIGNATURE");

        final CallbackRequest callbackRequest = lineBotCallbackRequestParser.handle(request);

        assertThat(callbackRequest).isNotNull();

        final List<Event> result = callbackRequest.getEvents();

        final MessageEvent messageEvent = (MessageEvent) result.get(0);
        final TextMessageContent text = (TextMessageContent) messageEvent.getMessage();
        assertThat(text.getText()).isEqualTo("Hello, world");

        final String followedUserId = messageEvent.getSource().getUserId();
        assertThat(followedUserId).isEqualTo("u206d25c2ea6bd87c17655609a1c37cb8");
        assertThat(messageEvent.getTimestamp()).isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z"));
    }
}
