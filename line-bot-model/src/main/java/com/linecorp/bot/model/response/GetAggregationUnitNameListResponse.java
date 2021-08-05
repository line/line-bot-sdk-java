/*
 * Copyright 2021 LINE Corporation
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

package com.linecorp.bot.model.response;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.response.GetAggregationUnitNameListResponse.GetAggregationUnitNameListResponseBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = GetAggregationUnitNameListResponseBuilder.class)
public class GetAggregationUnitNameListResponse {
    List<String> customAggregationUnits;
    /**
     * A continuation token to get the next array of unit names. Non-null only when there are remaining
     * aggregation units that were not returned in customAggregationUnits in the original request.
     */
    @Nullable
    String next;

    @JsonPOJOBuilder(withPrefix = "")
    public static class GetAggregationUnitNameListResponseBuilder {
    }
}
