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

package com.linecorp.bot.model.narrowcast.recipient;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.narrowcast.recipient.LogicalOperatorRecipient.LogicalOperatorRecipientBuilder;

import lombok.Builder;
import lombok.Value;

/**
 * Use logical AND, OR, and NOT operators to combine multiple recipient objects together. You can specify
 * up to 10 recipient objects per request.
 */
@Value
@Builder
@JsonTypeName("operator")
@JsonDeserialize(builder = LogicalOperatorRecipientBuilder.class)
public class LogicalOperatorRecipient implements Recipient {
    List<Recipient> and;
    List<Recipient> or;
    Recipient not;

    @Override
    public String getType() {
        return "operator";
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class LogicalOperatorRecipientBuilder {
        // Filled by lombok
    }
}
