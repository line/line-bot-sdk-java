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
import com.linecorp.bot.model.message.flex.unit.FlexAlign;
import com.linecorp.bot.model.message.flex.unit.FlexGravity;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonTypeName("image")
@JsonInclude(Include.NON_NULL)
public class Image implements FlexComponent {

    public enum ImageSize {
        @JsonProperty("xxs")
        XXS,
        @JsonProperty("xs")
        XS,
        @JsonProperty("sm")
        SM,
        @JsonProperty("md")
        MD,
        @JsonProperty("lg")
        LG,
        @JsonProperty("xl")
        XL,
        @JsonProperty("xxl")
        XXL,
        @JsonProperty("3xl")
        XXXL,
        @JsonProperty("4xl")
        XXXXL,
        @JsonProperty("5xl")
        XXXXXL,
        @JsonProperty("full")
        FULL_WIDTH,
    }

    public enum ImageAspectRatio {
        @JsonProperty("1:1")
        R1TO1,
        @JsonProperty("20:13")
        R20TO13,
        @JsonProperty("1.91:1")
        R1_91TO1,
        @JsonProperty("4:3")
        R4TO3,
        @JsonProperty("16:9")
        R16TO9,
        @JsonProperty("2:1")
        R2TO1,
        @JsonProperty("3:1")
        R3TO1,
        @JsonProperty("3:4")
        R3TO4,
        @JsonProperty("9:16")
        R9TO16,
        @JsonProperty("1:2")
        R1TO2,
        @JsonProperty("1:3")
        R1TO3,
        @JsonProperty("1.51:1")
        R1_51TO1,
    }

    public enum ImageAspectMode {
        @JsonProperty("fit")
        Fit,
        @JsonProperty("cover")
        Cover,
    }

    private final Integer flex;

    private final String url;

    private final ImageSize size;

    private final ImageAspectRatio aspectRatio;

    private final ImageAspectMode aspectMode;

    private final String backgroundColor;

    private final FlexAlign align;

    private final Action action;

    private final FlexGravity gravity;

    private final FlexMarginSize margin;

    @JsonCreator
    public Image(
            @JsonProperty("flex") Integer flex,
            @JsonProperty("url") String url,
            @JsonProperty("size") ImageSize size,
            @JsonProperty("aspectRatio") ImageAspectRatio aspectRatio,
            @JsonProperty("aspectMode") ImageAspectMode aspectMode,
            @JsonProperty("backgroundColor") String backgroundColor,
            @JsonProperty("align") FlexAlign align,
            @JsonProperty("action") Action action,
            @JsonProperty("gravity") FlexGravity gravity,
            @JsonProperty("margin") FlexMarginSize margin) {
        this.flex = flex;
        this.url = url;
        this.size = size;
        this.aspectRatio = aspectRatio;
        this.aspectMode = aspectMode;
        this.backgroundColor = backgroundColor;
        this.align = align;
        this.action = action;
        this.gravity = gravity;
        this.margin = margin;
    }
}
