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
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * LocationMessage
 *
 * @see <a href="https://developers.line.biz/en/reference/messaging-api/#location-message">
 *     Documentation</a>
 */
@JsonTypeName("location")
@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record LocationMessage(
    /** Get quickReply */
    @JsonProperty("quickReply") QuickReply quickReply,
    /** Get sender */
    @JsonProperty("sender") Sender sender,
    /** Get title */
    @JsonProperty("title") String title,
    /** Get address */
    @JsonProperty("address") String address,
    /** Get latitude */
    @JsonProperty("latitude") Double latitude,
    /** Get longitude */
    @JsonProperty("longitude") Double longitude)
    implements Message {

  public LocationMessage(String title, String address, Double latitude, Double longitude) {
    this(null, null, title, address, latitude, longitude);
  }

  public static class Builder {
    private QuickReply quickReply;
    private Sender sender;
    private String title;
    private String address;
    private Double latitude;
    private Double longitude;

    public Builder(String title, String address, Double latitude, Double longitude) {

      this.title = title;

      this.address = address;

      this.latitude = latitude;

      this.longitude = longitude;
    }

    public Builder quickReply(QuickReply quickReply) {
      this.quickReply = quickReply;
      return this;
    }

    public Builder sender(Sender sender) {
      this.sender = sender;
      return this;
    }

    public LocationMessage build() {
      return new LocationMessage(quickReply, sender, title, address, latitude, longitude);
    }
  }
}
