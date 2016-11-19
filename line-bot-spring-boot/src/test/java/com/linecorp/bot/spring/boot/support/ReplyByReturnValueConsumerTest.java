package com.linecorp.bot.spring.boot.support;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.rules.ExpectedException;
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

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Rule
    public final SystemOutRule systemOut = new SystemOutRule().enableLog();

    @Mock
    private LineMessagingClient lineMessagingClient;

    @InjectMocks
    private ReplyByReturnValueConsumer.Factory targetFactory;

    private ReplyByReturnValueConsumer target;

    @Before
    public void setUp() {
        target = targetFactory.createForEvent(EVENT);
        when(lineMessagingClient.replyMessage(any()))
                .thenReturn(CompletableFuture.completedFuture(new BotApiResponse("success", null)));
    }

    // Publich methods test

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
    public void errorInCompletableLoggingTest() {
        // Do
        final CompletableFuture<List<TextMessage>> returnValue = new CompletableFuture<>();
        target.accept(returnValue);
        returnValue.completeExceptionally(new GeneralLineMessagingException("EXCEPTION HAPPEN!", null, null));

        // Verify
        assertThat(systemOut.getLogWithNormalizedLineSeparator())
                .contains("EXCEPTION HAPPEN!");
    }

    @Test
    public void errorInLineMessagingClientLoggingTest() {
        reset(lineMessagingClient);
        when(lineMessagingClient.replyMessage(any()))
                .thenReturn(new CompletableFuture<BotApiResponse>() {{
                    completeExceptionally(new GeneralLineMessagingException("EXCEPTION HAPPEN!", null, null));
                }});

        // Do
        final CompletableFuture<List<TextMessage>> returnValue = new CompletableFuture<>();
        target.accept(returnValue);
        returnValue.complete(singletonList(new TextMessage("Reply Text")));

        // Verify
        assertThat(systemOut.getLogWithNormalizedLineSeparator())
                .contains("failed")
                .contains("EXCEPTION HAPPEN!");
    }

    // Internal method test.
    @Test
    public void checkListContentsNullTest() throws Exception {
        expectedException.expect(NullPointerException.class);

        // Do
        ReplyByReturnValueConsumer.checkListContents(singletonList(null));
    }

    @Test
    public void checkListContentsIllegalTypeTest() throws Exception {
        expectedException.expect(IllegalArgumentException.class);

        // Do
        ReplyByReturnValueConsumer.checkListContents(singletonList(new Object()));
    }
}
