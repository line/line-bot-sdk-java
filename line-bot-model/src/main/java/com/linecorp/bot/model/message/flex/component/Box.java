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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.message.flex.unit.FlexBorderWidthSize;
import com.linecorp.bot.model.message.flex.unit.FlexCornerRadiusSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import com.linecorp.bot.model.message.flex.unit.FlexOffsetSize;
import com.linecorp.bot.model.message.flex.unit.FlexPaddingSize;
import com.linecorp.bot.model.message.flex.unit.FlexPosition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@JsonTypeName("box")
@JsonInclude(Include.NON_NULL)
@Value
@Builder(toBuilder = true)
@AllArgsConstructor(onConstructor = @__(@Deprecated)) // TODO: Remove next release. Use builder() instead.
@JsonDeserialize(builder = Box.BoxBuilder.class)
public class Box implements FlexComponent {
    FlexLayout layout;

    Integer flex;

    List<FlexComponent> contents;

    FlexMarginSize spacing;

    FlexMarginSize margin;

    FlexPosition position;

    String offsetTop;

    String offsetBottom;

    String offsetStart;

    String offsetEnd;

    String backgroundColor;

    String borderColor;

    String borderWidth;

    String cornerRadius;

    String width;

    String height;

    String paddingAll;

    String paddingTop;

    String paddingBottom;

    String paddingStart;

    String paddingEnd;

    Action action;

    @JsonPOJOBuilder(withPrefix = "")
    public static class BoxBuilder {
        @JsonSetter // Avoid conflict with same name method.
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
