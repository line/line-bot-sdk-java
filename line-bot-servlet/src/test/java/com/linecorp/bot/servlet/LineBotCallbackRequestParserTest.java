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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
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
import com.linecorp.bot.model.event.CallbackRequest;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;

@RunWith(MockitoJUnitRunner.class)
public class LineBotCallbackRequestParserTest {
    @Mock
    private HttpServletResponse response;

    @Spy
    private LineBotClient lineBotClient = LineBotClientBuilder.create("SECRET", "TOKEN").build();

    private LineBotCallbackRequestParser lineBotCallbackRequestParser;

    @Before
    public void before() throws IOException {
        when(response.getWriter())
                .thenReturn(mock(PrintWriter.class));
        this.lineBotCallbackRequestParser = new LineBotCallbackRequestParser(lineBotClient);
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
        request.addHeader("X-Line-Channel-Signature", "SSSSIGNATURE");
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
        request.addHeader("X-Line-Channel-Signature", "SSSSIGNATURE");
        request.setContent(requestBody);

        doReturn(true).when(lineBotClient).validateSignature(requestBody, "SSSSIGNATURE");

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
        request.addHeader("X-Line-Channel-Signature", "SSSSIGNATURE");
        request.setContent(requestBody);

        doReturn(true).when(lineBotClient).validateSignature(requestBody, "SSSSIGNATURE");

        CallbackRequest callbackRequest = lineBotCallbackRequestParser.handle(
                request,
                response
        );
        Assert.assertNotNull(callbackRequest);

        final List<Event> result = callbackRequest.getEvents();

        final TextMessageContent text = (TextMessageContent) ((MessageEvent) result.get(0)).getMessage();
        assertThat(text.getText(), is("Hello, BOT API Server!"));

//        final AddedAsFriendOperation addFriend = (AddedAsFriendOperation) result.get(1).getContent();
//        assertThat(addFriend.getMid(), is("u464471c59f5eefe815a19be11f210147"));
    }
}
