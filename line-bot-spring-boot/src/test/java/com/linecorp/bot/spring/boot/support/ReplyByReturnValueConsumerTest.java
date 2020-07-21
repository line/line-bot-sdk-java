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

<<<<<<< HEAD
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
=======
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
>>>>>>> @{-1}
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.exception.GeneralLineMessagingException;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.test.EventTestUtil;

@ExtendWith(OutputCaptureExtension.class)
@ExtendWith(MockitoExtension.class)
public class ReplyByReturnValueConsumerTest {
    private static final MessageEvent EVENT = EventTestUtil.createTextMessage("text");

<<<<<<< HEAD
=======
    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

>>>>>>> @{-1}
    @Mock
    private LineMessagingClient lineMessagingClient;

    @InjectMocks
    private ReplyByReturnValueConsumer.Factory targetFactory;

    private ReplyByReturnValueConsumer target;

    @BeforeEach
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
<<<<<<< HEAD
    public void errorInCompletableLoggingTest(CapturedOutput output) {
=======
    public void errorInCompletableLoggingTest() throws Exception {
>>>>>>> @{-1}
        // Do
        String systemOut = tapSystemOut(() -> {
            final CompletableFuture<List<TextMessage>> returnValue = new CompletableFuture<>();
            target.accept(returnValue);
            returnValue.completeExceptionally(
                    new GeneralLineMessagingException("EXCEPTION HAPPEN!", null, null));
        });

        // Verify
<<<<<<< HEAD
        assertThat(output.getOut())
=======
        assertThat(systemOut)
>>>>>>> @{-1}
                .contains("EXCEPTION HAPPEN!");
    }

    @Test
<<<<<<< HEAD
    public void errorInLineMessagingClientLoggingTest(CapturedOutput output) {
=======
    public void errorInLineMessagingClientLoggingTest() throws Exception {
>>>>>>> @{-1}
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
<<<<<<< HEAD
        assertThat(output.getOut())
=======
        assertThat(systemOut)
>>>>>>> @{-1}
                .contains("failed")
                .contains("EXCEPTION HAPPEN!");
    }

    // Internal method test.
    @Test
    public void checkListContentsNullTest() throws Exception {
<<<<<<< HEAD
=======
        // Do
>>>>>>> @{-1}
        assertThatThrownBy(() -> ReplyByReturnValueConsumer.checkListContents(singletonList(null)))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void checkListContentsIllegalTypeTest() throws Exception {
<<<<<<< HEAD
=======
        // Do
>>>>>>> @{-1}
        assertThatThrownBy(() -> ReplyByReturnValueConsumer.checkListContents(singletonList(new Object())))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
