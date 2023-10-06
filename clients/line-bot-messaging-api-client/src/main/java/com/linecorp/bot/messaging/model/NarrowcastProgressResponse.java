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

import java.time.OffsetDateTime;



/**
 * NarrowcastProgressResponse
 */
@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record NarrowcastProgressResponse (
    /**
     * The current status. One of:  &#x60;waiting&#x60;: Messages are not yet ready to be sent. They are currently being filtered or processed in some way. &#x60;sending&#x60;: Messages are currently being sent. &#x60;succeeded&#x60;: Messages were sent successfully. This may not mean the messages were successfully received. &#x60;failed&#x60;: Messages failed to be sent. Use the failedDescription property to find the cause of the failure. 
     */

    @JsonProperty("phase")
    Phase phase,
    /**
     * The number of users who successfully received the message.
     */

    @JsonProperty("successCount")
    Long successCount,
    /**
     * The number of users who failed to send the message.
     */

    @JsonProperty("failureCount")
    Long failureCount,
    /**
     * The number of intended recipients of the message.
     */

    @JsonProperty("targetCount")
    Long targetCount,
    /**
     * The reason the message failed to be sent. This is only included with a &#x60;phase&#x60; property value of &#x60;failed&#x60;.
     */

    @JsonProperty("failedDescription")
    String failedDescription,
    /**
     * Error summary. This is only included with a phase property value of failed. One of:  &#x60;1&#x60;: An internal error occurred. &#x60;2&#x60;: An error occurred because there weren&#39;t enough recipients. &#x60;3&#x60;: A conflict error of requests occurs because a request that has already been accepted is retried. 
     */

    @JsonProperty("errorCode")
    Long errorCode,
    /**
     * Narrowcast message request accepted time in milliseconds.  Format: ISO 8601 (e.g. 2020-12-03T10:15:30.121Z) Timezone: UTC 
     */

    @JsonProperty("acceptedTime")
    OffsetDateTime acceptedTime,
    /**
     * Processing of narrowcast message request completion time in milliseconds. Returned when the phase property is succeeded or failed.  Format: ISO 8601 (e.g. 2020-12-03T10:15:30.121Z) Timezone: UTC 
     */

    @JsonProperty("completedTime")
    OffsetDateTime completedTime
)  {
    /**
     * The current status. One of:  `waiting`: Messages are not yet ready to be sent. They are currently being filtered or processed in some way. `sending`: Messages are currently being sent. `succeeded`: Messages were sent successfully. This may not mean the messages were successfully received. `failed`: Messages failed to be sent. Use the failedDescription property to find the cause of the failure. 
     */
    public enum Phase {
      @JsonProperty("waiting")
      WAITING,
      @JsonProperty("sending")
      SENDING,
      @JsonProperty("succeeded")
      SUCCEEDED,
      @JsonProperty("failed")
      FAILED,
      @JsonEnumDefaultValue
      UNDEFINED;
    }









}

