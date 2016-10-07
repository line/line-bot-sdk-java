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

package com.linecorp.bot.model.profile;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

/**
 * Response object for 'Get Profile' API.
 */
@Value
public class UserProfileResponse {
    /**
     * Display name
     */
    private final String displayName;

    /**
     * User ID
     */
    private final String userId;

    /**
     * Image URL
     */
    private final String pictureUrl;

    /**
     * Status message
     */
    private final String statusMessage;

    public UserProfileResponse(
            @JsonProperty("displayName") String displayName,
            @JsonProperty("userId") String userId,
            @JsonProperty("pictureUrl") String pictureUrl,
            @JsonProperty("statusMessage") String statusMessage) {
        this.displayName = displayName;
        this.userId = userId;
        this.pictureUrl = pictureUrl;
        this.statusMessage = statusMessage;
    }
}
