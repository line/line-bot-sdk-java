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

import com.linecorp.bot.liff.model.LiffBotPrompt;
import com.linecorp.bot.liff.model.LiffFeatures;
import com.linecorp.bot.liff.model.LiffScope;
import com.linecorp.bot.liff.model.LiffView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * AddLiffAppRequest
 * @see <a href="https://developers.line.biz/en/reference/liff-server/#add-liff-app"> Documentation</a>
 */

@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record AddLiffAppRequest (
/**
    * Get view
    */
    
    @JsonProperty("view")
    LiffView view,
/**
    * Name of the LIFF app.  The LIFF app name can&#39;t include \&quot;LINE\&quot; or similar strings, or inappropriate strings. 
    */
    
    @JsonProperty("description")
    String description,
/**
    * Get features
    */
    
    @JsonProperty("features")
    LiffFeatures features,
/**
    * How additional information in LIFF URLs is handled. Specify &#x60;concat&#x60;. 
    */
    
    @JsonProperty("permanentLinkPattern")
    String permanentLinkPattern,
/**
    * Get scope
    */
    
    @JsonProperty("scope")
    List<LiffScope> scope,
/**
    * Get botPrompt
    */
    
    @JsonProperty("botPrompt")
    LiffBotPrompt botPrompt

)  {


    public static class Builder {
private LiffView view;
    
        private boolean view$set;
    
private String description;
    
private LiffFeatures features;
    
private String permanentLinkPattern;
    
private List<LiffScope> scope;
    
private LiffBotPrompt botPrompt;
    


        public Builder() {
        }

public Builder view(LiffView view) {
            this.view = view;
    
            this.view$set = true;
    
            return this;
        }
public Builder description(String description) {
            this.description = description;
    
            return this;
        }
public Builder features(LiffFeatures features) {
            this.features = features;
    
            return this;
        }
public Builder permanentLinkPattern(String permanentLinkPattern) {
            this.permanentLinkPattern = permanentLinkPattern;
    
            return this;
        }
public Builder scope(List<LiffScope> scope) {
            this.scope = scope;
    
            return this;
        }
public Builder botPrompt(LiffBotPrompt botPrompt) {
            this.botPrompt = botPrompt;
    
            return this;
        }


        public AddLiffAppRequest build() {

            if (!this.view$set) {
                throw new IllegalStateException("'view' must be set for AddLiffAppRequest.");
            }
    







            return new AddLiffAppRequest(
view,description,features,permanentLinkPattern,scope,botPrompt
            );
        }
    }
}
