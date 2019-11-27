/*
 * Copyright 2019 LINE Corporation
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

package com.linecorp.bot.internal;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import com.linecorp.bot.model.action.DatetimePickerAction;

/**
 * Formatter for {@link DatetimePickerAction} format.
 *
 * @see <a href="https://developers.line.biz/en/docs/messaging-api/actions/#datetime-picker-action">//developers.line.biz/en/docs/messaging-api/actions/#datetime-picker-action</a>
 */
public class DatetimePickerFieldSerializer extends JsonSerializer<Temporal> {
    // no ":ss"
    private static final DateTimeFormatter DATETIMEPICKER_LOCAL_TIME =
            DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATETIMEPICKER_LOCAL_DATETIME =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    public void serialize(final Temporal value, final JsonGenerator gen, final SerializerProvider serializers)
            throws IOException, JsonProcessingException {
        gen.writeString(serialize(value));
    }

    String serialize(final Temporal value) {
        if (value instanceof LocalTime) {
            return DATETIMEPICKER_LOCAL_TIME.format(value);
        } else if (value instanceof LocalDate) {
            return DateTimeFormatter.ISO_LOCAL_DATE.format(value);
        } else if (value instanceof LocalDateTime) {
            return DATETIMEPICKER_LOCAL_DATETIME.format(value);
        } else {
            throw new IllegalArgumentException("Illegal value type: " + value.getClass());
        }
    }
}
