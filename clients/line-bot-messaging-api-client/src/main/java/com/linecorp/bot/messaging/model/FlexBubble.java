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
import com.linecorp.bot.messaging.model.FlexBox;
import com.linecorp.bot.messaging.model.FlexBubbleStyles;
import com.linecorp.bot.messaging.model.FlexComponent;
import com.linecorp.bot.messaging.model.FlexContainer;



/**
 * FlexBubble
 */
@JsonTypeName("bubble")
@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record FlexBubble (
    /**
     * Get direction
     */

    @JsonProperty("direction")
    Direction direction,
    /**
     * Get styles
     */

    @JsonProperty("styles")
    FlexBubbleStyles styles,
    /**
     * Get header
     */

    @JsonProperty("header")
    FlexBox header,
    /**
     * Get hero
     */

    @JsonProperty("hero")
    FlexComponent hero,
    /**
     * Get body
     */

    @JsonProperty("body")
    FlexBox body,
    /**
     * Get footer
     */

    @JsonProperty("footer")
    FlexBox footer,
    /**
     * Get size
     */

    @JsonProperty("size")
    Size size,
    /**
     * Get action
     */

    @JsonProperty("action")
    Action action
) implements FlexContainer  {
    /**
     * Gets or Sets direction
     */
    public enum Direction {
      @JsonProperty("ltr")
      LTR,
      @JsonProperty("rtl")
      RTL,
      @JsonEnumDefaultValue
      UNDEFINED;
    }







    /**
     * Gets or Sets size
     */
    public enum Size {
      @JsonProperty("nano")
      NANO,
      @JsonProperty("micro")
      MICRO,
      @JsonProperty("deca")
      DECA,
      @JsonProperty("hecto")
      HECTO,
      @JsonProperty("kilo")
      KILO,
      @JsonProperty("mega")
      MEGA,
      @JsonProperty("giga")
      GIGA,
      @JsonEnumDefaultValue
      UNDEFINED;
    }



}

