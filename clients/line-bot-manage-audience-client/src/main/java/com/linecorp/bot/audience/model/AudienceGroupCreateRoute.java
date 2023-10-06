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




import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * How the audience was created. One of:  - `OA_MANAGER`: Audience created with [LINE Official Account Manager](https://manager.line.biz/). - `MESSAGING_API`: Audience created with Messaging API. - `POINT_AD`: Audience created with [LINE Points Ads](https://www.linebiz.com/jp/service/line-point-ad/) (Japanese only). - `AD_MANAGER`: Audience created with [LINE Ads](https://admanager.line.biz/). 
 */
public enum AudienceGroupCreateRoute {
  
    @JsonProperty("OA_MANAGER")
  OA_MANAGER,
  
    @JsonProperty("MESSAGING_API")
  MESSAGING_API,
  
    @JsonProperty("POINT_AD")
  POINT_AD,
  
    @JsonProperty("AD_MANAGER")
  AD_MANAGER,
  
    @JsonEnumDefaultValue
    UNDEFINED
}

