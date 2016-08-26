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

package com.linecorp.bot.model.deprecated.event;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;

/**
 * Response model for /v1/events.
 */
@Getter
@ToString
public class EventResponse {
    private final Integer version;
    private final Long timestamp;
    private final String messageId;

    private final int statusCode;
    private final String statusMessage;
    private final List<String> failed;

    public EventResponse(
            @JsonProperty("version") Integer version,
            @JsonProperty("timestamp") Long timestamp,
            @JsonProperty("messageId") String messageId,
            @JsonProperty("statusCode") int statusCode,
            @JsonProperty("statusMessage") String statusMessage,
            @JsonProperty("failed") List<String> failed) {
        this.version = version;
        this.timestamp = timestamp;
        this.messageId = messageId;
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.failed = failed;
    }

}
