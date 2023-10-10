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


package com.linecorp.bot.insight.model;

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
 * Get number of message deliveries
 */

@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record GetNumberOfMessageDeliveriesResponse (
/**
    * Status of the counting process.
    */
    
    @JsonProperty("status")
    Status status,
/**
    * Number of messages sent to all of this LINE Official Account&#39;s friends (broadcast messages).
    */
    
    @JsonProperty("broadcast")
    Long broadcast,
/**
    * Number of messages sent to some of this LINE Official Account&#39;s friends, based on specific attributes (targeted messages).
    */
    
    @JsonProperty("targeting")
    Long targeting,
/**
    * Number of auto-response messages sent.
    */
    
    @JsonProperty("autoResponse")
    Long autoResponse,
/**
    * Number of greeting messages sent.
    */
    
    @JsonProperty("welcomeResponse")
    Long welcomeResponse,
/**
    * Number of messages sent from LINE Official Account Manager [Chat screen](https://www.linebiz.com/jp/manual/OfficialAccountManager/chats/) (only available in Japanese).
    */
    
    @JsonProperty("chat")
    Long chat,
/**
    * Number of broadcast messages sent with the &#x60;Send broadcast message&#x60; Messaging API operation.
    */
    
    @JsonProperty("apiBroadcast")
    Long apiBroadcast,
/**
    * Number of push messages sent with the &#x60;Send push message&#x60; Messaging API operation.
    */
    
    @JsonProperty("apiPush")
    Long apiPush,
/**
    * Number of multicast messages sent with the &#x60;Send multicast message&#x60; Messaging API operation.
    */
    
    @JsonProperty("apiMulticast")
    Long apiMulticast,
/**
    * Number of narrowcast messages sent with the &#x60;Send narrowcast message&#x60; Messaging API operation.
    */
    
    @JsonProperty("apiNarrowcast")
    Long apiNarrowcast,
/**
    * Number of replies sent with the &#x60;Send reply message&#x60; Messaging API operation.
    */
    
    @JsonProperty("apiReply")
    Long apiReply

)  {
/**
     * Status of the counting process.
     */
    public enum Status {
@JsonProperty("ready")
      READY,
    @JsonProperty("unready")
      UNREADY,
    @JsonProperty("out_of_service")
      OUT_OF_SERVICE,
    

      @JsonEnumDefaultValue
      UNDEFINED;
    }


}
