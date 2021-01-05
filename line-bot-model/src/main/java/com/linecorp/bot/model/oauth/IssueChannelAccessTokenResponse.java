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

package com.linecorp.bot.model.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.oauth.IssueChannelAccessTokenResponse.IssueChannelAccessTokenResponseBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = IssueChannelAccessTokenResponseBuilder.class)
public class IssueChannelAccessTokenResponse {
    /**
     * A short-lived channel access token. Valid for 30 days. Note: Channel access tokens cannot be refreshed.
     */
    @JsonProperty("access_token")
    String accessToken;

    /**
     * Time until channel access token expires in seconds from time the token is issued.
     */
    @JsonProperty("expires_in")
    int expiresInSecs;

    /**
     * A token type.
     */
    @Builder.Default
    @JsonProperty("token_type")
    String tokenType = "Bearer";

    /**
     * Unique key ID for identifying the channel access token.
     */
    @JsonProperty("key_id")
    String keyId;

    @JsonPOJOBuilder(withPrefix = "")
    public static class IssueChannelAccessTokenResponseBuilder {
        // Filled by lombok.
    }
}
