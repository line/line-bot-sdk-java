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
import java.util.List;

/**
 * PushMessageRequest
 *
 * @see <a href="https://developers.line.biz/en/reference/messaging-api/#send-push-message">
 *     Documentation</a>
 */
@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record PushMessageRequest(
    /** ID of the receiver. */
    @JsonProperty("to") String to,
    /** List of Message objects. */
    @JsonProperty("messages") List<Message> messages,
    /**
     * &#x60;true&#x60;: The user doesn’t receive a push notification when a message is sent.
     * &#x60;false&#x60;: The user receives a push notification when the message is sent (unless
     * they have disabled push notifications in LINE and/or their device). The default value is
     * false.
     */
    @JsonProperty("notificationDisabled") Boolean notificationDisabled,
    /**
     * List of aggregation unit name. Case-sensitive. This functions can only be used by corporate
     * users who have submitted the required applications.
     */
    @JsonProperty("customAggregationUnits") List<String> customAggregationUnits) {

  public static class Builder {
    private String to;
    private List<Message> messages;
    private Boolean notificationDisabled;
    private List<String> customAggregationUnits;

    public Builder(String to, List<Message> messages) {

      this.to = to;

      this.messages = messages;
    }

    public Builder notificationDisabled(Boolean notificationDisabled) {
      this.notificationDisabled = notificationDisabled;
      return this;
    }

    public Builder customAggregationUnits(List<String> customAggregationUnits) {
      this.customAggregationUnits = customAggregationUnits;
      return this;
    }

    public PushMessageRequest build() {
      return new PushMessageRequest(to, messages, notificationDisabled, customAggregationUnits);
    }
  }
}
