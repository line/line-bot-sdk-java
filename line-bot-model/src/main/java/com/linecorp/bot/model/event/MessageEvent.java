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

package com.linecorp.bot.model.event;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.source.Source;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/**
 * Event object which contains the sent message.
 * The message field contains a message object which corresponds with the message type.
 * You can reply to message events.
 */
@JsonTypeName("message")
@Value
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__(@Deprecated))
// TODO: Remove next release. Use builder() instead.
@JsonDeserialize(builder = MessageEvent.MessageEventBuilder.class)
public class MessageEvent<T extends MessageContent> implements Event, ReplyEvent {
    @JsonPOJOBuilder(withPrefix = "")
    public static class MessageEventBuilder<T extends MessageContent> {
        // Providing builder instead of public constructor. Class body is filled by lombok.
    }

    /**
     * Token for replying to this event.
     */
    String replyToken;

    /**
     * JSON object which contains the source of the event.
     */
    Source source;

    /**
     * Message body.
     */
    T message;

    /**
     * Time of the event.
     */
    Instant timestamp;

    /**
     * Channel state.
     * <dl>
     * <dt>active</dt>
     * <dd>The channel is active. You can send a reply message or push message from the bot server that received
     * this webhook event.</dd>
     * <dt>standby (under development)</dt>
     * <dd>The channel is waiting. The bot server that received this webhook event shouldn't send any messages.
     * </dd>
     * </dl>
     */
    EventMode mode;

    /**
     * Deprecated constructor.
     *
     * @deprecated Use builder method instead. This construct will remove in next major release.
     */
    @Deprecated
    public MessageEvent(
            final String replyToken,
            final Source source,
            final T message,
            final Instant timestamp) {
        this(replyToken, source, message, timestamp, null);
    }
}
