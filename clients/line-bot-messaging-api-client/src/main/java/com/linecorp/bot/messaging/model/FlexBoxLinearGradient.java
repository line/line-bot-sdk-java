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

import com.linecorp.bot.messaging.model.FlexBoxBackground;


/**
 * FlexBoxLinearGradient
 */
@JsonTypeName("linearGradient")

@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record FlexBoxLinearGradient (
/**
    * Get angle
    */
    
    @JsonProperty("angle")
    String angle,
/**
    * Get startColor
    */
    
    @JsonProperty("startColor")
    String startColor,
/**
    * Get endColor
    */
    
    @JsonProperty("endColor")
    String endColor,
/**
    * Get centerColor
    */
    
    @JsonProperty("centerColor")
    String centerColor,
/**
    * Get centerPosition
    */
    
    @JsonProperty("centerPosition")
    String centerPosition

) implements FlexBoxBackground {


    public static class Builder {
private String angle;
    
private String startColor;
    
private String endColor;
    
private String centerColor;
    
private String centerPosition;
    


        public Builder() {
        }

public Builder angle(String angle) {
            this.angle = angle;
    
            return this;
        }
public Builder startColor(String startColor) {
            this.startColor = startColor;
    
            return this;
        }
public Builder endColor(String endColor) {
            this.endColor = endColor;
    
            return this;
        }
public Builder centerColor(String centerColor) {
            this.centerColor = centerColor;
    
            return this;
        }
public Builder centerPosition(String centerPosition) {
            this.centerPosition = centerPosition;
    
            return this;
        }


        public FlexBoxLinearGradient build() {







            return new FlexBoxLinearGradient(
angle,startColor,endColor,centerColor,centerPosition
            );
        }
    }
}
