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

import com.fasterxml.jackson.annotation.JsonProperty;

import com.linecorp.bot.model.event.source.Source;

import lombok.Value;

/**
 * Fallback event type for {@link Event}.
 */
@Value
public class UnknownEvent implements Event {
    /**
     * Type of the event.
     */
    private final String type;

    /**
     * JSON object which contains the source of the event
     */
    private final Source source;

    /**
     * Time of the event
     */
    private final Instant timestamp;

    public UnknownEvent(
            @JsonProperty("type") String type,
            @JsonProperty("source") Source source,
            @JsonProperty("timestamp") Instant timestamp
    ) {
        this.type = type;
        this.source = source;
        this.timestamp = timestamp;
    }
}
