/*
 * Copyright 2019 LINE Corporation
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
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.event.source.Source;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/**
 * Event object for when a user joins a group or room that the bot is in.
 */
@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonTypeName("memberJoined")
@JsonDeserialize(builder = MemberJoinedEvent.MemberJoinedEventBuilder.class)
public class MemberJoinedEvent implements Event, ReplyEvent {
    /**
     * Token for replying to this event.
     */
    String replyToken;

    /**
     * JSON object which contains the source of the event.
     */
    Source source;

    /**
     * Time of the event.
     */
    Instant timestamp;

    /**
     * User ID of users who joined.
     */
    JoinedMembers joined;

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
    public MemberJoinedEvent(
            @JsonProperty("replyToken") final String replyToken,
            @JsonProperty("source") final Source source,
            @JsonProperty("joined") final JoinedMembers joined,
            @JsonProperty("timestamp") final Instant timestamp) {
        this(replyToken, source, timestamp, joined, null);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class MemberJoinedEventBuilder {
        // Filled by lombok
    }

    @Value
    public static class JoinedMembers {
        // User ID of users who joined
        List<Source> members;

        @JsonCreator
        public JoinedMembers(@JsonProperty("members") List<Source> members) {
            this.members = members;
        }
    }
}
