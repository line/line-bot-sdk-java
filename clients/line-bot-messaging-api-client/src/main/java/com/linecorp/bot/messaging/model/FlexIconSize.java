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
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * You can set the width of an Flex icon component with the &#x60;size&#x60; property, in pixels, as
 * a percentage, or with a keyword. FlexIconSize just provides only keywords.
 */
public enum FlexIconSize {
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
  _3XL,

  @JsonProperty("4xl")
  _4XL,

  @JsonProperty("5xl")
  _5XL,

  @JsonEnumDefaultValue
  UNDEFINED
}
