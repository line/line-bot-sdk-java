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

import java.net.URI;
import java.text.DecimalFormat;
import java.util.function.Supplier;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import com.linecorp.bot.model.message.flex.unit.FlexOffsetSize;
import com.linecorp.bot.model.message.flex.unit.FlexPosition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@JsonTypeName("icon")
@JsonInclude(Include.NON_NULL)
@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = Icon.IconBuilder.class)
public class Icon implements FlexComponent {
    @AllArgsConstructor
    @Getter
    public enum IconAspectRatio {
        R1TO1("1:1"),
        R2TO1("2:1"),
        R3TO1("3:1");

        private final String ratio;
    }

    URI url;

    String size;

    String aspectRatio;

    String margin;

    FlexPosition position;

    String offsetTop;

    String offsetBottom;

    String offsetStart;

    String offsetEnd;

    @JsonPOJOBuilder(withPrefix = "")
    public static class IconBuilder {
        private static final Supplier<DecimalFormat> RATIO_FORMAT = () -> new DecimalFormat("0.#####");

        public IconBuilder size(FlexFontSize size) {
            this.size = size.getPropertyValue();
            return this;
        }

        public IconBuilder size(String size) {
            this.size = size;
            return this;
        }

        public IconBuilder margin(FlexMarginSize margin) {
            this.margin = margin.getPropertyValue();
            return this;
        }

        public IconBuilder margin(String margin) {
            this.margin = margin;
            return this;
        }

        public IconBuilder offsetTop(FlexOffsetSize offset) {
            offsetTop = offset.getPropertyValue();
            return this;
        }

        public IconBuilder offsetTop(String offset) {
            offsetTop = offset;
            return this;
        }

        public IconBuilder offsetBottom(FlexOffsetSize offset) {
            offsetBottom = offset.getPropertyValue();
            return this;
        }

        public IconBuilder offsetBottom(String offset) {
            offsetBottom = offset;
            return this;
        }

        public IconBuilder offsetStart(FlexOffsetSize offset) {
            offsetStart = offset.getPropertyValue();
            return this;
        }

        public IconBuilder offsetStart(String offset) {
            offsetStart = offset;
            return this;
        }

        public IconBuilder offsetEnd(FlexOffsetSize offset) {
            offsetEnd = offset.getPropertyValue();
            return this;
        }

        public IconBuilder offsetEnd(String offset) {
            offsetEnd = offset;
            return this;
        }

        public IconBuilder aspectRatio(IconAspectRatio aspectRatio) {
            this.aspectRatio = aspectRatio.getRatio();
            return this;
        }

        public IconBuilder aspectRatio(String aspectRatio) {
            this.aspectRatio = aspectRatio;
            return this;
        }

        public IconBuilder aspectRatio(double width, double height) {
            final DecimalFormat fmt = RATIO_FORMAT.get();
            aspectRatio = fmt.format(width) + ':' + fmt.format(height);
            return this;
        }
    }
}
