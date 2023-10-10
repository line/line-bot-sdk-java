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


package com.linecorp.bot.audience.model;

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

import java.net.URI;


/**
 * Create audience for click-based retargeting
 */

@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record CreateClickBasedAudienceGroupRequest (
/**
    * The audience&#39;s name. This is case-insensitive, meaning AUDIENCE and audience are considered identical. Max character limit: 120 
    */
    
    @JsonProperty("description")
    String description,
/**
    * The request ID of a broadcast or narrowcast message sent in the past 60 days. Each Messaging API request has a request ID. 
    */
    
    @JsonProperty("requestId")
    String requestId,
/**
    * The URL clicked by the user. If empty, users who clicked any URL in the message are added to the list of recipients. Max character limit: 2,000 
    */
    
    @JsonProperty("clickUrl")
    URI clickUrl

)  {


}
