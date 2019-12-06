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

package com.linecorp.bot.client;

import static java.util.Collections.emptyList;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.client.BotApiResponseBody.BotApiResponseBodyBuilder;
import com.linecorp.bot.model.response.BotApiResponse;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;

/**
 * Response body of BotApiResponse.
 *
 * <p>Developer will use {@link BotApiResponse} returned by {@link #withRequestId} method.
 *
 * @see BotApiResponse
 */
@Value
@Builder
@JsonDeserialize(builder = BotApiResponseBodyBuilder.class)
class BotApiResponseBody {
    @JsonPOJOBuilder(withPrefix = "")
    public static class BotApiResponseBodyBuilder {
        // filled by lombok.
    }

    String message;
    @Default
    List<String> details = emptyList();

    BotApiResponse withRequestId(final String requestId) {
        return new BotApiResponse(requestId, message, details);
    }
}
