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

package com.linecorp.bot.model.event.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Value;

/**
 * Message content for text
 */
@Value
@JsonTypeName("text")
public class TextMessageContent implements MessageContent {
    private final String id;
    /**
     * Message text
     */
    private final String text;

    @JsonCreator
    public TextMessageContent(
            @JsonProperty("id") String id,
            @JsonProperty("text") String text) {
        this.id = id;
        this.text = text;
    }
}
