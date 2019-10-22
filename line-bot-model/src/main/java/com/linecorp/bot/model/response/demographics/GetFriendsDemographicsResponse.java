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

package com.linecorp.bot.model.response.demographics;

import static java.util.Collections.emptyList;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = GetFriendsDemographicsResponse.GetFriendsDemographicsResponseBuilder.class)
public class GetFriendsDemographicsResponse {

    /**
     * {@literal true} if friend demographic information is available.
     */
    boolean available;

    /**
     * Percentage per gender.
     */
    @Default
    List<GenderTile> genders = emptyList();

    /**
     * Percentage per age group.
     */
    @Default
    List<AgeTile> ages = emptyList();

    /**
     * Percentage per area.
     */
    @Default
    List<AreaTile> areas = emptyList();

    /**
     * Percentage by OS.
     */
    @Default
    List<AppTypeTile> appTypes = emptyList();

    /**
     * Percentage per friendship duration.
     */
    @Default
    List<SubscriptionPeriodTile> subscriptionPeriods = emptyList();

    @JsonPOJOBuilder(withPrefix = "")
    public static class GetFriendsDemographicsResponseBuilder {
        // Filled by lombok
    }
}
