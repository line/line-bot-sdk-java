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

package com.linecorp.bot.model.message.flex.component;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import com.linecorp.bot.model.message.flex.component.Text.TextDecoration;
import com.linecorp.bot.model.message.flex.component.Text.TextStyle;
import com.linecorp.bot.model.message.flex.component.Text.TextWeight;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonTypeName("span")
@JsonInclude(Include.NON_NULL)
public class Span implements FlexComponent {

    private final String text;

    private final FlexFontSize size;

    private final String color;

    private final TextWeight weight;

    private final TextStyle style;

    private final TextDecoration decoration;

    @JsonCreator
    public Span(
            @JsonProperty("text") String text,
            @JsonProperty("size") FlexFontSize size,
            @JsonProperty("color") String color,
            @JsonProperty("weight") TextWeight weight,
            @JsonProperty("style") TextStyle style,
            @JsonProperty("decoration") TextDecoration decoration) {
        this.text = text;
        this.size = size;
        this.color = color;
        this.weight = weight;
        this.style = style;
        this.decoration = decoration;
    }
}
