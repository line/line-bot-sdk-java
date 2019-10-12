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

package com.linecorp.bot.model.demographic;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.linecorp.bot.model.demographic.FriendDemographicResponse.FriendDemographicResponseBuilder;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
@JsonDeserialize(builder = FriendDemographicResponseBuilder.class)
public class FriendDemographicResponse {
    /**
     * true if friend demographic information is available.
     */
    boolean available;

    /**
     * 	Percentage per gender
     */
    List<GenderPercentage> genders;

    /**
     * Percentage per age group
     */
    List<AgePercentage> ages;

    /**
     * Percentage per area
     */
    List<AreaPercentage> areas;

    /**
     * Percentage by OS
     */
    List<AppTypePercentage> appTypes;

    /**
     * Percentage per friendship duration
     */
    List<SubscriptionPeriodPercentage> subscriptionPeriods;

    // The elements of each Array are included in the response only if the value of available is true.

    @JsonPOJOBuilder(withPrefix = "")
    public static class FriendDemographicResponseBuilder {
        // Filled by lombok
    }
}
