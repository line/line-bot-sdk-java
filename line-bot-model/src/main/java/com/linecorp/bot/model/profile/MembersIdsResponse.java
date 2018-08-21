/*
 * Copyright 2018 LINE Corporation
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

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

/**
 * Response body of MembersIdsResponse.
 *
 * @see <a href="https://developers.line.me/en/reference/messaging-api/#get-group-member-user-ids">//developers.line.me/en/reference/messaging-api/#get-group-member-user-ids</a>
 * @see <a href="https://developers.line.me/en/reference/messaging-api/#get-room-member-user-ids">//developers.line.me/en/reference/messaging-api/#get-room-member-user-ids</a>
 */
@Value
public class MembersIdsResponse {
    /**
     * List of user IDs of the members in the group or room.
     *
     * <p>Max: 100 user IDs
     */
    List<String> memberIds;

    String next;

    @JsonCreator
    public MembersIdsResponse(
            @JsonProperty("memberIds") final List<String> memberIds,
            @JsonProperty("next") final String next) {
        this.memberIds = requireNonNull(memberIds, "memberIds is null");
        this.next = next;
    }

    /**
     * Parameter to get continue member ids in next API call. a.k.a continuationToken
     *
     * <p>Returned if and only if there are more user IDs remaining.
     */
    public Optional<String> getNext() {
        return Optional.ofNullable(next);
    }
}
