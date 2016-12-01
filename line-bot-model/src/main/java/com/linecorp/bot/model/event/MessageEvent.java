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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.source.Source;

import lombok.Value;

/**
 * Event object which contains the sent message.
 * The message field contains a message object which corresponds with the message type. You can reply to message events.
 */
@Value
@JsonTypeName("message")
public class MessageEvent<T extends MessageContent> implements Event, ReplyEvent {
    /**
     * Token for replying to this event
     */
    private final String replyToken;

    /**
     * JSON object which contains the source of the event
     */
    private final Source source;

    /**
     * Message body
     */
    private final T message;

    /**
     * Time of the event
     */
    private final Instant timestamp;

    @JsonCreator
    public MessageEvent(
            @JsonProperty("replyToken") String replyToken,
            @JsonProperty("source") Source source,
            @JsonProperty("message") T message,
            @JsonProperty("timestamp") Instant timestamp) {
        this.replyToken = replyToken;
        this.source = source;
        this.message = message;
        this.timestamp = timestamp;
    }
}
