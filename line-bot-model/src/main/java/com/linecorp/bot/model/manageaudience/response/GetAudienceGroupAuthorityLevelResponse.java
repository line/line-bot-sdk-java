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

import com.linecorp.bot.model.manageaudience.AudienceGroupAuthorityLevel;
import com.linecorp.bot.model.manageaudience.response.GetAudienceGroupAuthorityLevelResponse.GetAudienceGroupAuthorityLevelResponseBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = GetAudienceGroupAuthorityLevelResponseBuilder.class)
public class GetAudienceGroupAuthorityLevelResponse {
    /**
     * Authority level
     */
    private AudienceGroupAuthorityLevel authorityLevel;

    @JsonPOJOBuilder(withPrefix = "")
    public static class GetAudienceGroupAuthorityLevelResponseBuilder {
        // Filled by lombok
    }
}
