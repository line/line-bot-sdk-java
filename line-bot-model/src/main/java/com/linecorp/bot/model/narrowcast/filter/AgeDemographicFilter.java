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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Getter;
import lombok.Value;

@Value
@JsonTypeName("age")
public class AgeDemographicFilter implements DemographicFilter {
    private final Age gte;
    private final Age lt;

    @JsonCreator
    public AgeDemographicFilter(Age gte, Age lt) {
        this.gte = gte;
        this.lt = lt;
    }

    public enum Age {
        @JsonProperty("age_15")
        AGE_15(15),
        @JsonProperty("age_20")
        AGE_20(20),
        @JsonProperty("age_25")
        AGE_25(25),
        @JsonProperty("age_30")
        AGE_30(30),
        @JsonProperty("age_35")
        AGE_35(35),
        @JsonProperty("age_40")
        AGE_40(40),
        @JsonProperty("age_45")
        AGE_45(45),
        @JsonProperty("age_50")
        AGE_50(50);

        @Getter
        private final int age;

        Age(int age) {
            this.age = age;
        }
    }
}
