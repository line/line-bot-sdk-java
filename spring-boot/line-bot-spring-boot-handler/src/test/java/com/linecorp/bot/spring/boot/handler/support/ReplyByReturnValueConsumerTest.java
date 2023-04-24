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

package com.linecorp.bot.spring.boot.handler.support;

import static java.util.Collections.singletonList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.linecorp.bot.messaging.client.MessagingApiClient;
import com.linecorp.bot.messaging.model.ReplyMessageRequest;
import com.linecorp.bot.messaging.model.TextMessage;
import com.linecorp.bot.webhook.model.MessageEvent;

@ExtendWith(MockitoExtension.class)
public class ReplyByReturnValueConsumerTest {
    private static final MessageEvent EVENT = EventTestUtil.createTextMessage("text");

    @Mock
    private MessagingApiClient messagingApiClient;

    @InjectMocks
    private ReplyByReturnValueConsumer.Factory targetFactory;

    private ReplyByReturnValueConsumer target;

    @BeforeEach
    public void setUp() {
        target = targetFactory.createForEvent(EVENT);
    }

    // Public methods test

    @Test
    public void acceptSingleReplyTest() throws Exception {
        when(messagingApiClient.replyMessage(any()))
                .thenReturn(completedFuture(null));

        // Do
        target.accept(new TextMessage("Reply Text"));

        // Verify
        verify(messagingApiClient, only())
                .replyMessage(new ReplyMessageRequest(EVENT.replyToken(),
                        singletonList(new TextMessage("Reply Text")),
                        false));
    }

    @Test
    public void acceptListReplyTest() throws Exception {
        when(messagingApiClient.replyMessage(any()))
                .thenReturn(completedFuture(null));
        // Do
        target.accept(singletonList(new TextMessage("Reply Text")));

        // Verify
        verify(messagingApiClient, only())
                .replyMessage(new ReplyMessageRequest(EVENT.replyToken(),
                        singletonList(new TextMessage("Reply Text")),
                        false
                ));
    }

    @Test
    public void acceptCompletableSingleReplyTest() throws Exception {
        // Do
        final CompletableFuture<TextMessage> returnValue = new CompletableFuture<>();
        target.accept(returnValue);
        returnValue.complete(new TextMessage("Reply Text"));

        // Verify
        verify(messagingApiClient, only())
                .replyMessage(new ReplyMessageRequest(EVENT.replyToken(),
                        singletonList(new TextMessage("Reply Text")),
                        false));
    }

    @Test
    public void acceptCompletableListReplyTest() throws Exception {
        // Do
        final CompletableFuture<List<TextMessage>> returnValue = new CompletableFuture<>();
        target.accept(returnValue);
        returnValue.complete(singletonList(new TextMessage("Reply Text")));

        // Verify
        verify(messagingApiClient, only())
                .replyMessage(new ReplyMessageRequest(EVENT.replyToken(),
                        singletonList(new TextMessage("Reply Text")),
                        false));
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
    public void errorInMessagingApiClientLoggingTest() throws Exception {
        reset(MessagingApiClient);
        when(MessagingApiClient.replyMessage(any()))
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
