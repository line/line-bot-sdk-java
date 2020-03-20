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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.narrowcast.filter.AppTypeDemographicFilter.AppTypeDemographicFilterBuilder;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Value
@Builder
@JsonTypeName("appType")
@JsonDeserialize(builder = AppTypeDemographicFilterBuilder.class)
public class AppTypeDemographicFilter implements DemographicFilter {
    private static final String type = "appType";

    List<AppType> oneOf;

    @JsonPOJOBuilder(withPrefix = "")
    public static class AppTypeDemographicFilterBuilder {
        // Filled by lombok
    }

    public enum AppType {
        @JsonProperty("ios")
        IOS("ios"),
        @JsonProperty("android")
        ANDROID("android");

        @Getter
        private final String appType;

        AppType(String appType) {
            this.appType = appType;
        }
    }
}
