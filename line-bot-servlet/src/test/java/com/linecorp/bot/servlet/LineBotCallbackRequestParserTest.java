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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.client.LineBotAPIHeaders;
import com.linecorp.bot.client.LineBotClient;
import com.linecorp.bot.model.callback.CallbackRequest;

@RunWith(MockitoJUnitRunner.class)
public class LineBotCallbackRequestParserTest {
    @Mock
    private HttpServletResponse response;
    @Mock
    private LineBotClient lineBotClient;
    private LineBotCallbackRequestParser lineBotCallbackRequestParser;

    @Before
    public void before() throws IOException {
        when(response.getWriter())
                .thenReturn(mock(PrintWriter.class));
        this.lineBotCallbackRequestParser = new LineBotCallbackRequestParser(
                lineBotClient,
                new ObjectMapper()
        );
    }

    @Test
    public void testMissingHeader() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        lineBotCallbackRequestParser.handle(
                request,
                response
        );

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST,
                                   "Missing 'X-Line-ChannelSignature' header");
    }

    @Test
    public void testInvalidSignature() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(LineBotAPIHeaders.X_LINE_CHANNEL_SIGNATURE, "SSSSIGNATURE");
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
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(LineBotAPIHeaders.X_LINE_CHANNEL_SIGNATURE, "SSSSIGNATURE");
        request.setContent("null".getBytes(StandardCharsets.UTF_8));

        when(lineBotClient.validateSignature("null", "SSSSIGNATURE"))
                .thenReturn(true);

        lineBotCallbackRequestParser.handle(
                request,
                response
        );

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST,
                                   "Result shouldn't be null");
    }

    @Test
    public void testCallRequest() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(LineBotAPIHeaders.X_LINE_CHANNEL_SIGNATURE, "SSSSIGNATURE");
        request.setContent("{\"result\":[{}]}".getBytes(StandardCharsets.UTF_8));

        when(lineBotClient.validateSignature("{\"result\":[{}]}", "SSSSIGNATURE"))
                .thenReturn(true);

        CallbackRequest callbackRequest = lineBotCallbackRequestParser.handle(
                request,
                response
        );
        Assert.assertNotNull(callbackRequest);
    }
}
