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

package com.linecorp.bot.model.message.flex.container;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.FlexComponent;
import com.linecorp.bot.model.message.flex.unit.FlexDirection;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonTypeName("bubble")
@JsonInclude(Include.NON_NULL)
public class Bubble implements FlexContainer {

    public enum BubbleSize {
        @JsonProperty("nano")
        NANO,
        @JsonProperty("micro")
        MICRO,
        @JsonProperty("kilo")
        KILO,
        @JsonProperty("mega")
        MEGA,
        @JsonProperty("giga")
        GIGA
    }

    private final FlexDirection direction;

    private final BubbleStyles styles;

    private final Box header;

    private final FlexComponent hero;

    private final Box body;

    private final Box footer;

    private final BubbleSize size;

    private final Action action;

    @JsonCreator
    public Bubble(
            @JsonProperty("direction") FlexDirection direction,
            @JsonProperty("styles") BubbleStyles styles,
            @JsonProperty("header") Box header,
            @JsonProperty("hero") FlexComponent hero,
            @JsonProperty("body") Box body,
            @JsonProperty("footer") Box footer,
            @JsonProperty("size") BubbleSize size,
            @JsonProperty("action") Action action) {
        this.direction = direction;
        this.styles = styles;
        this.header = header;
        this.hero = hero;
        this.body = body;
        this.footer = footer;
        this.size = size;
        this.action = action;
    }

}
