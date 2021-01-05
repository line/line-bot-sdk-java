/*
 * Copyright 2021 LINE Corporation
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

package com.linecorp.bot.model.oauth;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.oauth.ChannelAccessTokenKeyIdsResponse.ChannelAccessTokenKeyIdsResponseBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = ChannelAccessTokenKeyIdsResponseBuilder.class)
public class ChannelAccessTokenKeyIdsResponse {
    /**
     * Array of channel access token key IDs.
     */
    List<String> kids;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ChannelAccessTokenKeyIdsResponseBuilder {
        // Filled by lombok.
    }
}
