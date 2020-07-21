/*
 * Copyright 2018 LINE Corporation
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

package com.linecorp.bot.spring.boot.support;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;
import static java.util.Collections.singletonList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.exception.GeneralLineMessagingException;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.test.EventTestUtil;

public class ReplyByReturnValueConsumerTest {
    private static final MessageEvent EVENT = EventTestUtil.createTextMessage("text");

    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private LineMessagingClient lineMessagingClient;

    @InjectMocks
    private ReplyByReturnValueConsumer.Factory targetFactory;

    private ReplyByReturnValueConsumer target;

    @Before
    public void setUp() {
        target = targetFactory.createForEvent(EVENT);
        when(lineMessagingClient.replyMessage(any()))
                .thenReturn(completedFuture(new BotApiResponse("", " success", null)));
    }

    // Public methods test

    @Test
    public void acceptSingleReplyTest() throws Exception {
        // Do
        target.accept(new TextMessage("Reply Text"));

        // Verify
        verify(lineMessagingClient, only())
                .replyMessage(new ReplyMessage(EVENT.getReplyToken(),
                                               singletonList(new TextMessage("Reply Text"))));
    }

    @Test
    public void acceptListReplyTest() throws Exception {
        // Do
        target.accept(singletonList(new TextMessage("Reply Text")));

        // Verify
        verify(lineMessagingClient, only())
                .replyMessage(new ReplyMessage(EVENT.getReplyToken(),
                                               singletonList(new TextMessage("Reply Text"))));
    }

    @Test
    public void acceptCompletableSingleReplyTest() throws Exception {
        // Do
        final CompletableFuture<TextMessage> returnValue = new CompletableFuture<>();
        target.accept(returnValue);
        returnValue.complete(new TextMessage("Reply Text"));

        // Verify
        verify(lineMessagingClient, only())
                .replyMessage(new ReplyMessage(EVENT.getReplyToken(),
                                               singletonList(new TextMessage("Reply Text"))));
    }

    @Test
    public void acceptCompletableListReplyTest() throws Exception {
        // Do
        final CompletableFuture<List<TextMessage>> returnValue = new CompletableFuture<>();
        target.accept(returnValue);
        returnValue.complete(singletonList(new TextMessage("Reply Text")));

        // Verify
        verify(lineMessagingClient, only())
                .replyMessage(new ReplyMessage(EVENT.getReplyToken(),
                                               singletonList(new TextMessage("Reply Text"))));
    }

    @Test
    public void errorInCompletableLoggingTest() throws Exception {
        // Do
        String systemOut = tapSystemOut(() -> {
            final CompletableFuture<List<TextMessage>> returnValue = new CompletableFuture<>();
            target.accept(returnValue);
            returnValue.completeExceptionally(
                    new GeneralLineMessagingException("EXCEPTION HAPPEN!", null, null));
        });

        // Verify
        assertThat(systemOut)
                .contains("EXCEPTION HAPPEN!");
    }

    @Test
    public void errorInLineMessagingClientLoggingTest() throws Exception {
        reset(lineMessagingClient);
        when(lineMessagingClient.replyMessage(any()))
                .thenReturn(new CompletableFuture<BotApiResponse>() {{
                    completeExceptionally(new GeneralLineMessagingException("EXCEPTION HAPPEN!", null, null));
                }});

        // Do
        String systemOut = tapSystemOut(() -> {
            final CompletableFuture<List<TextMessage>> returnValue = new CompletableFuture<>();
            target.accept(returnValue);
            returnValue.complete(singletonList(new TextMessage("Reply Text")));
        });

        // Verify
        assertThat(systemOut)
                .contains("failed")
                .contains("EXCEPTION HAPPEN!");
    }

    // Internal method test.
    @Test
    public void checkListContentsNullTest() throws Exception {
        // Do
        assertThatThrownBy(() -> ReplyByReturnValueConsumer.checkListContents(singletonList(null)))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void checkListContentsIllegalTypeTest() throws Exception {
        // Do
        assertThatThrownBy(() -> ReplyByReturnValueConsumer.checkListContents(singletonList(new Object())))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
