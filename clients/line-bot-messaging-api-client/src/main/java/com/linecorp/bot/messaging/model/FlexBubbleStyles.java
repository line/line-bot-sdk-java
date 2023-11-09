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
 * https://openapi-generator.tech
 * Do not edit the class manually.
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

import com.linecorp.bot.messaging.model.FlexBlockStyle;


/**
 * FlexBubbleStyles
 */

@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record FlexBubbleStyles (
/**
    * Get header
    */
    
    @JsonProperty("header")
    FlexBlockStyle header,
/**
    * Get hero
    */
    
    @JsonProperty("hero")
    FlexBlockStyle hero,
/**
    * Get body
    */
    
    @JsonProperty("body")
    FlexBlockStyle body,
/**
    * Get footer
    */
    
    @JsonProperty("footer")
    FlexBlockStyle footer

)  {


    public static class Builder {
private FlexBlockStyle header;
private FlexBlockStyle hero;
private FlexBlockStyle body;
private FlexBlockStyle footer;


        public Builder() {
        }

public Builder header(FlexBlockStyle header) {
            this.header = header;
            return this;
        }
public Builder hero(FlexBlockStyle hero) {
            this.hero = hero;
            return this;
        }
public Builder body(FlexBlockStyle body) {
            this.body = body;
            return this;
        }
public Builder footer(FlexBlockStyle footer) {
            this.footer = footer;
            return this;
        }


        public FlexBubbleStyles build() {
            return new FlexBubbleStyles(
header,hero,body,footer
            );
        }
    }
}
