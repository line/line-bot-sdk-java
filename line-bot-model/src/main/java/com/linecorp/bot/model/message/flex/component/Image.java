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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.message.flex.unit.FlexAlign;
import com.linecorp.bot.model.message.flex.unit.FlexGravity;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import com.linecorp.bot.model.message.flex.unit.FlexOffsetSize;
import com.linecorp.bot.model.message.flex.unit.FlexPosition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@JsonTypeName("image")
@JsonInclude(Include.NON_NULL)
@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = Image.ImageBuilder.class)
public class Image implements FlexComponent {

    @AllArgsConstructor
    @Getter
    public enum ImageSize {
        @JsonProperty("xxs")
        XXS("xxs"),
        @JsonProperty("xs")
        XS("xs"),
        @JsonProperty("sm")
        SM("sm"),
        @JsonProperty("md")
        MD("md"),
        @JsonProperty("lg")
        LG("lg"),
        @JsonProperty("xl")
        XL("xl"),
        @JsonProperty("xxl")
        XXL("xxl"),
        @JsonProperty("3xl")
        XXXL("3xl"),
        @JsonProperty("4xl")
        XXXXL("4xl"),
        @JsonProperty("5xl")
        XXXXXL("5xl"),
        @JsonProperty("full")
        FULL_WIDTH("full");

        private final String propertyValue;
    }

    @AllArgsConstructor
    @Getter
    public enum ImageAspectRatio {
        R1TO1("1:1"),
        R20TO13("20:13"),
        R1_91TO1("1.91:1"),
        R4TO3("4:3"),
        R16TO9("16:9"),
        R2TO1("2:1"),
        R3TO1("3:1"),
        R3TO4("3:4"),
        R9TO16("9:16"),
        R1TO2("1:2"),
        R1TO3("1:3"),
        R1_51TO1("1.51:1");

        private final String ratio;
    }

    public enum ImageAspectMode {
        @JsonProperty("fit")
        Fit,
        @JsonProperty("cover")
        Cover,
    }

    Integer flex;

    URI url;

    String size;

    String aspectRatio;

    ImageAspectMode aspectMode;

    String backgroundColor;

    FlexAlign align;

    Action action;

    FlexGravity gravity;

    String margin;

    FlexPosition position;

    String offsetTop;

    String offsetBottom;

    String offsetStart;

    String offsetEnd;

    Boolean animated;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ImageBuilder {
        private static final Supplier<DecimalFormat> RATIO_FORMAT = () -> new DecimalFormat("0.#####");

        public ImageBuilder size(ImageSize size) {
            this.size = size.getPropertyValue();
            return this;
        }

        public ImageBuilder size(String size) {
            this.size = size;
            return this;
        }

        /**
         * Specify aspect ratio by keyword.
         */
        public ImageBuilder aspectRatio(ImageAspectRatio aspectRatio) {
            this.aspectRatio = aspectRatio.getRatio();
            return this;
        }

        /**
         * Specify custom aspect ratio.
         */
        public ImageBuilder aspectRatio(String aspectRatio) {
            this.aspectRatio = aspectRatio;
            return this;
        }

        /**
         * Specify custom aspect ratio. The width and height are rounded up to 5 decimal places.
         */
        public ImageBuilder aspectRatio(double width, double height) {
            final DecimalFormat fmt = RATIO_FORMAT.get();
            aspectRatio = fmt.format(width) + ':' + fmt.format(height);
            return this;
        }

        public ImageBuilder margin(FlexMarginSize margin) {
            this.margin = margin.getPropertyValue();
            return this;
        }

        public ImageBuilder margin(String margin) {
            this.margin = margin;
            return this;
        }

        public ImageBuilder offsetTop(FlexOffsetSize offset) {
            offsetTop = offset.getPropertyValue();
            return this;
        }

        public ImageBuilder offsetTop(String offset) {
            offsetTop = offset;
            return this;
        }

        public ImageBuilder offsetBottom(FlexOffsetSize offset) {
            offsetBottom = offset.getPropertyValue();
            return this;
        }

        public ImageBuilder offsetBottom(String offset) {
            offsetBottom = offset;
            return this;
        }

        public ImageBuilder offsetStart(FlexOffsetSize offset) {
            offsetStart = offset.getPropertyValue();
            return this;
        }

        public ImageBuilder offsetStart(String offset) {
            offsetStart = offset;
            return this;
        }

        public ImageBuilder offsetEnd(FlexOffsetSize offset) {
            offsetEnd = offset.getPropertyValue();
            return this;
        }

        public ImageBuilder offsetEnd(String offset) {
            offsetEnd = offset;
            return this;
        }
    }
}
