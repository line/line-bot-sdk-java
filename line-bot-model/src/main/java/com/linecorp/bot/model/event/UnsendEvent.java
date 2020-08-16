/*
 * Copyright 2020 LINE Corporation
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

import com.linecorp.bot.model.event.source.Source;

import lombok.Builder;
import lombok.Value;

/**
 * Event object for when the user unsends a message in a group or room.
 */
@JsonTypeName("unsend")
@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = UnsendEvent.UnsendEventBuilder.class)
public class UnsendEvent implements Event {
    @JsonPOJOBuilder(withPrefix = "")
    public static class UnsendEventBuilder {
        // Providing builder instead of public constructor. Class body is filled by lombok.
    }

    /**
     * JSON object which contains the source of the event.
     */
    Source source;

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

    UnsendDetail unsend;

    @Value
    @Builder(toBuilder = true)
    @JsonDeserialize(builder = UnsendDetail.UnsendDetailBuilder.class)
    public static class UnsendDetail {
        @JsonPOJOBuilder(withPrefix = "")
        public static class UnsendDetailBuilder {
            // Providing builder instead of public constructor. Class body is filled by lombok.
        }

        /**
         * The message ID of the unsent message.
         */
        String messageId;
    }
}
