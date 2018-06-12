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

import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.message.flex.unit.FxAlign;
import com.linecorp.bot.model.message.flex.unit.FxFontSize;
import com.linecorp.bot.model.message.flex.unit.FxGravity;
import com.linecorp.bot.model.message.flex.unit.FxMarginSize;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonTypeName("text")
@JsonInclude(Include.NON_NULL)
public class Text implements FlexComponent {

    public enum TextWeight {
        @JsonProperty("regular")
        Regular,
        @JsonProperty("bold")
        Bold,
    }

    private final Integer flex;

    private final String text;

    private final FxFontSize size;

    private final FxAlign align;

    private final FxGravity gravity;

    private final String color;

    private final TextWeight weight;

    private final Boolean wrap;

    private final FxMarginSize margin;

    private final Action action;

    @JsonCreator
    public Text(
            @JsonProperty("flex") Integer flex,
            @JsonProperty("text") String text,
            @JsonProperty("size") FxFontSize size,
            @JsonProperty("align") FxAlign align,
            @JsonProperty("gravity") FxGravity gravity,
            @JsonProperty("color") String color,
            @JsonProperty("weight") TextWeight weight,
            @JsonProperty("wrap") Boolean wrap,
            @JsonProperty("margin") FxMarginSize margin,
            @JsonProperty("action") Action action) {
        this.flex = flex;
        this.text = text;
        this.size = size;
        this.align = align;
        this.gravity = gravity;
        this.color = color;
        this.weight = weight;
        this.wrap = wrap;
        this.margin = margin;
        this.action = action;
    }
}
