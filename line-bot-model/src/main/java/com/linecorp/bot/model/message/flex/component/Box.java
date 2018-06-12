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

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.message.flex.unit.FxLayout;
import com.linecorp.bot.model.message.flex.unit.FxMarginSize;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonTypeName("box")
@JsonInclude(Include.NON_NULL)
public class Box implements FlexComponent {

    private final FxLayout layout;

    private final Integer flex;

    private final List<FlexComponent> contents;

    private final FxMarginSize spacing;

    private final FxMarginSize margin;

    private final Action action;

    @JsonCreator
    public Box(
            @JsonProperty("layout") FxLayout layout,
            @JsonProperty("flex") Integer flex,
            @JsonProperty("contents") List<FlexComponent> contents,
            @JsonProperty("spacing") FxMarginSize spacing,
            @JsonProperty("margin") FxMarginSize margin,
            @JsonProperty("action") Action action) {
        this.layout = layout;
        this.flex = flex;
        this.contents = contents != null ? contents : Collections.emptyList();
        this.spacing = spacing;
        this.margin = margin;
        this.action = action;
    }
}
