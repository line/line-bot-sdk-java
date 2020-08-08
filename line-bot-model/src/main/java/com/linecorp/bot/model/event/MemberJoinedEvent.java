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

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.event.source.Source;

import lombok.Builder;
import lombok.Value;

/**
 * Event object for when a user joins a group or room that the bot is in.
 */
@JsonTypeName("memberJoined")
@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = MemberJoinedEvent.MemberJoinedEventBuilder.class)
public class MemberJoinedEvent implements Event, ReplyEvent {
    @JsonPOJOBuilder(withPrefix = "")
    public static class MemberJoinedEventBuilder {
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

    @Value
    @Builder(toBuilder = true)
    @JsonDeserialize(builder = JoinedMembers.JoinedMembersBuilder.class)
    public static class JoinedMembers {
        @JsonPOJOBuilder(withPrefix = "")
        public static class JoinedMembersBuilder {
            // Providing builder instead of public constructor. Class body is filled by lombok.
        }

        // User ID of users who joined
        List<Source> members;
    }
}
