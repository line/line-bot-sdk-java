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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.message.flex.unit.FlexAdjustMode;
import com.linecorp.bot.model.message.flex.unit.FlexAlign;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexGravity;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import com.linecorp.bot.model.message.flex.unit.FlexOffsetSize;
import com.linecorp.bot.model.message.flex.unit.FlexPosition;

import lombok.Builder;
import lombok.Value;

@JsonTypeName("text")
@JsonInclude(Include.NON_NULL)
@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = Text.TextBuilder.class)
public class Text implements FlexComponent {
    public enum TextWeight {
        @JsonProperty("regular")
        REGULAR,
        @JsonProperty("bold")
        BOLD,
    }

    public enum TextStyle {
        @JsonProperty("normal")
        NORMAL,
        @JsonProperty("italic")
        ITALIC,
    }

    public enum TextDecoration {
        @JsonProperty("none")
        NONE,
        @JsonProperty("underline")
        UNDERLINE,
        @JsonProperty("line-through")
        LINE_THROUGH,
    }

    Integer flex;

    String text;

    String size;

    FlexAlign align;

    FlexGravity gravity;

    String color;

    TextWeight weight;

    TextStyle style;

    TextDecoration decoration;

    Boolean wrap;

    String margin;

    FlexPosition position;

    String offsetTop;

    String offsetBottom;

    String offsetStart;

    String offsetEnd;

    Action action;

    Integer maxLines;

    List<Span> contents;

    FlexAdjustMode adjustMode;

    @JsonPOJOBuilder(withPrefix = "")
    public static class TextBuilder {
        // Providing builder instead of public constructor. Class body is filled by lombok.

        public TextBuilder size(FlexFontSize size) {
            this.size = size.getPropertyValue();
            return this;
        }

        public TextBuilder size(String size) {
            this.size = size;
            return this;
        }

        public TextBuilder margin(FlexMarginSize margin) {
            this.margin = margin.getPropertyValue();
            return this;
        }

        public TextBuilder margin(String margin) {
            this.margin = margin;
            return this;
        }

        public TextBuilder offsetTop(FlexOffsetSize offset) {
            offsetTop = offset.getPropertyValue();
            return this;
        }

        public TextBuilder offsetTop(String offset) {
            offsetTop = offset;
            return this;
        }

        public TextBuilder offsetBottom(FlexOffsetSize offset) {
            offsetBottom = offset.getPropertyValue();
            return this;
        }

        public TextBuilder offsetBottom(String offset) {
            offsetBottom = offset;
            return this;
        }

        public TextBuilder offsetStart(FlexOffsetSize offset) {
            offsetStart = offset.getPropertyValue();
            return this;
        }

        public TextBuilder offsetStart(String offset) {
            offsetStart = offset;
            return this;
        }

        public TextBuilder offsetEnd(FlexOffsetSize offset) {
            offsetEnd = offset.getPropertyValue();
            return this;
        }

        public TextBuilder offsetEnd(String offset) {
            offsetEnd = offset;
            return this;
        }
    }
}
