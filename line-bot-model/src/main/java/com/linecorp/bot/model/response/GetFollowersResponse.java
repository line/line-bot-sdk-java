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

package com.linecorp.bot.model.response;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.response.GetFollowersResponse.GetFollowersResponseBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = GetFollowersResponseBuilder.class)
public class GetFollowersResponse {
    /**
     * List of user IDs of users that have added the LINE Official Account as a friend. Only users of LINE for
     * iOS and LINE for Android are included in userIds. For more information, see
     * <a href="https://developers.line.biz/en/docs/messaging-api/user-consent/">Consent on getting user
     * profile information</a>. Max: 300 user IDs
     */
    List<String> userIds;

    /**
     * A continuation token to get the next array of user IDs. Returned only when there are remaining user IDs
     * that were not returned in userIds in the original request. The number of user IDs in the userIds element
     * does not have to reach 300 for the next property to be included in the response.
     */
    @Nullable
    String next;

    @JsonPOJOBuilder(withPrefix = "")
    public static class GetFollowersResponseBuilder {
        // Filled by lombok
    }
}
