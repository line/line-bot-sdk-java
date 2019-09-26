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

import java.util.OptionalInt;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;

/**
 * Internal implementation of {@link AgeRange}.
 */
@Value
@Builder
class AgeRangeImpl implements AgeRange {
    private static final String JSON_UNKNOWN_VALUE = "unknown";
    private static final Pattern VALUE_PATTERN = Pattern.compile("from(?<from>\\d+)(?:to(?<to>\\d+))?");

    @Default
    OptionalInt from = empty();
    @Default
    OptionalInt to = empty();

    static AgeRange valueOf(final String value) {
        if (value.equals(JSON_UNKNOWN_VALUE)) {
            return AgeRange.UNKNOWN;
        }

        final AgeRangeImplBuilder builder = builder();

        final Matcher matcher = VALUE_PATTERN.matcher(value);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Unexpected format: " + value);
        }

        builder.from(OptionalInt.of(Integer.parseInt(matcher.group("from"))));

        if (matcher.group("to") != null) {
            builder.to(OptionalInt.of(Integer.parseInt(matcher.group("to"))));
        }

        return builder.build();
    }

    @Override
    public String getJsonRawValue() {
        if (AgeRange.UNKNOWN.equals(this)) {
            return JSON_UNKNOWN_VALUE;
        }

        final StringBuilder sb = new StringBuilder();
        if (from.isPresent()) {
            sb.append("from" + from.getAsInt());
        }
        if (to.isPresent()) {
            sb.append("to" + to.getAsInt());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return getJsonRawValue();
    }
}
