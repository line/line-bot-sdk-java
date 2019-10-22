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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.response.demographics.AppTypeTile.AppTypeTileBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = AppTypeTileBuilder.class)
class AppTypeTile {
    /**
     * Operating System.
     *
     * <p>Possible values: {@link AppType#IOS IOS}, {@link AppType#ANDROID ANDROID},
     * or {@link AppType#OTHERS OTHERS}.
     */
    AppType appType;

    /**
     * Percentage. Possible values: [0.0,100.0] e.g. {@code 0}, {@code 2.9}, {@code 37.6}.
     */
    double percentage;

    public enum AppType {
        @JsonProperty("ios")
        IOS,
        @JsonProperty("android")
        ANDROID,
        @JsonProperty("others")
        OTHERS;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class AppTypeTileBuilder {
        // Filled by lombok
    }
}
