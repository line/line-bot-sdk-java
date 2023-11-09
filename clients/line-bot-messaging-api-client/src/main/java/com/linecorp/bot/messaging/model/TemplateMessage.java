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

import com.linecorp.bot.messaging.model.Message;
import com.linecorp.bot.messaging.model.QuickReply;
import com.linecorp.bot.messaging.model.Sender;
import com.linecorp.bot.messaging.model.Template;


/**
 * TemplateMessage
 * @see <a href="https://developers.line.biz/en/reference/messaging-api/#template-messages"> Documentation</a>
 */
@JsonTypeName("template")

@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record TemplateMessage (
/**
    * Get quickReply
    */
    
    @JsonProperty("quickReply")
    QuickReply quickReply,
/**
    * Get sender
    */
    
    @JsonProperty("sender")
    Sender sender,
/**
    * Get altText
    */
    
    @JsonProperty("altText")
    String altText,
/**
    * Get template
    */
    
    @JsonProperty("template")
    Template template

) implements Message {

public TemplateMessage(String altText, Template template) {
    this(null, null, altText, template);
}

    public static class Builder {
private QuickReply quickReply;
    
private Sender sender;
    
private String altText;
    
        private boolean altText$set;
    
private Template template;
    
        private boolean template$set;
    


        public Builder() {
        }

public Builder quickReply(QuickReply quickReply) {
            this.quickReply = quickReply;
    
            return this;
        }
public Builder sender(Sender sender) {
            this.sender = sender;
    
            return this;
        }
public Builder altText(String altText) {
            this.altText = altText;
    
            this.altText$set = true;
    
            return this;
        }
public Builder template(Template template) {
            this.template = template;
    
            this.template$set = true;
    
            return this;
        }


        public TemplateMessage build() {



            if (!this.altText$set) {
                throw new IllegalStateException("'altText' must be set for TemplateMessage.");
            }
    

            if (!this.template$set) {
                throw new IllegalStateException("'template' must be set for TemplateMessage.");
            }
    


            return new TemplateMessage(
quickReply,sender,altText,template
            );
        }
    }
}
