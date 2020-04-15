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

import java.net.URI;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;

/**
 * Response object for 'Get Profile' API.
 */
@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = UserProfileResponse.UserProfileResponseBuilder.class)
public class UserProfileResponse {
    /**
     * Display name.
     */
    String displayName;

    /**
     * User ID.
     */
    String userId;

    /**
     * Image URL.
     */
    URI pictureUrl;

    /**
     * Status message.
     */
    String statusMessage;

    /**
     * User's language.
     */
    String language;

    @JsonPOJOBuilder(withPrefix = "")
    public static class UserProfileResponseBuilder {
        // Providing builder instead of public constructor. Class body is filled by lombok.
    }
}
