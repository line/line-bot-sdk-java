/*
 * Copyright (c) 2019 LINE Corporation. All rights reserved.
 * LINE Corporation PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.linecorp.bot.model.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.linecorp.bot.model.event.source.Source;
import lombok.Value;

import java.time.Instant;
import java.util.List;

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
     * User ID of users who joined
     */
    private final JoinedMembers joined;

    @JsonCreator
    public MemberJoinedEvent(
            final String replyToken,
            final Source source,
            final JoinedMembers joined,
            final Instant timestamp) {
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
        public JoinedMembers(List<Source> members) {
            this.members = members;
        }
    }
}
