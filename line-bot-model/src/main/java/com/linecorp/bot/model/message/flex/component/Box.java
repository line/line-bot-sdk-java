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
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.message.flex.unit.FlexBorderWidthSize;
import com.linecorp.bot.model.message.flex.unit.FlexCornerRadiusSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import com.linecorp.bot.model.message.flex.unit.FlexOffsetSize;
import com.linecorp.bot.model.message.flex.unit.FlexPaddingSize;
import com.linecorp.bot.model.message.flex.unit.FlexPosition;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonTypeName("box")
@JsonInclude(Include.NON_NULL)
public class Box implements FlexComponent {

    private final FlexLayout layout;

    private final Integer flex;

    private final List<FlexComponent> contents;

    private final FlexMarginSize spacing;

    private final FlexMarginSize margin;

    private final FlexPosition position;

    private final String offsetTop;

    private final String offsetBottom;

    private final String offsetStart;

    private final String offsetEnd;

    private final String backgroundColor;

    private final String borderColor;

    private final String borderWidth;

    private final String cornerRadius;

    private final String width;

    private final String height;

    private final String paddingAll;

    private final String paddingTop;

    private final String paddingBottom;

    private final String paddingStart;

    private final String paddingEnd;

    private final Action action;

    @JsonCreator
    public Box(
            @JsonProperty("layout") FlexLayout layout,
            @JsonProperty("flex") Integer flex,
            @JsonProperty("contents") List<FlexComponent> contents,
            @JsonProperty("spacing") FlexMarginSize spacing,
            @JsonProperty("margin") FlexMarginSize margin,
            @JsonProperty("position") FlexPosition position,
            @JsonProperty("offsetTop") String offsetTop,
            @JsonProperty("offsetBottom") String offsetBottom,
            @JsonProperty("offsetStart") String offsetStart,
            @JsonProperty("offsetEnd") String offsetEnd,
            @JsonProperty("backgroundColor") String backgroundColor,
            @JsonProperty("borderColor") String borderColor,
            @JsonProperty("borderWidth") String borderWidth,
            @JsonProperty("cornerRadius") String cornerRadius,
            @JsonProperty("width") String width,
            @JsonProperty("height") String height,
            @JsonProperty("paddingAll") String paddingAll,
            @JsonProperty("paddingTop") String paddingTop,
            @JsonProperty("paddingBottom") String paddingBottom,
            @JsonProperty("paddingStart") String paddingStart,
            @JsonProperty("paddingEnd") String paddingEnd,
            @JsonProperty("action") Action action) {
        this.layout = layout;
        this.flex = flex;
        this.contents = contents != null ? contents : Collections.emptyList();
        this.spacing = spacing;
        this.margin = margin;
        this.position = position;
        this.offsetTop = offsetTop;
        this.offsetBottom = offsetBottom;
        this.offsetStart = offsetStart;
        this.offsetEnd = offsetEnd;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
        this.cornerRadius = cornerRadius;
        this.width = width;
        this.height = height;
        this.paddingAll = paddingAll;
        this.paddingTop = paddingTop;
        this.paddingBottom = paddingBottom;
        this.paddingStart = paddingStart;
        this.paddingEnd = paddingEnd;
        this.action = action;
    }

    public static class BoxBuilder {

        public BoxBuilder contents(List<FlexComponent> contents) {
            this.contents = new ArrayList<>(contents);
            return this;
        }

        public BoxBuilder contents(FlexComponent... contents) {
            this.contents = Arrays.asList(contents);
            return this;
        }

        public BoxBuilder content(FlexComponent content) {
            return contents(content);
        }

        public BoxBuilder paddingAll(FlexPaddingSize padding) {
            paddingAll = padding.getPropertyValue();
            return this;
        }

        public BoxBuilder paddingAll(String padding) {
            paddingAll = padding;
            return this;
        }

        public BoxBuilder paddingTop(FlexPaddingSize padding) {
            paddingTop = padding.getPropertyValue();
            return this;
        }

        public BoxBuilder paddingTop(String padding) {
            paddingTop = padding;
            return this;
        }

        public BoxBuilder paddingBottom(FlexPaddingSize padding) {
            paddingBottom = padding.getPropertyValue();
            return this;
        }

        public BoxBuilder paddingBottom(String padding) {
            paddingBottom = padding;
            return this;
        }

        public BoxBuilder paddingStart(FlexPaddingSize padding) {
            paddingStart = padding.getPropertyValue();
            return this;
        }

        public BoxBuilder paddingStart(String padding) {
            paddingStart = padding;
            return this;
        }

        public BoxBuilder paddingEnd(FlexPaddingSize padding) {
            paddingEnd = padding.getPropertyValue();
            return this;
        }

        public BoxBuilder paddingEnd(String padding) {
            paddingEnd = padding;
            return this;
        }

        public BoxBuilder offsetTop(FlexOffsetSize padding) {
            offsetTop = padding.getPropertyValue();
            return this;
        }

        public BoxBuilder offsetTop(String offset) {
            offsetTop = offset;
            return this;
        }

        public BoxBuilder offsetBottom(FlexOffsetSize offset) {
            offsetBottom = offset.getPropertyValue();
            return this;
        }

        public BoxBuilder offsetBottom(String offset) {
            offsetBottom = offset;
            return this;
        }

        public BoxBuilder offsetStart(FlexOffsetSize offset) {
            offsetStart = offset.getPropertyValue();
            return this;
        }

        public BoxBuilder offsetStart(String offset) {
            offsetStart = offset;
            return this;
        }

        public BoxBuilder offsetEnd(FlexOffsetSize offset) {
            offsetEnd = offset.getPropertyValue();
            return this;
        }

        public BoxBuilder offsetEnd(String offset) {
            offsetEnd = offset;
            return this;
        }

        public BoxBuilder borderWidth(FlexBorderWidthSize width) {
            borderWidth = width.getPropertyValue();
            return this;
        }

        public BoxBuilder borderWidth(String width) {
            borderWidth = width;
            return this;
        }

        public BoxBuilder cornerRadius(FlexCornerRadiusSize radius) {
            cornerRadius = radius.getPropertyValue();
            return this;
        }

        public BoxBuilder cornerRadius(String radius) {
            cornerRadius = radius;
            return this;
        }
    }
}
