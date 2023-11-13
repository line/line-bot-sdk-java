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
 * BroadcastRequest
 *
 * @see <a href="https://developers.line.biz/en/reference/messaging-api/#send-broadcast-message">
 *     Documentation</a>
 */
@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record BroadcastRequest(
    /** List of Message objects. */
    @JsonProperty("messages") List<Message> messages,
    /**
     * &#x60;true&#x60;: The user doesn’t receive a push notification when a message is sent.
     * &#x60;false&#x60;: The user receives a push notification when the message is sent (unless
     * they have disabled push notifications in LINE and/or their device). The default value is
     * false.
     */
    @JsonProperty("notificationDisabled") Boolean notificationDisabled) {

  public static class Builder {
    private List<Message> messages;
    private Boolean notificationDisabled;

    public Builder(List<Message> messages) {

      this.messages = messages;
    }

    public Builder notificationDisabled(Boolean notificationDisabled) {
      this.notificationDisabled = notificationDisabled;
      return this;
    }

    public BroadcastRequest build() {
      return new BroadcastRequest(messages, notificationDisabled);
    }
  }
}
