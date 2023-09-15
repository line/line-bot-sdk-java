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

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linecorp.bot.client.base.Result;
import com.linecorp.bot.messaging.client.MessagingApiClient;
import com.linecorp.bot.messaging.model.Message;
import com.linecorp.bot.messaging.model.ReplyMessageRequest;
import com.linecorp.bot.messaging.model.ReplyMessageResponse;
import com.linecorp.bot.webhook.model.Event;
import com.linecorp.bot.webhook.model.ReplyEvent;

/**
 * Internal class to send message as reply.
 *
 * <p>Basically, message contents are from return value of handler method.
 */
class ReplyByReturnValueConsumer implements Consumer<Object> {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ReplyByReturnValueConsumer.class);
    private final MessagingApiClient messagingApiClient;
    private final Event originalEvent;

    ReplyByReturnValueConsumer(MessagingApiClient messagingApiClient, Event originalEvent) {
        this.messagingApiClient = messagingApiClient;
        this.originalEvent = originalEvent;
    }

    @Component
    static class Factory {
        private final MessagingApiClient messagingApiClient;

        @Autowired
        Factory(final MessagingApiClient messagingApiClient) {
            this.messagingApiClient = messagingApiClient;
        }

        ReplyByReturnValueConsumer createForEvent(final Event event) {
            return new ReplyByReturnValueConsumer(messagingApiClient, event);
        }
    }

    @Override
    public void accept(final Object returnValue) {
        if (returnValue instanceof CompletableFuture) {
            // accept when future complete.
            ((CompletableFuture<?>) returnValue)
                    .whenComplete(this::whenComplete);
        } else {
            // accept immediately.
            acceptResult(returnValue);
        }
    }

    private void whenComplete(final Object futureResult, final Throwable throwable) {
        if (throwable != null) {
            log.error("Method return value waited but exception occurred in CompletedFuture", throwable);
            return;
        }

        acceptResult(futureResult);
    }

    private void acceptResult(final Object returnValue) {
        if (returnValue instanceof Message) {
            reply(singletonList((Message) returnValue));
        } else if (returnValue instanceof List<?> returnValueAsList) {
            if (returnValueAsList.isEmpty()) {
                return;
            }

            reply(checkListContents(returnValueAsList));
        }
    }

    private void reply(final List<Message> messages) {
        final ReplyEvent replyEvent = (ReplyEvent) originalEvent;
        messagingApiClient.replyMessage(new ReplyMessageRequest(replyEvent.replyToken(), messages, false))
                .whenComplete(this::logging);
        // DO NOT BLOCK HERE, otherwise, next message processing will be BLOCKED.
    }

    private void logging(final Result<ReplyMessageResponse> unused, final Throwable throwable) {
        if (throwable == null) {
            log.debug("Reply message success");
        } else {
            log.warn("Reply message failed: " + throwable.getMessage(), throwable);
        }
    }

    @SuppressWarnings("unchecked")
    // VisibleForTesting
    static List<Message> checkListContents(final List<?> list) {
        for (int i = 0; i < list.size(); ++i) {
            final Object item = list.get(i);
            Objects.requireNonNull(item, "item is null: " + list);
            if (!(item instanceof Message)) {
                throw new IllegalArgumentException(
                        "List contains not Message type object. type = " + item.getClass()
                );
            }
        }

        return (List<Message>) list;
    }
}
