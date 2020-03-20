/*
 * Copyright 2020 LINE Corporation
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
 *
 */

package com.linecorp.bot.model.narrowcast.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.narrowcast.filter.SubscriptionPeriodDemographicFilter.SubscriptionPeriodDemographicFilterBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonTypeName("subscriptionPeriod")
@JsonDeserialize(builder = SubscriptionPeriodDemographicFilterBuilder.class)
public class SubscriptionPeriodDemographicFilter implements DemographicFilter {
    private static final String type = "subscriptionPeriod";

    SubscriptionPeriod gte;
    SubscriptionPeriod lt;

    @JsonPOJOBuilder(withPrefix = "")
    public static class SubscriptionPeriodDemographicFilterBuilder {
        // Filled by lombok
    }

    public enum SubscriptionPeriod {
        @JsonProperty("day_7")
        DAY_7(7),
        @JsonProperty("day_30")
        DAY_30(30),
        @JsonProperty("day_90")
        DAY_90(90),
        @JsonProperty("day_180")
        DAY_180(180),
        @JsonProperty("day_365")
        DAY_365(365);

        private final int days;

        SubscriptionPeriod(int days) {
            this.days = days;
        }

        public int getDays() {
            return days;
        }
    }
}
