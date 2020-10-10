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
 */

package com.linecorp.bot.model.message.flex.component.box;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.message.flex.component.box.BoxLinearGradient.BoxLinearGradientBuilder;

import lombok.Builder;
import lombok.Value;

@JsonTypeName("linearGradient")
@JsonInclude(Include.NON_NULL)
@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = BoxLinearGradientBuilder.class)
public class BoxLinearGradient implements BoxBackground {

    String angle;

    String startColor;

    String endColor;

    String centerColor;

    String centerPosition;

    @JsonPOJOBuilder(withPrefix = "")
    public static class BoxLinearGradientBuilder {
        // Providing builder instead of public constructor. Class body is filled by lombok.
    }
}
