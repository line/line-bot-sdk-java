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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import com.linecorp.bot.internal.DatetimePickerFieldSerializer;

import lombok.Builder;
import lombok.Value;

/**
 * DatetimePickerAction.
 *
 * <p>When this action is tapped, a postback event is returned via webhook
 * with the date and time selected by the user from the date and time selection dialog.
 *
 * <p>To create instance of this class, use
 * {@link DatetimePickerAction.OfLocalTime#builder()},
 * {@link DatetimePickerAction.OfLocalDate#builder()}
 * or {@link DatetimePickerAction.OfLocalDatetime#builder()}.
 *
 * @see <a href="https://developers.line.biz/en/docs/messaging-api/actions/#datetime-picker-action">//developers.line.biz/en/docs/messaging-api/actions/#datetime-picker-action</a>
 */
@JsonTypeName("datetimepicker")
@JsonInclude(Include.NON_NULL)
public interface DatetimePickerAction<T extends Temporal> extends Action {
    /**
     * DatetimePicker pick mode.
     */
    enum Mode {
        @JsonProperty("date")
        DATE,
        @JsonProperty("time")
        TIME,
        @JsonProperty("datetime")
        DATETIME,
    }

    @Value
    @Builder
    class OfLocalDate implements DatetimePickerAction<LocalDate> {
        @Override
        public Mode getMode() {
            return Mode.DATE;
        }

        String label;
        String data;
        LocalDate initial;
        LocalDate min;
        LocalDate max;
    }

    @Value
    @Builder
    class OfLocalTime implements DatetimePickerAction<LocalTime> {
        @Override
        public Mode getMode() {
            return Mode.TIME;
        }

        String label;
        String data;
        LocalTime initial;
        LocalTime min;
        LocalTime max;
    }

    @Value
    @Builder
    class OfLocalDatetime implements DatetimePickerAction<LocalDateTime> {
        @Override
        public Mode getMode() {
            return Mode.DATETIME;
        }

        String label;
        String data;
        LocalDateTime initial;
        LocalDateTime min;
        LocalDateTime max;
    }

    /**
     * Label for the action.
     *
     * <ul>
     * <li>Required for templates other than image carousel. Max: 20 characters</li>
     * <li>Optional for image carousel templates. Max: 12 characters.</li>
     * </ul>
     */
    @Override
    String getLabel();

    /**
     * String returned via webhook in the postback.data property of the postback event.
     *
     * <p>Max: 300 characters
     */
    String getData();

    /**
     * Action mode.
     *
     * @see Mode
     */
    Mode getMode();

    /**
     * Initial value of date or time.
     */
    @JsonSerialize(using = DatetimePickerFieldSerializer.class)
    T getInitial();

    /**
     * Largest date or time value that can be selected.
     * Must be greater than the min value.
     */
    @JsonSerialize(using = DatetimePickerFieldSerializer.class)
    T getMax();

    /**
     * Smallest date or time value that can be selected.
     * Must be less than the max value.
     */
    @JsonSerialize(using = DatetimePickerFieldSerializer.class)
    T getMin();

    /**
     * Create new instance.
     *
     * @param label Label for the action. Max: 20 characters.
     * @param data String returned via webhook in the postback.data property of the postback event.
     *         Max: 300 characters.
     * @param mode Action mode. One of 'date', 'time', 'datetime'.
     * @param initial Initial value of date or time (optional)
     * @param max Largest date or time value that can be selected.
     *         Must be greater than the min value. (optional)
     * @param min Smallest date or time value that can be selected.
     *         Must be less than the max value. (optional)
     *
     * @deprecated This method intended to used in deserialize json object.
     *     Use {@link DatetimePickerAction.OfLocalTime#builder()},
     *     {@link DatetimePickerAction.OfLocalDate#builder()}
     *     or {@link DatetimePickerAction.OfLocalDatetime#builder()}.
     */
    @JsonCreator
    @Deprecated
    static DatetimePickerAction<?> parse(
            @JsonProperty("label") final String label,
            @JsonProperty("data") final String data,
            @JsonProperty("mode") final Mode mode,
            @JsonProperty("initial") final String initial,
            @JsonProperty("max") final String max,
            @JsonProperty("min") final String min) {
        switch (mode) {
            case DATE:
                return OfLocalDate
                        .builder()
                        .label(label)
                        .data(data)
                        .initial(initial != null ? LocalDate.parse(initial) : null)
                        .max(initial != null ? LocalDate.parse(max) : null)
                        .min(initial != null ? LocalDate.parse(min) : null)
                        .build();

            case TIME:
                return OfLocalTime
                        .builder()
                        .label(label)
                        .data(data)
                        .initial(initial != null ? LocalTime.parse(initial) : null)
                        .max(initial != null ? LocalTime.parse(max) : null)
                        .min(initial != null ? LocalTime.parse(min) : null)
                        .build();

            case DATETIME:
                return OfLocalDatetime
                        .builder()
                        .label(label)
                        .data(data)
                        .initial(initial != null ? LocalDateTime.parse(initial) : null)
                        .max(initial != null ? LocalDateTime.parse(max) : null)
                        .min(initial != null ? LocalDateTime.parse(min) : null)
                        .build();
        }

        throw new UnsupportedOperationException("Unknown mode: " + mode);
    }
}
