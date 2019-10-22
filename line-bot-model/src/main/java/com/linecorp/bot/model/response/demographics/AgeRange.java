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

import java.util.OptionalInt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Key of {@link AgeTile}.
 *
 * <p>Age range contains lower bound `from` and upper bound `to`.
 * Both of them are inclusive boundary.
 *
 * </p>And un-identified friends aggregated in {@link #UNKNOWN} key.
 * {@code UNKNOWN} key is immutable singleton instance.
 * You can check equal to {@code UNKNOWN} by {@code ==} or {@link #equals}.
 *
 * @see AgeTile#getAge()
 */
public interface AgeRange {
    /**
     * Unknown identifier of AgeRange.
     *
     * <p>This instance is immutable and singleton.
     * You can check equal to {@code UNKNOWN} by {@code ==} or {@link #equals}.
     */
    AgeRange UNKNOWN = AgeRangeImpl.builder().build();

    @JsonCreator
    static AgeRange valueOf(final String value) {
        return AgeRangeImpl.valueOf(value);
    }

    /**
     * Start of age range. Inclusive.
     *
     * @return Start of age range. Inclusive. null iff UNKNOWN range.
     */
    OptionalInt getFrom();

    /**
     * End of age range. Inclusive.
     *
     * @return End of age range. Exclusive. null if UNKNOWN or last (&lt;∞) range .
     */
    OptionalInt getTo();

    @JsonValue
    String getJsonRawValue();
}
