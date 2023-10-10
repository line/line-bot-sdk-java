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


}
