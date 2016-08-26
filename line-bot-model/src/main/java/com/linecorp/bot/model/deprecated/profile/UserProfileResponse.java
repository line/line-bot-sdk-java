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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;

/**
 * Response object to get user's profile information
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
@Getter
public class UserProfileResponse {
    /**
     * Container of profile information for 0 friends and above.
     */
    private final List<UserProfileResponseContact> contacts;

    /**
     * No. of results in this response.
     */
    private final Long count;

    /**
     * Total no. of results that match the specified conditions.
     */
    private final Long total;

    /**
     * Starting index specified in the request.
     */
    private final Long start;

    /**
     * Display parameter value specified in the request.
     */
    private final Long display;

    public UserProfileResponse(
            @JsonProperty("contacts") List<UserProfileResponseContact> contacts,
            @JsonProperty("count") Long count,
            @JsonProperty("total") Long total,
            @JsonProperty("start") Long start,
            @JsonProperty("display") Long display) {
        this.contacts = contacts;
        this.count = count;
        this.total = total;
        this.start = start;
        this.display = display;
    }
}
