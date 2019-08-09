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
import com.linecorp.bot.model.message.flex.unit.FlexGravity;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import com.linecorp.bot.model.message.flex.unit.FlexOffsetSize;
import com.linecorp.bot.model.message.flex.unit.FlexPosition;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonTypeName("button")
@JsonInclude(Include.NON_NULL)
public class Button implements FlexComponent {

    public enum ButtonStyle {
        @JsonProperty("primary")
        PRIMARY,
        @JsonProperty("secondary")
        SECONDARY,
        @JsonProperty("link")
        LINK,
    }

    public enum ButtonHeight {
        @JsonProperty("md")
        MEDIUM,
        @JsonProperty("sm")
        SMALL,
    }

    private Integer flex;

    private String color;

    private ButtonStyle style;

    private Action action;

    private FlexGravity gravity;

    private FlexMarginSize margin;

    private FlexPosition position;

    private String offsetTop;

    private String offsetBottom;

    private String offsetStart;

    private String offsetEnd;

    private ButtonHeight height;

    @JsonCreator
    public Button(
            @JsonProperty("flex") Integer flex,
            @JsonProperty("color") String color,
            @JsonProperty("style") ButtonStyle style,
            @JsonProperty("action") Action action,
            @JsonProperty("gravity") FlexGravity gravity,
            @JsonProperty("margin") FlexMarginSize margin,
            @JsonProperty("position") FlexPosition position,
            @JsonProperty("offsetTop") String offsetTop,
            @JsonProperty("offsetBottom") String offsetBottom,
            @JsonProperty("offsetStart") String offsetStart,
            @JsonProperty("offsetEnd") String offsetEnd,
            @JsonProperty("height") ButtonHeight height) {
        this.flex = flex;
        this.color = color;
        this.style = style;
        this.action = action;
        this.gravity = gravity;
        this.margin = margin;
        this.position = position;
        this.offsetTop = offsetTop;
        this.offsetBottom = offsetBottom;
        this.offsetStart = offsetStart;
        this.offsetEnd = offsetEnd;
        this.height = height;
    }

    public static class ButtonBuilder {

        public ButtonBuilder offsetTop(FlexOffsetSize offset) {
            offsetTop = offset.getPropertyValue();
            return this;
        }

        public ButtonBuilder offsetTop(String offset) {
            offsetTop = offset;
            return this;
        }

        public ButtonBuilder offsetBottom(FlexOffsetSize offset) {
            offsetBottom = offset.getPropertyValue();
            return this;
        }

        public ButtonBuilder offsetBottom(String offset) {
            offsetBottom = offset;
            return this;
        }

        public ButtonBuilder offsetStart(FlexOffsetSize offset) {
            offsetStart = offset.getPropertyValue();
            return this;
        }

        public ButtonBuilder offsetStart(String offset) {
            offsetStart = offset;
            return this;
        }

        public ButtonBuilder offsetEnd(FlexOffsetSize offset) {
            offsetEnd = offset.getPropertyValue();
            return this;
        }

        public ButtonBuilder offsetEnd(String offset) {
            offsetEnd = offset;
            return this;
        }

    }
}
