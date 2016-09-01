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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;

import com.linecorp.bot.client.LineBotClient;
import com.linecorp.bot.client.LineBotClientBuilder;
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
    private LineBotClient lineBotClient = LineBotClientBuilder.create("TOKEN").build();
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
        this.lineBotCallbackRequestParser = new LineBotCallbackRequestParser(lineBotClient,
                                                                             lineSignatureValidator);
    }

    @Test
    public void testMissingHeader() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        lineBotCallbackRequestParser.handle(
                request,
                response
        );

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST,
                                   "Missing 'X-Line-Signature' header");
    }

    @Test
    public void testInvalidSignature() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Line-Signature", "SSSSIGNATURE");
        request.setContent("{}".getBytes(StandardCharsets.UTF_8));
        lineBotCallbackRequestParser.handle(
                request,
                response
        );

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST,
                                   "Invalid API signature");
    }

    @Test
    public void testNullRequest() throws Exception {
        final byte[] requestBody = "null".getBytes(StandardCharsets.UTF_8);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Line-Signature", "SSSSIGNATURE");
        request.setContent(requestBody);

        doReturn(true).when(lineSignatureValidator).validateSignature(requestBody, "SSSSIGNATURE");

        lineBotCallbackRequestParser.handle(
                request,
                response
        );

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST,
                                   "Invalid Callback");
    }

    @Test
    public void testCallRequest() throws Exception {
        InputStream resource = getClass().getClassLoader().getResourceAsStream("callback-request.json");
        byte[] requestBody = IOUtils.toByteArray(resource);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Line-Signature", "SSSSIGNATURE");
        request.setContent(requestBody);

        doReturn(true).when(lineSignatureValidator).validateSignature(requestBody, "SSSSIGNATURE");

        CallbackRequest callbackRequest = lineBotCallbackRequestParser.handle(
                request,
                response
        );
        Assert.assertNotNull(callbackRequest);

        final List<Event> result = callbackRequest.getEvents();

        final TextMessageContent text = (TextMessageContent) ((MessageEvent) result.get(0)).getMessage();
        assertEquals("Hello, world", text.getText());

        final String followedUserId = result.get(0).getSource().getUserId();
        assertEquals("u206d25c2ea6bd87c17655609a1c37cb8", followedUserId);
    }
}
