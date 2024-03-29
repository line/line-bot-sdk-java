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
 * https://openapi-generator.tech Do not edit the class manually.
 */
package com.linecorp.bot.messaging.model;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Limit of the Narrowcast
 *
 * @see <a href="https://developers.line.biz/en/reference/messaging-api/#send-narrowcast-message">
 *     Documentation</a>
 */
@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record Limit(
    /**
     * The maximum number of narrowcast messages to send. Use this parameter to limit the number of
     * narrowcast messages sent. The recipients will be chosen at random. minimum: 1
     */
    @JsonProperty("max") Integer max,
    /**
     * If true, the message will be sent within the maximum number of deliverable messages. The
     * default value is &#x60;false&#x60;. Targets will be selected at random.
     */
    @JsonProperty("upToRemainingQuota") Boolean upToRemainingQuota) {

  public static class Builder {
    private Integer max;
    private Boolean upToRemainingQuota;

    public Builder() {}

    public Builder max(Integer max) {
      this.max = max;
      return this;
    }

    public Builder upToRemainingQuota(Boolean upToRemainingQuota) {
      this.upToRemainingQuota = upToRemainingQuota;
      return this;
    }

    public Limit build() {
      return new Limit(max, upToRemainingQuota);
    }
  }
}
