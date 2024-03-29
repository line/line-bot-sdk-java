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
package com.linecorp.bot.webhook.model;



import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/** BeaconContent */
@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record BeaconContent(
    /** Hardware ID of the beacon that was detected */
    @JsonProperty("hwid") String hwid,
    /** Type of beacon event. */
    @JsonProperty("type") Type type,
    /** Device message of beacon that was detected. */
    @JsonProperty("dm") String dm) {

  /** Type of beacon event. */
  public enum Type {
    @JsonProperty("enter")
    ENTER,
    @JsonProperty("banner")
    BANNER,
    @JsonProperty("stay")
    STAY,

    @JsonEnumDefaultValue
    UNDEFINED;
  }

  public static class Builder {
    private String hwid;
    private Type type;
    private String dm;

    public Builder(String hwid, Type type) {

      this.hwid = hwid;

      this.type = type;
    }

    public Builder dm(String dm) {
      this.dm = dm;
      return this;
    }

    public BeaconContent build() {
      return new BeaconContent(hwid, type, dm);
    }
  }
}
