/*
 * Copyright 2018 LINE Corporation
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

package com.linecorp.bot.model.message.flex.component;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import com.linecorp.bot.model.message.flex.unit.FxFontSize;
import com.linecorp.bot.model.message.flex.unit.FxMarginSize;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonTypeName("icon")
@JsonInclude(Include.NON_NULL)
public class Icon implements FlexComponent {

    public enum IconAspectRatio {
        @JsonProperty("1:1")
        R1to1,
        @JsonProperty("2:1")
        R2to1,
        @JsonProperty("3:1")
        R3to1,
    }

    private final String url;

    private final FxFontSize size;

    private final IconAspectRatio aspectRatio;

    private final FxMarginSize margin;

    @JsonCreator
    public Icon(
            @JsonProperty("url") String url,
            @JsonProperty("size") FxFontSize size,
            @JsonProperty("aspectRatio") IconAspectRatio aspectRatio,
            @JsonProperty("margin") FxMarginSize margin) {
        this.url = url;
        this.size = size;
        this.aspectRatio = aspectRatio;
        this.margin = margin;
    }
}
