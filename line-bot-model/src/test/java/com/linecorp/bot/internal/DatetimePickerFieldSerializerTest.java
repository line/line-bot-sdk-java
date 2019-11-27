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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Test;

public class DatetimePickerFieldSerializerTest {
    final DatetimePickerFieldSerializer target = new DatetimePickerFieldSerializer();

    @Test
    public void localDateSerializeTest() throws Exception {
        assertThat(target.serialize(LocalDate.of(2017, 9, 8)))
                .isEqualTo("2017-09-08");
    }

    @Test
    public void localTimeSerializeTest() throws Exception {
        assertThat(target.serialize(LocalTime.of(15, 52)))
                .isEqualTo("15:52");
    }

    @Test
    public void localDatetimeSerializeTest() throws Exception {
        assertThat(target.serialize(LocalDateTime.of(2017, 9, 8, 15, 52)))
                .isEqualToIgnoringCase("2017-09-08T15:52");
    }

    @Test
    public void unsupportedTypeSerializeTest() throws Exception {
        assertThatThrownBy(() -> {
            target.serialize(Instant.now());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
