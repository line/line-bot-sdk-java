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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.message.flex.unit.FlexAlign;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexGravity;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import com.linecorp.bot.model.message.flex.unit.FlexOffsetSize;
import com.linecorp.bot.model.message.flex.unit.FlexPosition;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonTypeName("text")
@JsonInclude(Include.NON_NULL)
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

    private final Integer flex;

    private final String text;

    private final FlexFontSize size;

    private final FlexAlign align;

    private final FlexGravity gravity;

    private final String color;

    private final TextWeight weight;

    private final TextStyle style;

    private final TextDecoration decoration;

    private final Boolean wrap;

    private final FlexMarginSize margin;

    private final FlexPosition position;

    private final String offsetTop;

    private final String offsetBottom;

    private final String offsetStart;

    private final String offsetEnd;

    private final Action action;

    private final Integer maxLines;

    private final List<Span> contents;

    @JsonCreator
    public Text(
            @JsonProperty("flex") Integer flex,
            @JsonProperty("text") String text,
            @JsonProperty("size") FlexFontSize size,
            @JsonProperty("align") FlexAlign align,
            @JsonProperty("gravity") FlexGravity gravity,
            @JsonProperty("color") String color,
            @JsonProperty("weight") TextWeight weight,
            @JsonProperty("style") TextStyle style,
            @JsonProperty("decoration") TextDecoration decoration,
            @JsonProperty("wrap") Boolean wrap,
            @JsonProperty("margin") FlexMarginSize margin,
            @JsonProperty("position") FlexPosition position,
            @JsonProperty("offsetTop") String offsetTop,
            @JsonProperty("offsetBottom") String offsetBottom,
            @JsonProperty("offsetStart") String offsetStart,
            @JsonProperty("offsetEnd") String offsetEnd,
            @JsonProperty("action") Action action,
            @JsonProperty("maxLines") Integer maxLines,
            @JsonProperty("contents") List<Span> contents) {
        this.flex = flex;
        this.text = text;
        this.size = size;
        this.align = align;
        this.gravity = gravity;
        this.color = color;
        this.weight = weight;
        this.style = style;
        this.decoration = decoration;
        this.wrap = wrap;
        this.margin = margin;
        this.position = position;
        this.offsetTop = offsetTop;
        this.offsetBottom = offsetBottom;
        this.offsetStart = offsetStart;
        this.offsetEnd = offsetEnd;
        this.action = action;
        this.maxLines = maxLines;
        this.contents = contents;
    }

    public static class TextBuilder {

        public TextBuilder contents(List<Span> contents) {
            this.contents = new ArrayList<>(contents);
            return this;
        }

        public TextBuilder contents(Span... contents) {
            this.contents = Arrays.asList(contents);
            return this;
        }

        public TextBuilder content(Span content) {
            return contents(content);
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
