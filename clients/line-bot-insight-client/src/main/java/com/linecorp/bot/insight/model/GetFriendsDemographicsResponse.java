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

import com.linecorp.bot.insight.model.AgeTile;
import com.linecorp.bot.insight.model.AppTypeTile;
import com.linecorp.bot.insight.model.AreaTile;
import com.linecorp.bot.insight.model.GenderTile;
import com.linecorp.bot.insight.model.SubscriptionPeriodTile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Get friend demographics
 */

@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record GetFriendsDemographicsResponse (
/**
    * true if friend demographic information is available.
    */
    
    @JsonProperty("available")
    Boolean available,
/**
    * Percentage per gender.
    */
    
    @JsonProperty("genders")
    List<GenderTile> genders,
/**
    * Percentage per age group.
    */
    
    @JsonProperty("ages")
    List<AgeTile> ages,
/**
    * Percentage per area.
    */
    
    @JsonProperty("areas")
    List<AreaTile> areas,
/**
    * Percentage by OS.
    */
    
    @JsonProperty("appTypes")
    List<AppTypeTile> appTypes,
/**
    * Percentage per friendship duration.
    */
    
    @JsonProperty("subscriptionPeriods")
    List<SubscriptionPeriodTile> subscriptionPeriods

)  {


}
