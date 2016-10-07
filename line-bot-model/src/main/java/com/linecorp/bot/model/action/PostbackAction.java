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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Value;

/**
 * <p>
 * When this action is tapped, a postback event is returned via webhook with the specified string in the data field.
 * </p>
 *
 * <p>
 * If you have included the text field, the string in the text field is sent as a message from the user.
 * </p>
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("postback")
@JsonInclude(Include.NON_NULL)
public class PostbackAction implements Action {
    /**
     * Label for the action<br>
     * Max: 20 characters
     */
    private final String label;

    /**
     * String returned via webhook in the postback.data property of the postback event<br>
     * Max: 300 characters
     */
    private final String data;

    /**
     * Text sent when the action is performed<br>
     * Max: 300 characters
     */
    private final String text;

    /**
     * Create new instance.
     * @param label Label for the action. Max: 20 characters.
     * @param data String returned via webhook in the postback.data property of the postback event.
     *              Max: 300 characters.
     * @param text text message (optional)
     */
    @JsonCreator
    public PostbackAction(
            @JsonProperty("label") String label,
            @JsonProperty("data") String data,
            @JsonProperty("text") String text) {
        this.label = label;
        this.data = data;
        this.text = text;
    }

    public PostbackAction(String label, String data) {
        this(label, data, null);
    }
}
