/*
 * Copyright 2018 LINE Corporation
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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import com.linecorp.bot.model.event.source.Source;

/**
 * Abstract interface of events.
 */
@JsonSubTypes({
        @JsonSubTypes.Type(MessageEvent.class),
        @JsonSubTypes.Type(UnfollowEvent.class),
        @JsonSubTypes.Type(FollowEvent.class),
        @JsonSubTypes.Type(JoinEvent.class),
        @JsonSubTypes.Type(LeaveEvent.class),
        @JsonSubTypes.Type(PostbackEvent.class),
        @JsonSubTypes.Type(BeaconEvent.class),
        @JsonSubTypes.Type(AccountLinkEvent.class),
        @JsonSubTypes.Type(ThingsEvent.class),
        @JsonSubTypes.Type(MemberJoinedEvent.class),
        @JsonSubTypes.Type(MemberLeftEvent.class),
        @JsonSubTypes.Type(UnsendEvent.class),
        @JsonSubTypes.Type(VideoPlayCompleteEvent.class)
})
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        defaultImpl = UnknownEvent.class,
        visible = true
)
public interface Event {
    /** Get event source. */
    Source getSource();

    /** Time of the event. */
    Instant getTimestamp();

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
    EventMode getMode();
}
