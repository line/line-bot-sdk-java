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




/**
 * NumberOfMessagesResponse
 */
@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record NumberOfMessagesResponse (
    /**
     * Aggregation process status. One of:  &#x60;ready&#x60;: The number of messages can be obtained. &#x60;unready&#x60;: We haven&#39;t finished calculating the number of sent messages for the specified in date. For example, this property is returned when the delivery date or a future date is specified. Calculation usually takes about a day. &#x60;unavailable_for_privacy&#x60;: The total number of messages on the specified day is less than 20. &#x60;out_of_service&#x60;: The specified date is earlier than the date on which we first started calculating sent messages (March 31, 2018). 
     */

    @JsonProperty("status")
    Status status,
    /**
     * The number of messages delivered using the phone number on the date specified in &#x60;date&#x60;. The response has this property only when the value of &#x60;status&#x60; is &#x60;ready&#x60;.  
     */

    @JsonProperty("success")
    Long success
)  {
    /**
     * Aggregation process status. One of:  `ready`: The number of messages can be obtained. `unready`: We haven't finished calculating the number of sent messages for the specified in date. For example, this property is returned when the delivery date or a future date is specified. Calculation usually takes about a day. `unavailable_for_privacy`: The total number of messages on the specified day is less than 20. `out_of_service`: The specified date is earlier than the date on which we first started calculating sent messages (March 31, 2018). 
     */
    public enum Status {
      @JsonProperty("ready")
      READY,
      @JsonProperty("unready")
      UNREADY,
      @JsonProperty("unavailable_for_privacy")
      UNAVAILABLE_FOR_PRIVACY,
      @JsonProperty("out_of_service")
      OUT_OF_SERVICE,
      @JsonEnumDefaultValue
      UNDEFINED;
    }



}

