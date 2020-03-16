/*
 * Copyright 2020 LINE Corporation
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
 *
 */

package com.linecorp.bot.model.narrowcast.filter;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(GenderDemographicFilter.class),
        @JsonSubTypes.Type(AgeDemographicFilter.class),
        @JsonSubTypes.Type(AppTypeDemographicFilter.class),
        @JsonSubTypes.Type(AreaDemographicFilter.class),
        @JsonSubTypes.Type(SubscriptionPeriodDemographicFilter.class),
        @JsonSubTypes.Type(OperatorDemographicFilter.class)
})
public interface DemographicFilter {
}
