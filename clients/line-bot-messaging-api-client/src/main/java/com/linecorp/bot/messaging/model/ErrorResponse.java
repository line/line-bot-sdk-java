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
 * ErrorResponse
 *
 * @see <a href="https://developers.line.biz/en/reference/messaging-api/#error-responses">
 *     Documentation</a>
 */
@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record ErrorResponse(
    /** Message containing information about the error. */
    @JsonProperty("message") String message,
    /**
     * An array of error details. If the array is empty, this property will not be included in the
     * response.
     */
    @JsonProperty("details") List<ErrorDetail> details,
    /** Array of sent messages. */
    @JsonProperty("sentMessages") List<SentMessage> sentMessages) {

  public static class Builder {
    private String message;
    private List<ErrorDetail> details;
    private List<SentMessage> sentMessages;

    public Builder(String message) {

      this.message = message;
    }

    public Builder details(List<ErrorDetail> details) {
      this.details = details;
      return this;
    }

    public Builder sentMessages(List<SentMessage> sentMessages) {
      this.sentMessages = sentMessages;
      return this;
    }

    public ErrorResponse build() {
      return new ErrorResponse(message, details, sentMessages);
    }
  }
}
