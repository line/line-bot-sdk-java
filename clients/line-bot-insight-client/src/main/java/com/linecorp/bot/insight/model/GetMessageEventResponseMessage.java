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
 * GetMessageEventResponseMessage
 */

@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record GetMessageEventResponseMessage (
/**
    * Bubble&#39;s serial number.
    */
    
    @JsonProperty("seq")
    Integer seq,
/**
    * Number of times the bubble was displayed.
    */
    
    @JsonProperty("impression")
    Long impression,
/**
    * Number of times audio or video in the bubble started playing.
    */
    
    @JsonProperty("mediaPlayed")
    Long mediaPlayed,
/**
    * Number of times audio or video in the bubble started playing and was played 25% of the total time.
    */
    
    @JsonProperty("mediaPlayed25Percent")
    Long mediaPlayed25Percent,
/**
    * Number of times audio or video in the bubble started playing and was played 50% of the total time.
    */
    
    @JsonProperty("mediaPlayed50Percent")
    Long mediaPlayed50Percent,
/**
    * Number of times audio or video in the bubble started playing and was played 75% of the total time.
    */
    
    @JsonProperty("mediaPlayed75Percent")
    Long mediaPlayed75Percent,
/**
    * Number of times audio or video in the bubble started playing and was played 100% of the total time.
    */
    
    @JsonProperty("mediaPlayed100Percent")
    Long mediaPlayed100Percent,
/**
    * Number of users that started playing audio or video in the bubble.
    */
    
    @JsonProperty("uniqueMediaPlayed")
    Long uniqueMediaPlayed,
/**
    * Number of users that started playing audio or video in the bubble and played 25% of the total time.
    */
    
    @JsonProperty("uniqueMediaPlayed25Percent")
    Long uniqueMediaPlayed25Percent,
/**
    * Number of users that started playing audio or video in the bubble and played 50% of the total time.
    */
    
    @JsonProperty("uniqueMediaPlayed50Percent")
    Long uniqueMediaPlayed50Percent,
/**
    * Number of users that started playing audio or video in the bubble and played 75% of the total time.
    */
    
    @JsonProperty("uniqueMediaPlayed75Percent")
    Long uniqueMediaPlayed75Percent,
/**
    * Number of users that started playing audio or video in the bubble and played 100% of the total time.
    */
    
    @JsonProperty("uniqueMediaPlayed100Percent")
    Long uniqueMediaPlayed100Percent

)  {


    public static class Builder {
private Integer seq;
private Long impression;
private Long mediaPlayed;
private Long mediaPlayed25Percent;
private Long mediaPlayed50Percent;
private Long mediaPlayed75Percent;
private Long mediaPlayed100Percent;
private Long uniqueMediaPlayed;
private Long uniqueMediaPlayed25Percent;
private Long uniqueMediaPlayed50Percent;
private Long uniqueMediaPlayed75Percent;
private Long uniqueMediaPlayed100Percent;


        public Builder() {
        }

public Builder seq(Integer seq) {
            this.seq = seq;
            return this;
        }
public Builder impression(Long impression) {
            this.impression = impression;
            return this;
        }
public Builder mediaPlayed(Long mediaPlayed) {
            this.mediaPlayed = mediaPlayed;
            return this;
        }
public Builder mediaPlayed25Percent(Long mediaPlayed25Percent) {
            this.mediaPlayed25Percent = mediaPlayed25Percent;
            return this;
        }
public Builder mediaPlayed50Percent(Long mediaPlayed50Percent) {
            this.mediaPlayed50Percent = mediaPlayed50Percent;
            return this;
        }
public Builder mediaPlayed75Percent(Long mediaPlayed75Percent) {
            this.mediaPlayed75Percent = mediaPlayed75Percent;
            return this;
        }
public Builder mediaPlayed100Percent(Long mediaPlayed100Percent) {
            this.mediaPlayed100Percent = mediaPlayed100Percent;
            return this;
        }
public Builder uniqueMediaPlayed(Long uniqueMediaPlayed) {
            this.uniqueMediaPlayed = uniqueMediaPlayed;
            return this;
        }
public Builder uniqueMediaPlayed25Percent(Long uniqueMediaPlayed25Percent) {
            this.uniqueMediaPlayed25Percent = uniqueMediaPlayed25Percent;
            return this;
        }
public Builder uniqueMediaPlayed50Percent(Long uniqueMediaPlayed50Percent) {
            this.uniqueMediaPlayed50Percent = uniqueMediaPlayed50Percent;
            return this;
        }
public Builder uniqueMediaPlayed75Percent(Long uniqueMediaPlayed75Percent) {
            this.uniqueMediaPlayed75Percent = uniqueMediaPlayed75Percent;
            return this;
        }
public Builder uniqueMediaPlayed100Percent(Long uniqueMediaPlayed100Percent) {
            this.uniqueMediaPlayed100Percent = uniqueMediaPlayed100Percent;
            return this;
        }


        public GetMessageEventResponseMessage build() {
            return new GetMessageEventResponseMessage(
seq,impression,mediaPlayed,mediaPlayed25Percent,mediaPlayed50Percent,mediaPlayed75Percent,mediaPlayed100Percent,uniqueMediaPlayed,uniqueMediaPlayed25Percent,uniqueMediaPlayed50Percent,uniqueMediaPlayed75Percent,uniqueMediaPlayed100Percent
            );
        }
    }
}
