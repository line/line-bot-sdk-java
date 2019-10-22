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

package com.linecorp.bot.model.response.demographics;

import static java.util.OptionalInt.empty;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Map;
import java.util.OptionalInt;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.Value;

/**
 * Key of {@link SubscriptionPeriodTile}.
 *
 * @see SubscriptionPeriodTile#getSubscriptionPeriod()
 */
public interface SubscriptionPeriod {
    @JsonCreator
    static SubscriptionPeriod valueOf(final String value) {
        final WellKnownValue wellknownValue =
                WellKnownValue.RAW_VALUE_MAP.get(value);
        if (wellknownValue != null) {
            return wellknownValue;
        }
        return new UnknownValue(value);
    }

    OptionalInt getWithin();

    @JsonValue
    String getJsonRawValue();

    @Getter
    enum WellKnownValue implements SubscriptionPeriod {
        WITHIN7DAYS(7, "within7days"),
        WITHIN30DAYS(30, "within30days"),
        WITHIN90DAYS(90, "within90days"),
        WITHIN180DAYS(180, "within180days"),
        WITHIN365DAYS(365, "within365days"),
        OVER365DAYS(null, "over365days"),
        UNKNOWN(null, "unknown");

        WellKnownValue(final Integer within, final String jsonRawValue) {
            if (within == null) {
                this.within = empty();
            } else {
                this.within = OptionalInt.of(within);
            }
            this.jsonRawValue = jsonRawValue;
        }

        private static final Map<String, WellKnownValue> RAW_VALUE_MAP =
                Arrays.stream(values()).collect(
                        toMap(WellKnownValue::getJsonRawValue, identity()));

        private final OptionalInt within;
        private final String jsonRawValue;

        @Override
        public String toString() {
            return jsonRawValue;
        }
    }

    @Value
    class UnknownValue implements SubscriptionPeriod {
        private static final Pattern WITHIN_NUM_PATTERN = Pattern.compile("within(\\d+)days");

        OptionalInt within;
        String jsonRawValue;

        UnknownValue(final String jsonRawValue) {
            final Matcher matcher = WITHIN_NUM_PATTERN.matcher(jsonRawValue);
            if (matcher.matches()) {
                within = OptionalInt.of(Integer.parseInt(matcher.group(1)));
            } else {
                within = empty();
            }
            this.jsonRawValue = jsonRawValue;
        }

        @Override
        public String toString() {
            return jsonRawValue;
        }
    }
}
