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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.message.flex.unit.FlexAdjustMode;
import com.linecorp.bot.model.message.flex.unit.FlexGravity;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import com.linecorp.bot.model.message.flex.unit.FlexOffsetSize;
import com.linecorp.bot.model.message.flex.unit.FlexPosition;

import lombok.Builder;
import lombok.Value;

@JsonTypeName("button")
@JsonInclude(Include.NON_NULL)
@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = Button.ButtonBuilder.class)
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

    Integer flex;

    String color;

    ButtonStyle style;

    Action action;

    FlexGravity gravity;

    String margin;

    FlexPosition position;

    String offsetTop;

    String offsetBottom;

    String offsetStart;

    String offsetEnd;

    ButtonHeight height;

    FlexAdjustMode adjustMode;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ButtonBuilder {
        // Providing builder instead of public constructor. Class body is filled by lombok.

        public ButtonBuilder margin(FlexMarginSize margin) {
            this.margin = margin.getPropertyValue();
            return this;
        }

        public ButtonBuilder margin(String margin) {
            this.margin = margin;
            return this;
        }

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
