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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.linecorp.bot.model.event.source.Source;
import lombok.Value;

import java.time.Instant;
import java.util.List;

/**
 * Event object for when others leave a room or a group where your bot participates in.
 * This event is sent only when the bot is turned on group participation.
 */
@Value
@JsonTypeName("memberLeft")
public class MemberLeftEvent implements Event {
    /**
     * JSON object which contains the source of the event
     */
    private final Source source;

    /**
     * Per-channel UserSource list of members left.
     */
    private final List<Source> members;

    /**
     * Time of the event
     */
    private final Instant timestamp;

    @JsonCreator
    public MemberLeftEvent(
            final Source source,
            final List<Source> members,
            final Instant timestamp) {
        this.source = source;
        this.members = members;
        this.timestamp = timestamp;
    }
}
