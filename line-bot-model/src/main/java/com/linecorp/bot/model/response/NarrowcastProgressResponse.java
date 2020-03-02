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

package com.linecorp.bot.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.response.NarrowcastProgressResponse.NarrowcastProgressResponseBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = NarrowcastProgressResponseBuilder.class)
public class NarrowcastProgressResponse {
    /**
     * The current status.
     */
    Phase phase;

    /**
     * The number of users who successfully received the message.
     * (Not available when phase is waiting)
     */
    Long successCount;

    /**
     * The number of users who failed to receive the message.
     * (Not available when phase is waiting)
     */
    Long failureCount;

    /**
     * The number of intended recipients of the message.
     * (Not available when phase is waiting)
     */
    Long targetCount;

    /**
     * The reason why the message failed to be sent. This is only included with a phase property value of
     * failed.
     */
    String failedDescription;

    /**
     * A brief summary of the error.
     */
    Long errorCode;

    public enum Phase {
        @JsonProperty("waiting")
        WAITING,
        @JsonProperty("sending")
        SENDING,
        @JsonProperty("succeeded")
        SUCCEEDED,
        @JsonProperty("failed")
        FAILED
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class NarrowcastProgressResponseBuilder {
    }
}
