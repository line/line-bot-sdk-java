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

package com.linecorp.bot.model.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Value;

/**
 * When this action is tapped, the string in the text field is sent as a message from the user.
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("message")
public class MessageAction implements Action {
    /**
     *Label for the action. Max 20 characters.
     */
    private final String label;

    /**
     * Text sent when the action is performed
     * Max: 300 characters
     */
    private final String text;

    @JsonCreator
    public MessageAction(
            @JsonProperty("label") String label,
            @JsonProperty("text") String text) {
        this.label = label;
        this.text = text;
    }
}
