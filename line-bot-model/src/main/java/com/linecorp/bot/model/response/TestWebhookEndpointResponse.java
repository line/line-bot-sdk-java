/*
 * Copyright 2020 LINE Corporation
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

import java.time.Instant;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.response.TestWebhookEndpointResponse.TestWebhookEndpointResponseBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = TestWebhookEndpointResponseBuilder.class)
public class TestWebhookEndpointResponse {
    /**
     * Result of the communication from the LINE platform to the webhook URL.
     */
    boolean success;

    /**
     * Please refer to <a href="https://developers.line.biz/en/reference/messaging-api/#common-properties">
     *     Common Properties</a>.
     */
    Instant timestamp;

    /**
     * The HTTP status code. If the webhook response isn't received, the status code is set to zero or a
     * negative number.
     */
    int statusCode;

    /**
     * Reason for the response.
     */
    String reason;

    /**
     * Details of the response.
     */
    String detail;

    @JsonPOJOBuilder(withPrefix = "")
    public static class TestWebhookEndpointResponseBuilder {
    }
}
