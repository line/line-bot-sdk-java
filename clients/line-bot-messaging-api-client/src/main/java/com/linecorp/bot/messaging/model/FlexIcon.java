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
import java.net.URI;

/**
 * FlexIcon
 *
 * @see <a href="https://developers.line.biz/en/reference/messaging-api/#icon">Documentation</a>
 */
@JsonTypeName("icon")
@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record FlexIcon(
    /** Get url */
    @JsonProperty("url") URI url,
    /** Get size */
    @JsonProperty("size") String size,
    /** Get aspectRatio */
    @JsonProperty("aspectRatio") String aspectRatio,
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
    /** Get scaling */
    @JsonProperty("scaling") Boolean scaling)
    implements FlexComponent {
  /** Gets or Sets position */
  public enum Position {
    @JsonProperty("relative")
    RELATIVE,
    @JsonProperty("absolute")
    ABSOLUTE,

    @JsonEnumDefaultValue
    UNDEFINED;
  }

  public static class Builder {
    private URI url;
    private String size;
    private String aspectRatio;
    private String margin;
    private Position position;
    private String offsetTop;
    private String offsetBottom;
    private String offsetStart;
    private String offsetEnd;
    private Boolean scaling;

    public Builder(URI url) {

      this.url = url;
    }

    public Builder size(String size) {
      this.size = size;
      return this;
    }

    public Builder aspectRatio(String aspectRatio) {
      this.aspectRatio = aspectRatio;
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

    public Builder scaling(Boolean scaling) {
      this.scaling = scaling;
      return this;
    }

    public FlexIcon build() {
      return new FlexIcon(
          url,
          size,
          aspectRatio,
          margin,
          position,
          offsetTop,
          offsetBottom,
          offsetStart,
          offsetEnd,
          scaling);
    }
  }
}
