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

/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech Do not edit the class manually.
 */
package com.linecorp.bot.messaging.model;



import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;

/** FlexBox */
@JsonTypeName("box")
@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record FlexBox(
    /** Get layout */
    @JsonProperty("layout") Layout layout,
    /** Get flex */
    @JsonProperty("flex") Integer flex,
    /** Get contents */
    @JsonProperty("contents") List<FlexComponent> contents,
    /** Get spacing */
    @JsonProperty("spacing") String spacing,
    /** Get margin */
    @JsonProperty("margin") String margin,
    /** Get position */
    @JsonProperty("position") Position position,
    /** Get offsetTop */
    @JsonProperty("offsetTop") String offsetTop,
    /** Get offsetBottom */
    @JsonProperty("offsetBottom") String offsetBottom,
    /** Get offsetStart */
    @JsonProperty("offsetStart") String offsetStart,
    /** Get offsetEnd */
    @JsonProperty("offsetEnd") String offsetEnd,
    /** Get backgroundColor */
    @JsonProperty("backgroundColor") String backgroundColor,
    /** Get borderColor */
    @JsonProperty("borderColor") String borderColor,
    /** Get borderWidth */
    @JsonProperty("borderWidth") String borderWidth,
    /** Get cornerRadius */
    @JsonProperty("cornerRadius") String cornerRadius,
    /** Get width */
    @JsonProperty("width") String width,
    /** Get maxWidth */
    @JsonProperty("maxWidth") String maxWidth,
    /** Get height */
    @JsonProperty("height") String height,
    /** Get maxHeight */
    @JsonProperty("maxHeight") String maxHeight,
    /** Get paddingAll */
    @JsonProperty("paddingAll") String paddingAll,
    /** Get paddingTop */
    @JsonProperty("paddingTop") String paddingTop,
    /** Get paddingBottom */
    @JsonProperty("paddingBottom") String paddingBottom,
    /** Get paddingStart */
    @JsonProperty("paddingStart") String paddingStart,
    /** Get paddingEnd */
    @JsonProperty("paddingEnd") String paddingEnd,
    /** Get action */
    @JsonProperty("action") Action action,
    /** Get justifyContent */
    @JsonProperty("justifyContent") JustifyContent justifyContent,
    /** Get alignItems */
    @JsonProperty("alignItems") AlignItems alignItems,
    /** Get background */
    @JsonProperty("background") FlexBoxBackground background)
    implements FlexComponent {
  /** Gets or Sets layout */
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

  /** Gets or Sets position */
  public enum Position {
    @JsonProperty("relative")
    RELATIVE,
    @JsonProperty("absolute")
    ABSOLUTE,

    @JsonEnumDefaultValue
    UNDEFINED;
  }

  /** Gets or Sets justifyContent */
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

  /** Gets or Sets alignItems */
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

  public static class Builder {
    private Layout layout;
    private Integer flex;
    private List<FlexComponent> contents;
    private String spacing;
    private String margin;
    private Position position;
    private String offsetTop;
    private String offsetBottom;
    private String offsetStart;
    private String offsetEnd;
    private String backgroundColor;
    private String borderColor;
    private String borderWidth;
    private String cornerRadius;
    private String width;
    private String maxWidth;
    private String height;
    private String maxHeight;
    private String paddingAll;
    private String paddingTop;
    private String paddingBottom;
    private String paddingStart;
    private String paddingEnd;
    private Action action;
    private JustifyContent justifyContent;
    private AlignItems alignItems;
    private FlexBoxBackground background;

    public Builder(Layout layout, List<FlexComponent> contents) {

      this.layout = layout;

      this.contents = contents;
    }

    public Builder flex(Integer flex) {
      this.flex = flex;
      return this;
    }

    public Builder spacing(String spacing) {
      this.spacing = spacing;
      return this;
    }

    public Builder margin(String margin) {
      this.margin = margin;
      return this;
    }

    public Builder position(Position position) {
      this.position = position;
      return this;
    }

    public Builder offsetTop(String offsetTop) {
      this.offsetTop = offsetTop;
      return this;
    }

    public Builder offsetBottom(String offsetBottom) {
      this.offsetBottom = offsetBottom;
      return this;
    }

    public Builder offsetStart(String offsetStart) {
      this.offsetStart = offsetStart;
      return this;
    }

    public Builder offsetEnd(String offsetEnd) {
      this.offsetEnd = offsetEnd;
      return this;
    }

    public Builder backgroundColor(String backgroundColor) {
      this.backgroundColor = backgroundColor;
      return this;
    }

    public Builder borderColor(String borderColor) {
      this.borderColor = borderColor;
      return this;
    }

    public Builder borderWidth(String borderWidth) {
      this.borderWidth = borderWidth;
      return this;
    }

    public Builder cornerRadius(String cornerRadius) {
      this.cornerRadius = cornerRadius;
      return this;
    }

    public Builder width(String width) {
      this.width = width;
      return this;
    }

    public Builder maxWidth(String maxWidth) {
      this.maxWidth = maxWidth;
      return this;
    }

    public Builder height(String height) {
      this.height = height;
      return this;
    }

    public Builder maxHeight(String maxHeight) {
      this.maxHeight = maxHeight;
      return this;
    }

    public Builder paddingAll(String paddingAll) {
      this.paddingAll = paddingAll;
      return this;
    }

    public Builder paddingTop(String paddingTop) {
      this.paddingTop = paddingTop;
      return this;
    }

    public Builder paddingBottom(String paddingBottom) {
      this.paddingBottom = paddingBottom;
      return this;
    }

    public Builder paddingStart(String paddingStart) {
      this.paddingStart = paddingStart;
      return this;
    }

    public Builder paddingEnd(String paddingEnd) {
      this.paddingEnd = paddingEnd;
      return this;
    }

    public Builder action(Action action) {
      this.action = action;
      return this;
    }

    public Builder justifyContent(JustifyContent justifyContent) {
      this.justifyContent = justifyContent;
      return this;
    }

    public Builder alignItems(AlignItems alignItems) {
      this.alignItems = alignItems;
      return this;
    }

    public Builder background(FlexBoxBackground background) {
      this.background = background;
      return this;
    }

    public FlexBox build() {
      return new FlexBox(
          layout,
          flex,
          contents,
          spacing,
          margin,
          position,
          offsetTop,
          offsetBottom,
          offsetStart,
          offsetEnd,
          backgroundColor,
          borderColor,
          borderWidth,
          cornerRadius,
          width,
          maxWidth,
          height,
          maxHeight,
          paddingAll,
          paddingTop,
          paddingBottom,
          paddingStart,
          paddingEnd,
          action,
          justifyContent,
          alignItems,
          background);
    }
  }
}
