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

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Getter;
import lombok.Value;

@Value
@JsonTypeName("appType")
public class AppTypeDemographicFilter implements DemographicFilter {
    private static final String type = "appType";

    private final List<AppType> oneOf;

    @JsonCreator
    public AppTypeDemographicFilter(List<AppType> oneOf) {
        this.oneOf = oneOf;
    }

    public AppTypeDemographicFilter(AppType appType) {
        this(Collections.singletonList(appType));
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
