/*
 * Copyright 2023 LINE Corporation
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


package com.linecorp.bot.messaging.model;

import java.time.Instant;

import java.util.Objects;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import com.linecorp.bot.messaging.model.Action;
import com.linecorp.bot.messaging.model.FlexBoxBackground;
import com.linecorp.bot.messaging.model.FlexComponent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 * FlexBox
 */
@JsonTypeName("box")
@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record FlexBox (
    /**
     * Get layout
     */

    @JsonProperty("layout")
    Layout layout,
    /**
     * Get flex
     */

    @JsonProperty("flex")
    Integer flex,
    /**
     * Get contents
     */

    @JsonProperty("contents")
    List<FlexComponent> contents,
    /**
     * Get spacing
     */

    @JsonProperty("spacing")
    String spacing,
    /**
     * Get margin
     */

    @JsonProperty("margin")
    String margin,
    /**
     * Get position
     */

    @JsonProperty("position")
    Position position,
    /**
     * Get offsetTop
     */

    @JsonProperty("offsetTop")
    String offsetTop,
    /**
     * Get offsetBottom
     */

    @JsonProperty("offsetBottom")
    String offsetBottom,
    /**
     * Get offsetStart
     */

    @JsonProperty("offsetStart")
    String offsetStart,
    /**
     * Get offsetEnd
     */

    @JsonProperty("offsetEnd")
    String offsetEnd,
    /**
     * Get backgroundColor
     */

    @JsonProperty("backgroundColor")
    String backgroundColor,
    /**
     * Get borderColor
     */

    @JsonProperty("borderColor")
    String borderColor,
    /**
     * Get borderWidth
     */

    @JsonProperty("borderWidth")
    String borderWidth,
    /**
     * Get cornerRadius
     */

    @JsonProperty("cornerRadius")
    String cornerRadius,
    /**
     * Get width
     */

    @JsonProperty("width")
    String width,
    /**
     * Get maxWidth
     */

    @JsonProperty("maxWidth")
    String maxWidth,
    /**
     * Get height
     */

    @JsonProperty("height")
    String height,
    /**
     * Get maxHeight
     */

    @JsonProperty("maxHeight")
    String maxHeight,
    /**
     * Get paddingAll
     */

    @JsonProperty("paddingAll")
    String paddingAll,
    /**
     * Get paddingTop
     */

    @JsonProperty("paddingTop")
    String paddingTop,
    /**
     * Get paddingBottom
     */

    @JsonProperty("paddingBottom")
    String paddingBottom,
    /**
     * Get paddingStart
     */

    @JsonProperty("paddingStart")
    String paddingStart,
    /**
     * Get paddingEnd
     */

    @JsonProperty("paddingEnd")
    String paddingEnd,
    /**
     * Get action
     */

    @JsonProperty("action")
    Action action,
    /**
     * Get justifyContent
     */

    @JsonProperty("justifyContent")
    JustifyContent justifyContent,
    /**
     * Get alignItems
     */

    @JsonProperty("alignItems")
    AlignItems alignItems,
    /**
     * Get background
     */

    @JsonProperty("background")
    FlexBoxBackground background
) implements FlexComponent  {
    /**
     * Gets or Sets layout
     */
    public enum Layout {
      @JsonProperty("horizontal")
      HORIZONTAL,
      @JsonProperty("vertical")
      VERTICAL,
      @JsonProperty("baseline")
      BASELINE,
      @JsonEnumDefaultValue
      UNDEFINED;
    }






    /**
     * Gets or Sets position
     */
    public enum Position {
      @JsonProperty("relative")
      RELATIVE,
      @JsonProperty("absolute")
      ABSOLUTE,
      @JsonEnumDefaultValue
      UNDEFINED;
    }




















    /**
     * Gets or Sets justifyContent
     */
    public enum JustifyContent {
      @JsonProperty("center")
      CENTER,
      @JsonProperty("flex-start")
      FLEX_START,
      @JsonProperty("flex-end")
      FLEX_END,
      @JsonProperty("space-between")
      SPACE_BETWEEN,
      @JsonProperty("space-around")
      SPACE_AROUND,
      @JsonProperty("space-evenly")
      SPACE_EVENLY,
      @JsonEnumDefaultValue
      UNDEFINED;
    }


    /**
     * Gets or Sets alignItems
     */
    public enum AlignItems {
      @JsonProperty("center")
      CENTER,
      @JsonProperty("flex-start")
      FLEX_START,
      @JsonProperty("flex-end")
      FLEX_END,
      @JsonEnumDefaultValue
      UNDEFINED;
    }



}

