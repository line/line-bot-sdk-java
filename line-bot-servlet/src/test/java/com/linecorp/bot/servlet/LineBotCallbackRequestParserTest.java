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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;

import com.google.common.io.ByteStreams;

import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.model.event.CallbackRequest;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;

@RunWith(MockitoJUnitRunner.class)
public class LineBotCallbackRequestParserTest {
    @Mock
    private HttpServletResponse response;

    @Spy
    private LineSignatureValidator lineSignatureValidator = new LineSignatureValidator(
            "SECRET".getBytes(StandardCharsets.UTF_8));

    private LineBotCallbackRequestParser lineBotCallbackRequestParser;

    public LineBotCallbackRequestParserTest() throws InvalidKeyException, NoSuchAlgorithmException {
    }

    @Before
    public void before() throws IOException {
        when(response.getWriter())
                .thenReturn(mock(PrintWriter.class));
        this.lineBotCallbackRequestParser = new LineBotCallbackRequestParser(lineSignatureValidator);
    }

    @Test
    public void testMissingHeader() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();

        assertThatThrownBy(() -> lineBotCallbackRequestParser.handle(request))
                .isInstanceOf(LineBotCallbackException.class)
                .hasMessage("Missing 'X-Line-Signature' header");
    }
    
    @Test
    public void testMissingHeader2() throws Exception {
        assertThatThrownBy(() -> lineBotCallbackRequestParser.handle("", ""))
        .isInstanceOf(LineBotCallbackException.class)
        .hasMessage("Missing 'X-Line-Signature' header");
    }

    @Test
    public void testInvalidSignature() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Line-Signature", "SSSSIGNATURE");
        request.setContent("{}".getBytes(StandardCharsets.UTF_8));

        assertThatThrownBy(() -> lineBotCallbackRequestParser.handle(request))
                .isInstanceOf(LineBotCallbackException.class)
                .hasMessage("Invalid API signature");
    }
    
    @Test
    public void testInvalidSignature2() throws Exception {
        final String signature="SSSSIGNATURE";
        final String content="{}";
        
        assertThatThrownBy(() -> lineBotCallbackRequestParser.handle(signature, content))
        .isInstanceOf(LineBotCallbackException.class)
        .hasMessage("Invalid API signature");
    }

    @Test
    public void testNullRequest() throws Exception {
        final byte[] requestBody = "null".getBytes(StandardCharsets.UTF_8);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Line-Signature", "SSSSIGNATURE");
        request.setContent(requestBody);

        doReturn(true).when(lineSignatureValidator).validateSignature(requestBody, "SSSSIGNATURE");

        assertThatThrownBy(() -> lineBotCallbackRequestParser.handle(request))
                .isInstanceOf(LineBotCallbackException.class)
                .hasMessage("Invalid content");
    }
    
    @Test
    public void testNullRequest2() throws Exception {
        final String signature="SSSSIGNATURE";
        final String content="null";
        
        doReturn(true).when(lineSignatureValidator).validateSignature(content.getBytes(StandardCharsets.UTF_8), signature);
        
        assertThatThrownBy(() -> lineBotCallbackRequestParser.handle(signature, content))
        .isInstanceOf(LineBotCallbackException.class)
        .hasMessage("Invalid content");
    }

    @Test
    public void testCallRequest() throws Exception {
        InputStream resource = getClass().getClassLoader().getResourceAsStream("callback-request.json");
        byte[] requestBody = ByteStreams.toByteArray(resource);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Line-Signature", "SSSSIGNATURE");
        request.setContent(requestBody);

        doReturn(true).when(lineSignatureValidator).validateSignature(requestBody, "SSSSIGNATURE");

        CallbackRequest callbackRequest = lineBotCallbackRequestParser.handle(request);

        assertThat(callbackRequest).isNotNull();

        final List<Event> result = callbackRequest.getEvents();

        final MessageEvent messageEvent = (MessageEvent) result.get(0);
        final TextMessageContent text = (TextMessageContent) messageEvent.getMessage();
        assertThat(text.getText()).isEqualTo("Hello, world");

        final String followedUserId = messageEvent.getSource().getUserId();
        assertThat(followedUserId).isEqualTo("u206d25c2ea6bd87c17655609a1c37cb8");
        assertThat(messageEvent.getTimestamp()).isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z"));
    }
    
    @Test
    public void testCallRequest2() throws Exception {
        InputStream resource = getClass().getClassLoader().getResourceAsStream("callback-request.json");
        byte[] requestBody = ByteStreams.toByteArray(resource);
        
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Line-Signature", "SSSSIGNATURE");
        request.setContent(requestBody);
        
        doReturn(true).when(lineSignatureValidator).validateSignature(requestBody, "SSSSIGNATURE");
        
        CallbackRequest callbackRequest = lineBotCallbackRequestParser.handle("SSSSIGNATURE", new String(requestBody, StandardCharsets.UTF_8));
        
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
