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
 * GetAggregationUnitUsageResponse
 *
 * @see <a
 *     href="https://developers.line.biz/en/reference/messaging-api/#get-number-of-units-used-this-month">
 *     Documentation</a>
 */
@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record GetAggregationUnitUsageResponse(
    /** Number of aggregation units used this month. */
    @JsonProperty("numOfCustomAggregationUnits") Long numOfCustomAggregationUnits) {

  public static class Builder {
    private Long numOfCustomAggregationUnits;

    public Builder(Long numOfCustomAggregationUnits) {

      this.numOfCustomAggregationUnits = numOfCustomAggregationUnits;
    }

    public GetAggregationUnitUsageResponse build() {
      return new GetAggregationUnitUsageResponse(numOfCustomAggregationUnits);
    }
  }
}
