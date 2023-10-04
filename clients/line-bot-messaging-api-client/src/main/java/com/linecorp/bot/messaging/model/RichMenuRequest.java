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


package com.linecorp.bot.messaging.model;

import java.time.Instant;

import java.util.Objects;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import com.linecorp.bot.messaging.model.RichMenuArea;
import com.linecorp.bot.messaging.model.RichMenuSize;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 * RichMenuRequest
 */
@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record RichMenuRequest (
    /**
     * Get size
     */

    @JsonProperty("size")
    RichMenuSize size,
    /**
     * &#x60;true&#x60; to display the rich menu by default. Otherwise, &#x60;false&#x60;.
     */

    @JsonProperty("selected")
    Boolean selected,
    /**
     * Name of the rich menu. This value can be used to help manage your rich menus and is not displayed to users.
     */

    @JsonProperty("name")
    String name,
    /**
     * Text displayed in the chat bar
     */

    @JsonProperty("chatBarText")
    String chatBarText,
    /**
     * Array of area objects which define the coordinates and size of tappable areas
     */

    @JsonProperty("areas")
    List<RichMenuArea> areas
)  {





}

