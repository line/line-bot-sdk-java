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


package com.linecorp.bot.oauth.model;

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
 * Verification result
 */

@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record VerifyChannelAccessTokenResponse (
/**
    * The channel ID for which the channel access token was issued.
    */
    
    @JsonProperty("client_id")
    String clientId,
/**
    * Number of seconds before the channel access token expires.
    */
    
    @JsonProperty("expires_in")
    Long expiresIn,
/**
    * Permissions granted to the channel access token.
    */
    
    @JsonProperty("scope")
    String scope

)  {


    public static class Builder {
private String clientId;
private Long expiresIn;
private String scope;


        public Builder() {
        }

public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }
public Builder expiresIn(Long expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }
public Builder scope(String scope) {
            this.scope = scope;
            return this;
        }


        public VerifyChannelAccessTokenResponse build() {
            return new VerifyChannelAccessTokenResponse(
clientId,expiresIn,scope
            );
        }
    }
}
