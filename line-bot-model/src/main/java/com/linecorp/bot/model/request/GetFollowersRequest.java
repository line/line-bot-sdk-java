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

package com.linecorp.bot.model.request;

import javax.annotation.Nullable;

import com.linecorp.bot.model.response.GetFollowersResponse;

import lombok.Builder;
import lombok.Value;

/**
 * A request object to retrieve followers. If you want to retrieve your followers from the beginning, you need
 * to call getFollowers as follows.
 * <pre>
 * {@code
 * final LineMessagingClient client = ...;
 * final GetFollowersResponse response = client.getFollowers(GetFollowersRequest.builder().build()).join();
 * }</pre>
 * Or if you want to retrieve all followers, you need to call getFollowers API as follows.
 * <pre>
 * {@code
 * final LineMessagingClient client = ...;
 * GetFollowersRequest request = GetFollowersRequest.builder().build();
 * while (true) {
 *   final GetFollowersResponse response = client.getFollowers(request).join();
 *   final List<String> userIds = response.getUserIds();
 *   if (response.getNext() == null) { // No remaining user IDs anymore.
 *     break;
 *   }
 *   // You have remaining user IDs. You can retrieve rest of them using following request object.
 *   request = GetFollowersRequest .fromResponse(response).build();
 *  }
 * }</pre>
 */
@Value
@Builder
public class GetFollowersRequest {
    /**
     * Value of the continuation token found in the next property of the JSON object returned in the response.
     * Include this parameter to get the next array of user IDs.
     */
    @Nullable
    String next;

    /**
     * The maximum number of user IDs to retrieve in a single request.
     * The default value is 300. If you specify a value that exceeds the maximum, the maximum value will be set.
     * Max value: 1000
     */
    @Nullable
    Integer limit;

    public static GetFollowersRequestBuilder fromResponse(GetFollowersResponse response) {
        return builder().next(response.getNext());
    }

    public static class GetFollowersRequestBuilder {
        // Filled by lombok.
    }
}
