/*
 * Copyright 2019 LINE Corporation
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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.response.MessageQuotaResponse.MessageQuotaResponseBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = MessageQuotaResponseBuilder.class)
public class MessageQuotaResponse {
    /**
     * Indicates whether a target limit is set or not.
     */
    QuotaType type;

    /**
     * The target limit for additional messages in the current month. This property is set when the type
     * property has a value of limited.
     */
    long value;

    public enum QuotaType {
        /**
         * Indicates that a target limit is not set.
         */
        none,

        /**
         * Indicates that a target limit is set.
         */
        limited
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class MessageQuotaResponseBuilder {
    }
}
