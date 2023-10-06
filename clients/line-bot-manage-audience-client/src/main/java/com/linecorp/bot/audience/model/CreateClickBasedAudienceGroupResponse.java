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

import com.linecorp.bot.audience.model.AudienceGroupType;
import java.net.URI;



/**
 * Create audience for click-based retargeting
 */
@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record CreateClickBasedAudienceGroupResponse (
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
     * When the audience was created (in UNIX time).
     */

    @JsonProperty("created")
    Long created,
    /**
     * The request ID that was specified when the audience was created.
     */

    @JsonProperty("requestId")
    String requestId,
    /**
     * The URL that was specified when the audience was created.
     */

    @JsonProperty("clickUrl")
    URI clickUrl,
    /**
     * How the audience was created. &#x60;MESSAGING_API&#x60;: An audience created with Messaging API. 
     */

    @JsonProperty("createRoute")
    CreateRoute createRoute,
    /**
     * Audience&#39;s update permission. Audiences linked to the same channel will be READ_WRITE.  - &#x60;READ&#x60;: Can use only. - &#x60;READ_WRITE&#x60;: Can use and update. 
     */

    @JsonProperty("permission")
    Permission permission,
    /**
     * Time of audience expiration. Only returned for specific audiences.
     */

    @JsonProperty("expireTimestamp")
    Long expireTimestamp,
    /**
     * The value indicating the type of account to be sent, as specified when creating the audience for uploading user IDs. One of:  true: Accounts are specified with IFAs. false (default): Accounts are specified with user IDs. 
     */

    @JsonProperty("isIfaAudience")
    Boolean isIfaAudience
)  {






    /**
     * How the audience was created. `MESSAGING_API`: An audience created with Messaging API. 
     */
    public enum CreateRoute {
      @JsonProperty("MESSAGING_API")
      MESSAGING_API,
      @JsonEnumDefaultValue
      UNDEFINED;
    }


    /**
     * Audience's update permission. Audiences linked to the same channel will be READ_WRITE.  - `READ`: Can use only. - `READ_WRITE`: Can use and update. 
     */
    public enum Permission {
      @JsonProperty("READ")
      READ,
      @JsonProperty("READ_WRITE")
      READ_WRITE,
      @JsonEnumDefaultValue
      UNDEFINED;
    }




}

