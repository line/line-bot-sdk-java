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


package com.linecorp.bot.liff.model;

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
 * LiffView
 */

@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record LiffView (
/**
    * Size of the LIFF app view. Specify one of these values: - compact - tall - full 
    */
    
    @JsonProperty("type")
    Type type,
/**
    * Endpoint URL. This is the URL of the web app that implements the LIFF app (e.g. https://example.com). Used when the LIFF app is launched using the LIFF URL. The URL scheme must be https. URL fragments (#URL-fragment) can&#39;t be specified. 
    */
    
    @JsonProperty("url")
    URI url,
/**
    * &#x60;true&#x60; to use the LIFF app in modular mode. When in modular mode, the action button in the header is not displayed. 
    */
    
    @JsonProperty("moduleMode")
    Boolean moduleMode

)  {
/**
     * Size of the LIFF app view. Specify one of these values: - compact - tall - full 
     */
    public enum Type {
@JsonProperty("compact")
      COMPACT,
    @JsonProperty("tall")
      TALL,
    @JsonProperty("full")
      FULL,
    

      @JsonEnumDefaultValue
      UNDEFINED;
    }


}
