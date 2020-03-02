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

package com.linecorp.bot.model.manageaudience.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.manageaudience.response.CreateClickBasedAudienceGroupResponse.CreateClickBasedAudienceGroupResponseBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = CreateClickBasedAudienceGroupResponseBuilder.class)
public class CreateClickBasedAudienceGroupResponse {
    /**
     * The audience ID.
     */
    private final Long audienceGroupId;

    /**
     * `CLICK`
     */
    private final AudienceGroupType type;

    /**
     * The audience's name.
     */
    private final String description;

    /**
     * When the audience was created (in UNIX time).
     */
    private final Long created;

    /**
     * The request ID that was specified when the audience was created.
     */
    private final String requestId;

    /**
     * The URL that was specified when the audience was created.
     */
    private final String clickUrl;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CreateClickBasedAudienceGroupResponseBuilder {
        // Filled by lombok
    }
}
