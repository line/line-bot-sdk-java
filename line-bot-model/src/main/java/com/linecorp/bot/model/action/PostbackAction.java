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

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Value;

/**
 * Postback's Action.
 *
 * <p>When this action is tapped, a postback event is returned via webhook
 * with the specified string in the data field.
 *
 * <p>If you have included the text field, the string in the text field is sent as a message from the user.
 */
@Value
@Builder
@JsonTypeName("postback")
@JsonInclude(Include.NON_NULL)
public class PostbackAction implements Action {
    public enum InputOptionType {
        CLOSE_RICH_MENU("closeRichMenu"),
        OPEN_RICH_MENU("openRichMenu"),
        OPEN_KEYBOARD("openKeyboard"),
        OPEN_VOICE("openVoice");

        private final String name;

        InputOptionType(String name) {
            this.name = name;
        }

        @Override
        @JsonValue
        public String toString() {
            return this.name;
        }
    }

    /**
     * Label for the action.
     *
     * <p>Max: 20 characters
     */
    String label;

    /**
     * String returned via webhook in the postback.data property of the postback event.
     *
     * <p>Max: 300 characters
     */
    String data;

    /**
     * Text sent when the action is performed.
     *
     * <p>Max: 300 characters
     */
    String displayText;

    /**
     * Deprecated. Text sent when the action is performed.
     *
     * <p>Max: 300 characters
     */
    String text;

    /**
     * The display method of such as rich menu based on user action.
     */
    InputOptionType inputOption;

    /**
     * String to be pre-filled in the input field when the keyboard is opened.
     * Valid only when the inputOption property is set to InputOptionType.OPEN_KEYBOARD.
     *
     * <p>Max: 300 characters
     */
    String fillInText;

    /**
     * Create new instance.
     *
     * @param label Label for the action. Max: 20 characters.
     * @param data String returned via webhook in the postback.data property of the postback event.
     *         Max: 300 characters.
     * @param displayText text message (optional)
     *         Can not be used at the same time with text.
     * @param text text message (optional)
     *         Can not be used at the same time with displayText.
     */
    @JsonCreator
    public PostbackAction(
            @JsonProperty("label") String label,
            @JsonProperty("data") String data,
            @JsonProperty("displayText") String displayText,
            @JsonProperty("text") String text,
            @JsonProperty("inputOption") InputOptionType inputOption,
            @JsonProperty("fillInText") String fillinText) {
        this.label = label;
        this.data = data;
        this.displayText = displayText;
        this.text = text;
        this.inputOption = inputOption;
        this.fillInText = fillinText;
    }

    public PostbackAction(String label, String data, String text) {
        this(label, data, null, text, null, null);
    }

    public PostbackAction(String label, String data) {
        this(label, data, null, null, null, null);
    }
}
