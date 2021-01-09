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

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;

/**
 * Request object for webhook.
 */
@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = CallbackRequest.CallbackRequestBuilder.class)
public class CallbackRequest {
    @JsonPOJOBuilder(withPrefix = "")
    public static class CallbackRequestBuilder {
        // Providing builder instead of public constructor. Class body is filled by lombok.
    }

    /**
     * A user ID of a bot that should receive webhook events. The user ID value is
     * a string that matches the regular expression, {@code U[0-9a-f]{32}}.
     */
    String destination;

    /**
     * List of events.
     */
    List<Event> events;
}
