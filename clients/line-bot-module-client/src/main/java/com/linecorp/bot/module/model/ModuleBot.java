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


package com.linecorp.bot.module.model;

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



/**
 * basic information about the bot.
 */

@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record ModuleBot (
/**
    * Bot&#39;s user ID
    */
    
    @JsonProperty("userId")
    String userId,
/**
    * Bot&#39;s basic ID
    */
    
    @JsonProperty("basicId")
    String basicId,
/**
    * Bot&#39;s premium ID. Not included in the response if the premium ID isn&#39;t set.
    */
    
    @JsonProperty("premiumId")
    String premiumId,
/**
    * Bot&#39;s display name
    */
    
    @JsonProperty("displayName")
    String displayName,
/**
    * Profile image URL. Image URL starting with &#x60;https://&#x60;. Not included in the response if the bot doesn&#39;t have a profile image.
    */
    
    @JsonProperty("pictureUrl")
    String pictureUrl

)  {


}
