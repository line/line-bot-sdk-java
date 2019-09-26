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

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.response.GetFriendsDemographicsResponse.GetFriendsDemographicsResponseBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = GetFriendsDemographicsResponseBuilder.class)
public class GetFriendsDemographicsResponse {

    @Value
    @Builder
    @JsonDeserialize(builder = GenderWisePercentage.GenderWisePercentageBuilder.class)
    static class GenderWisePercentage {
        String gender;
        Double percentage;

        @JsonPOJOBuilder(withPrefix = "")
        public static class GenderWisePercentageBuilder{
            // Filled by lombok
        }
    }

    @Value
    @Builder
    @JsonDeserialize(builder = AgeWisePercentage.AgeWisePercentageBuilder.class)
    static class AgeWisePercentage {
        String age;
        Double percentage;

        @JsonPOJOBuilder(withPrefix = "")
        public static class  AgeWisePercentageBuilder{
            // Filled by lombok
        }
    }

    @Value
    @Builder
    @JsonDeserialize(builder = AreaWisePercentage.AreaWisePercentageBuilder.class)
    static class AreaWisePercentage {
        String area;
        Double percentage;

        @JsonPOJOBuilder(withPrefix = "")
        public static class  AreaWisePercentageBuilder{
            // Filled by lombok
        }
    }

    @Value
    @Builder
    @JsonDeserialize(builder = OSWisePercentage.OSWisePercentageBuilder.class)
    static class OSWisePercentage {
        String appType;
        Double percentage;

        @JsonPOJOBuilder(withPrefix = "")
        public static class  OSWisePercentageBuilder{
            // Filled by lombok
        }
    }

    @Value
    @Builder
    @JsonDeserialize(builder = SubscriptionWisePercentage.SubscriptionWisePercentageBuilder.class)
    static class SubscriptionWisePercentage {
        String subscriptionPeriod;
        Double percentage;

        @JsonPOJOBuilder(withPrefix = "")
        public static class  SubscriptionWisePercentageBuilder{
            // Filled by lombok
        }
    }

    /**
     * true if friend demographic information is available.
     */
    Boolean available;

    /**
     * Percentage per gender.
     */
    List<GenderWisePercentage> genders = new ArrayList<>();

    /**
     * Percentage per age group.
     */
    List<AgeWisePercentage> ages = new ArrayList<>();

    /**
     * Percentage per area.
     */
    List<AreaWisePercentage> areas = new ArrayList<>();

    /**
     * Percentage by OS.
     */
    List<OSWisePercentage> appTypes = new ArrayList<>();

    /**
     * Percentage per friendship duration.
     */
    List<SubscriptionWisePercentage> subscriptionPeriods = new ArrayList<>();

    @JsonPOJOBuilder(withPrefix = "")
    public static class  GetFriendsDemographicsResponseBuilder{
        // Filled by lombok
    }
}
