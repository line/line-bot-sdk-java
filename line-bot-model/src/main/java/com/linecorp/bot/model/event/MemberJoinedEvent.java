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

import com.linecorp.bot.model.event.source.Source;

import lombok.Value;

/**
 * Event object for when a user joins a group or room that the bot is in.
 */
@Value
@JsonTypeName("memberJoined")
public class MemberJoinedEvent implements Event, ReplyEvent {
    /**
     * Token for replying to this event.
     */
    private final String replyToken;

    /**
     * JSON object which contains the source of the event.
     */
    private final Source source;

    /**
     * Time of the event.
     */
    private final Instant timestamp;

    /**
     * User ID of users who joined.
     */
    private final JoinedMembers joined;

    @JsonCreator
    public MemberJoinedEvent(
            @JsonProperty("replyToken") final String replyToken,
            @JsonProperty("source") final Source source,
            @JsonProperty("joined") final JoinedMembers joined,
            @JsonProperty("timestamp") final Instant timestamp) {
        this.replyToken = replyToken;
        this.source = source;
        this.joined = joined;
        this.timestamp = timestamp;
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
