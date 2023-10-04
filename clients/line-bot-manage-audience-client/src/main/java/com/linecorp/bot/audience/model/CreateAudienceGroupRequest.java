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

import com.linecorp.bot.audience.model.Audience;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 * Create audience for uploading user IDs (by JSON)
 */
@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record CreateAudienceGroupRequest (
    /**
     * The audience&#39;s name. This is case-insensitive, meaning AUDIENCE and audience are considered identical. Max character limit: 120 
     */

    @JsonProperty("description")
    String description,
    /**
     * To specify recipients by IFAs: set true. To specify recipients by user IDs: set false or omit isIfaAudience property. 
     */

    @JsonProperty("isIfaAudience")
    Boolean isIfaAudience,
    /**
     * The description to register for the job (in jobs[].description). 
     */

    @JsonProperty("uploadDescription")
    String uploadDescription,
    /**
     * An array of user IDs or IFAs. Max number: 10,000 
     */

    @JsonProperty("audiences")
    List<Audience> audiences
)  {




}

