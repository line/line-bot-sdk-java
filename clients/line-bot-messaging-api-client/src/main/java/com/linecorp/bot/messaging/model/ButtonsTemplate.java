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

import com.linecorp.bot.messaging.model.Action;
import com.linecorp.bot.messaging.model.Template;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * ButtonsTemplate
 */
@JsonTypeName("buttons")

@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record ButtonsTemplate (
/**
    * Get thumbnailImageUrl
    */
    
    @JsonProperty("thumbnailImageUrl")
    URI thumbnailImageUrl,
/**
    * Get imageAspectRatio
    */
    
    @JsonProperty("imageAspectRatio")
    String imageAspectRatio,
/**
    * Get imageSize
    */
    
    @JsonProperty("imageSize")
    String imageSize,
/**
    * Get imageBackgroundColor
    */
    
    @JsonProperty("imageBackgroundColor")
    String imageBackgroundColor,
/**
    * Get title
    */
    
    @JsonProperty("title")
    String title,
/**
    * Get text
    */
    
    @JsonProperty("text")
    String text,
/**
    * Get defaultAction
    */
    
    @JsonProperty("defaultAction")
    Action defaultAction,
/**
    * Get actions
    */
    
    @JsonProperty("actions")
    List<Action> actions

) implements Template {


    public static class Builder {
private URI thumbnailImageUrl;
private String imageAspectRatio;
private String imageSize;
private String imageBackgroundColor;
private String title;
private String text;
private Action defaultAction;
private List<Action> actions;


        public Builder() {
        }

public Builder thumbnailImageUrl(URI thumbnailImageUrl) {
            this.thumbnailImageUrl = thumbnailImageUrl;
            return this;
        }
public Builder imageAspectRatio(String imageAspectRatio) {
            this.imageAspectRatio = imageAspectRatio;
            return this;
        }
public Builder imageSize(String imageSize) {
            this.imageSize = imageSize;
            return this;
        }
public Builder imageBackgroundColor(String imageBackgroundColor) {
            this.imageBackgroundColor = imageBackgroundColor;
            return this;
        }
public Builder title(String title) {
            this.title = title;
            return this;
        }
public Builder text(String text) {
            this.text = text;
            return this;
        }
public Builder defaultAction(Action defaultAction) {
            this.defaultAction = defaultAction;
            return this;
        }
public Builder actions(List<Action> actions) {
            this.actions = actions;
            return this;
        }


        public ButtonsTemplate build() {
            return new ButtonsTemplate(
thumbnailImageUrl,imageAspectRatio,imageSize,imageBackgroundColor,title,text,defaultAction,actions
            );
        }
    }
}
