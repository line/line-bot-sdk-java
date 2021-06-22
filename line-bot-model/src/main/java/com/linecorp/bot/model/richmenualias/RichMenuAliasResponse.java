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

package com.linecorp.bot.model.richmenualias;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.richmenualias.RichMenuAliasResponse.RichMenuAliasResponseBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = RichMenuAliasResponseBuilder.class)
public class RichMenuAliasResponse {
    String richMenuAliasId;
    String richMenuId;

    @JsonPOJOBuilder(withPrefix = "")
    public static class RichMenuAliasResponseBuilder {
        // Filled by lombok
    }
}

