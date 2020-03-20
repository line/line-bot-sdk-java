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

package com.linecorp.bot.model.manageaudience.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.manageaudience.request.CreateImpBasedAudienceGroupRequest.CreateImpBasedAudienceGroupRequestBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = CreateImpBasedAudienceGroupRequestBuilder.class)
public class CreateImpBasedAudienceGroupRequest {
    /**
     * The audience's name. Audience names must be unique. Note that comparisons are case-insensitive, so the
     * names AUDIENCE and audience are considered identical.
     *
     * <p>Max character limit: 120
     */
    String description;

    /**
     * The request ID of a broadcast or narrowcast message sent in the past 60 days. Each Messaging API request
     * has a request ID. Find it in the response headers.
     */
    String requestId;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CreateImpBasedAudienceGroupRequestBuilder {
        // Filled by lombok
    }
}
