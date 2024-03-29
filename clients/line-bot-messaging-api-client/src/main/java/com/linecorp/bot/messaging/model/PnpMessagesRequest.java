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
 * PnpMessagesRequest
 *
 * @see <a
 *     href="https://developers.line.biz/en/reference/partner-docs/#send-line-notification-message">
 *     Documentation</a>
 */
@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record PnpMessagesRequest(
    /** Message to be sent. */
    @JsonProperty("messages") List<Message> messages,
    /**
     * Message destination. Specify a phone number that has been normalized to E.164 format and
     * hashed with SHA256.
     */
    @JsonProperty("to") String to,
    /**
     * &#x60;true&#x60;: The user doesn’t receive a push notification when a message is sent.
     * &#x60;false&#x60;: The user receives a push notification when the message is sent (unless
     * they have disabled push notifications in LINE and/or their device). The default value is
     * false.
     */
    @JsonProperty("notificationDisabled") Boolean notificationDisabled) {

  public static class Builder {
    private List<Message> messages;
    private String to;
    private Boolean notificationDisabled;

    public Builder(List<Message> messages, String to) {

      this.messages = messages;

      this.to = to;
    }

    public Builder notificationDisabled(Boolean notificationDisabled) {
      this.notificationDisabled = notificationDisabled;
      return this;
    }

    public PnpMessagesRequest build() {
      return new PnpMessagesRequest(messages, to, notificationDisabled);
    }
  }
}
