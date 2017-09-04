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
import lombok.Value;

/**
 * <p>
 * When this action is tapped, a postback event is returned via webhook with the date and time selected by the user from the date and time selection dialog.
 * </p>
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("datetimepicker")
@JsonInclude(Include.NON_NULL)
public class DatetimePickerAction implements Action {
    /**
     * Label for the action<br>
     * Required for templates other than image carousel. Max: 20 characters
     * Optional for image carousel templates. Max: 12 characters.
     */
    private final String label;

    /**
     * String returned via webhook in the postback.data property of the postback event<br>
     * Max: 300 characters
     */
    private final String data;

    /**
     * Action mode
     * date: Pick date
     * time: Pick time
     * datetime: Pick date and time
     */
    private final String mode;

    /**
     * Initial value of date or time
     */
    private final String initial;

    /**
     * Largest date or time value that can be selected.
     * Must be greater than the min value.
     */
    private final String max;

    /**
     * Smallest date or time value that can be selected.
     * Must be less than the max value.
     */
    private final String min;

    /**
     * Create new instance.
     * @param label Label for the action. Max: 20 characters.
     * @param data String returned via webhook in the postback.data property of the postback event.
     *              Max: 300 characters.
     * @param mode Action mode. One of 'date', 'time', 'datetime'.
     * @param initial Initial value of date or time (optional)
     * @param max Largest date or time value that can be selected.
     *             Must be greater than the min value. (optional)
     * @param min Smallest date or time value that can be selected.
     *             Must be less than the max value. (optional)
     */
    @JsonCreator
    public DatetimePickerAction(
            @JsonProperty("label") String label,
            @JsonProperty("data") String data,
            @JsonProperty("mode") String mode,
            @JsonProperty("initial") String initial,
            @JsonProperty("max") String max,
            @JsonProperty("min") String min) {
        this.label = label;
        this.data = data;
        this.mode = mode;
        this.initial = initial;
        this.max = max;
        this.min = min;
    }

    public DatetimePickerAction(String label, String data, String mode) {
        this(label, data, mode, null, null, null);
    }
}
