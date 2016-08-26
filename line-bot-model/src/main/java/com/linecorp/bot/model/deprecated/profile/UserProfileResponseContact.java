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

package com.linecorp.bot.model.deprecated.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;

@ToString

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class UserProfileResponseContact {
    /**
     * User nickname.
     */
    private final String displayName;

    /**
     * User ID.
     */
    private final String mid;

    /**
     * URL of user's profile photo.
     */
    private final String pictureUrl;

    /**
     * User's status message. Not included in the object if the user has not created a status message.
     */
    private final String statusMessage;

    public UserProfileResponseContact(
            @JsonProperty("displayName") String displayName,
            @JsonProperty("mid") String mid,
            @JsonProperty("pictureUrl") String pictureUrl,
            @JsonProperty("statusMessage") String statusMessage
    ) {
        this.displayName = displayName;
        this.mid = mid;
        this.pictureUrl = pictureUrl;
        this.statusMessage = statusMessage;
    }
}
