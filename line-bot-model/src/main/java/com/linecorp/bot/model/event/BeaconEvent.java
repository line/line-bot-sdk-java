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

import com.linecorp.bot.model.event.beacon.BeaconContent;
import com.linecorp.bot.model.event.source.Source;

import lombok.Value;

/**
 * Event object for when a user detects a LINE Beacon. You can reply to beacon events.
 */
@Value
@JsonTypeName("beacon")
public class BeaconEvent implements Event, ReplyEvent {
    /**
     * Token for replying to this event
     */
    private final String replyToken;

    /**
     * JSON object which contains the source of the event
     */
    private final Source source;

    /**
     * Content of the beacon event.
     */
    private final BeaconContent beacon;

    /**
     * Time of the event
     */
    private final Instant timestamp;

    @JsonCreator
    public BeaconEvent(
            @JsonProperty("replyToken") String replyToken,
            @JsonProperty("source") Source source,
            @JsonProperty("timestamp") Instant timestamp,
            @JsonProperty("beacon") BeaconContent beacon
    ) {
        this.replyToken = replyToken;
        this.source = source;
        this.timestamp = timestamp;
        this.beacon = beacon;
    }
}
