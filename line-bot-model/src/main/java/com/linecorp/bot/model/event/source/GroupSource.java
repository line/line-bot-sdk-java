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

package com.linecorp.bot.model.event.source;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Value;

@Value
@JsonTypeName("group")
public class GroupSource implements Source {
    private final String groupId;
    private final String userId;

    /**
     * Create new instance.
     * @param groupId group ID
     * @param userId user id may be null
     */
    @JsonCreator
    public GroupSource(
            @JsonProperty("groupId") String groupId,
            @JsonProperty("userId") String userId
    ) {
        this.groupId = groupId;
        this.userId = userId;
    }

    @Override
    public String getSenderId() {
        return groupId;
    }
}
