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

package com.linecorp.bot.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.response.GetNumberOfFollowersResponse.GetNumberOfFollowersResponseBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = GetNumberOfFollowersResponseBuilder.class)
public class GetNumberOfFollowersResponse {
    /**
     * A status of current calculation.
     */
    public enum Status {
        /**
         * Calculation has finished; the numbers are up-to-date.
         */
        @JsonProperty("ready")
        READY,
        /**
         * We haven't finished calculating the number of sent messages for the specified date. Calculation
         * usually takes about a day. Please try again later.
         */
        @JsonProperty("unready")
        UNREADY,
        /**
         * The specified date is earlier than the date on which we first started calculating followers
         * (November 1, 2016).
         */
        @JsonProperty("out_of_service")
        OUT_OF_SERVICE
    }

    /**
     * Calculation status.
     */
    Status status;

    /**
     * The number of times, as of the specified date, that a user added this LINE official account as a friend
     * for the first time. The number doesn't decrease even if a user later blocks the account or when they
     * delete their LINE account.
     */
    Long followers;

    /**
     * The number of users, as of the specified date, that the official account can reach through targeted
     * messages based on gender, age, and/or region. This number only includes users who are active on LINE or
     * LINE services and whose demographics have a high level of certainty.
     */
    Long targetedReaches;

    /**
     * The number of users blocking the account as of the specified date. The number decreases when a user
     * unblocks the account.
     */
    Long blocks;

    @JsonPOJOBuilder(withPrefix = "")
    public static class GetNumberOfFollowersResponseBuilder {
        // Filled by lombok
    }
}
