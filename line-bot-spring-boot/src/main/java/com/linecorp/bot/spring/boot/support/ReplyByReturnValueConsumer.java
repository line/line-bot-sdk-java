package com.linecorp.bot.spring.boot.support;

import static java.util.Collections.singletonList;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.linecorp.bot.spring.boot.custom.LineMessagingClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.ReplyEvent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.response.BotApiResponse;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

/**
 * Internal class to send message as reply.
 *
 * Basically, message contents are from return value of handler method.
 *
 * @see LineMessageHandlerSupport#handleReturnValue(Event, Object)
 */
@Slf4j
@Builder
class ReplyByReturnValueConsumer implements BiConsumer<String, Object> {
    private final LineMessagingClientFactory lineMessagingClientFactory;
    private final Event originalEvent;

    @Component
    public static class Factory {
        private final LineMessagingClientFactory lineMessagingClientFactory;

        @Autowired
        public Factory(final LineMessagingClientFactory lineMessagingClientFactory) {
            this.lineMessagingClientFactory = lineMessagingClientFactory;
        }

        ReplyByReturnValueConsumer createForEvent(final Event event) {
            return builder()
                    .lineMessagingClientFactory(lineMessagingClientFactory)
                    .originalEvent(event)
                    .build();
        }
    }

    @Override
    public void accept(final String secretKey, final Object returnValue) {
        if (returnValue instanceof CompletableFuture) {
            // accept when future complete.
            ((CompletableFuture<?>) returnValue)
                    .whenComplete((futureResult, throwable) -> whenComplete(secretKey, futureResult, throwable));
        } else {
            // accept immediately.
            acceptResult(secretKey, returnValue);
        }
    }

    private void whenComplete(final String secretKey, final Object futureResult, final Throwable throwable) {
        if (throwable != null) {
            log.error("Method return value waited but exception occurred in CompletedFuture", throwable);
            return;
        }

        acceptResult(secretKey, futureResult);
    }

    private void acceptResult(final String secretKey, final Object returnValue) {
        if (returnValue instanceof Message) {
            reply(secretKey, singletonList((Message) returnValue));
        } else if (returnValue instanceof List) {
            List<?> returnValueAsList = (List<?>) returnValue;

            if (returnValueAsList.isEmpty()) {
                return;
            }

            reply(secretKey, checkListContents(returnValueAsList));
        }
    }

    private void reply(final String secretKey, final List<Message> messages) {
        final ReplyEvent replyEvent = (ReplyEvent) originalEvent;
        lineMessagingClientFactory.get(secretKey).replyMessage(new ReplyMessage(replyEvent.getReplyToken(), messages))
                           .whenComplete(this::logging);
        // DO NOT BLOCK HERE, otherwise, next message processing will be BLOCKED.
    }

    private void logging(final BotApiResponse botApiResponse, final Throwable throwable) {
        if (throwable == null) {
            log.debug("Reply message success. response = {}", botApiResponse);
        } else {
            log.warn("Reply message failed: {}", throwable.getMessage(), throwable);
        }
    }

    @VisibleForTesting
    static List<Message> checkListContents(final List<?> list) {
        for (int i = 0; i < list.size(); ++i) {
            final Object item = list.get(i);
            Preconditions.checkNotNull(item, "item is null. index = {} in {}", i, list);
            Preconditions.checkArgument(item instanceof Message,
                                        "List contains not Message type object. type = {}",
                                        item.getClass());
        }

        @SuppressWarnings("unchecked")
        final List<Message> messageList = (List<Message>) list;
        return messageList;
    }
}
