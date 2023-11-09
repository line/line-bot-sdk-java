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

import java.net.URI;


/**
 * GroupSummaryResponse
 * @see <a href="https://developers.line.biz/en/reference/messaging-api/#get-group-summary"> Documentation</a>
 */

@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record GroupSummaryResponse (
/**
    * Group ID
    */
    
    @JsonProperty("groupId")
    String groupId,
/**
    * Group name
    */
    
    @JsonProperty("groupName")
    String groupName,
/**
    * Group icon URL. Not included in the response if the user doesn&#39;t set a group profile icon.
    */
    
    @JsonProperty("pictureUrl")
    URI pictureUrl

)  {


    public static class Builder {
private String groupId;
    
        private boolean groupId$set;
    
private String groupName;
    
        private boolean groupName$set;
    
private URI pictureUrl;
    


        public Builder() {
        }

public Builder groupId(String groupId) {
            this.groupId = groupId;
    
            this.groupId$set = true;
    
            return this;
        }
public Builder groupName(String groupName) {
            this.groupName = groupName;
    
            this.groupName$set = true;
    
            return this;
        }
public Builder pictureUrl(URI pictureUrl) {
            this.pictureUrl = pictureUrl;
    
            return this;
        }


        public GroupSummaryResponse build() {

            if (!this.groupId$set) {
                throw new IllegalStateException("'groupId' must be set for GroupSummaryResponse.");
            }
    

            if (!this.groupName$set) {
                throw new IllegalStateException("'groupName' must be set for GroupSummaryResponse.");
            }
    



            return new GroupSummaryResponse(
groupId,groupName,pictureUrl
            );
        }
    }
}
