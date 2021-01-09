/*
 * Copyright 2016 LINE Corporation
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

package com.linecorp.bot.model.event.source;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;

@JsonTypeName("user")
@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = UserSource.UserSourceBuilder.class)
public class UserSource implements Source {
    @JsonPOJOBuilder(withPrefix = "")
    public static class UserSourceBuilder {
        // Providing builder instead of public constructor. Class body is filled by lombok.
    }

    String userId;

    @Override
    public String getSenderId() {
        return userId;
    }
}
