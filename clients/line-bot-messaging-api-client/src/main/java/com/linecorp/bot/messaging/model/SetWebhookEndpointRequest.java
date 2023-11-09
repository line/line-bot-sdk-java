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
import java.net.URI;

/**
 * SetWebhookEndpointRequest
 *
 * @see <a href="https://developers.line.biz/en/reference/messaging-api/#set-webhook-endpoint-url">
 *     Documentation</a>
 */
@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record SetWebhookEndpointRequest(
    /** A valid webhook URL. */
    @JsonProperty("endpoint") URI endpoint) {

  public static class Builder {
    private URI endpoint;

    private boolean endpoint$set;

    public Builder() {}

    public Builder endpoint(URI endpoint) {
      this.endpoint = endpoint;

      this.endpoint$set = true;

      return this;
    }

    public SetWebhookEndpointRequest build() {

      if (!this.endpoint$set) {
        throw new IllegalStateException("'endpoint' must be set for SetWebhookEndpointRequest.");
      }

      return new SetWebhookEndpointRequest(endpoint);
    }
  }
}
