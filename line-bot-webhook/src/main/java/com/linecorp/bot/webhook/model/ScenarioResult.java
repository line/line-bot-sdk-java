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


package com.linecorp.bot.webhook.model;

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

import com.linecorp.bot.webhook.model.ActionResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * ScenarioResult
 */

@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record ScenarioResult (
/**
    * Scenario ID executed
    */
    

    @JsonProperty("scenarioId")
    String scenarioId,
/**
    * Revision number of the scenario set containing the executed scenario
    */
    

    @JsonProperty("revision")
    Integer revision,
/**
    * Timestamp for when execution of scenario action started (milliseconds, LINE app time)
    */
    

    @JsonProperty("startTime")
    Long startTime,
/**
    * Timestamp for when execution of scenario was completed (milliseconds, LINE app time)
    */
    

    @JsonProperty("endTime")
    Long endTime,
/**
    * Scenario execution completion status
    */
    

    @JsonProperty("resultCode")
    String resultCode,
/**
    * Execution result of individual operations specified in action. Only included when things.result.resultCode is success.
    */
    

    @JsonProperty("actionResults")
    List<ActionResult> actionResults,
/**
    * Data contained in notification.
    */
    

    @JsonProperty("bleNotificationPayload")
    String bleNotificationPayload,
/**
    * Error reason.
    */
    

    @JsonProperty("errorReason")
    String errorReason

)  {


}
