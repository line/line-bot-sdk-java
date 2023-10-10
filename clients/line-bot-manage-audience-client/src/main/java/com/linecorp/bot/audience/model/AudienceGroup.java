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

import com.linecorp.bot.audience.model.AudienceGroupCreateRoute;
import com.linecorp.bot.audience.model.AudienceGroupFailedType;
import com.linecorp.bot.audience.model.AudienceGroupPermission;
import com.linecorp.bot.audience.model.AudienceGroupStatus;
import com.linecorp.bot.audience.model.AudienceGroupType;
import java.net.URI;


/**
 * Audience group
 */

@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record AudienceGroup (
/**
    * The audience ID.
    */
    
    @JsonProperty("audienceGroupId")
    Long audienceGroupId,
/**
    * Get type
    */
    
    @JsonProperty("type")
    AudienceGroupType type,
/**
    * The audience&#39;s name.
    */
    
    @JsonProperty("description")
    String description,
/**
    * Get status
    */
    
    @JsonProperty("status")
    AudienceGroupStatus status,
/**
    * Get failedType
    */
    
    @JsonProperty("failedType")
    AudienceGroupFailedType failedType,
/**
    * The number of users included in the audience.
    */
    
    @JsonProperty("audienceCount")
    Long audienceCount,
/**
    * When the audience was created (in UNIX time).
    */
    
    @JsonProperty("created")
    Long created,
/**
    * The request ID that was specified when the audience was created. This is only included when &#x60;audienceGroup.type&#x60; is CLICK or IMP. 
    */
    
    @JsonProperty("requestId")
    String requestId,
/**
    * The URL that was specified when the audience was created. This is only included when &#x60;audienceGroup.type&#x60; is CLICK and link URL is specified. 
    */
    
    @JsonProperty("clickUrl")
    URI clickUrl,
/**
    * The value indicating the type of account to be sent, as specified when creating the audience for uploading user IDs. 
    */
    
    @JsonProperty("isIfaAudience")
    Boolean isIfaAudience,
/**
    * Get permission
    */
    
    @JsonProperty("permission")
    AudienceGroupPermission permission,
/**
    * Get createRoute
    */
    
    @JsonProperty("createRoute")
    AudienceGroupCreateRoute createRoute

)  {


}
