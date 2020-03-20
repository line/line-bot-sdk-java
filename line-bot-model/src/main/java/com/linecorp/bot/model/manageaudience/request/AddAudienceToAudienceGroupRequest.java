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

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.manageaudience.request.AddAudienceToAudienceGroupRequest.AddAudienceToAudienceGroupRequestBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = AddAudienceToAudienceGroupRequestBuilder.class)
public class AddAudienceToAudienceGroupRequest {
    /**
     * The audience ID.
     */
    long audienceGroupId;

    /**
     * The description to register with the job (in jobs[].description).
     */
    String uploadDescription;

    /**
     * An array of up to 10,000 user IDs or IFAs.
     */
    List<Audience> audiences;

    @JsonPOJOBuilder(withPrefix = "")
    public static class AddAudienceToAudienceGroupRequestBuilder {
        // Filled by lombok
    }
}
