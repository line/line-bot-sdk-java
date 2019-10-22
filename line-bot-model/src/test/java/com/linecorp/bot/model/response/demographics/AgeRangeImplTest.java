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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.testutil.TestUtil;

public class AgeRangeImplTest {
    private static final ObjectMapper OBJECT_MAPPER = TestUtil.objectMapperWithProductionConfiguration(true);

    @Test
    public void testUnknownDeserializeAndSerialize() throws Exception {
        final String target = "\"unknown\"";

        // Do
        final AgeRange ageRange = OBJECT_MAPPER.readValue(target, AgeRange.class);

        // Verify
        assertThat(ageRange).isEqualTo(AgeRange.UNKNOWN);
        assertThat(ageRange.getFrom()).isNotPresent();
        assertThat(ageRange.getTo()).isNotPresent();
        assertThat(OBJECT_MAPPER.writeValueAsString(ageRange)).isEqualTo(target);
    }

    @Test
    public void testFromDeserializeAndSerialize() throws Exception {
        final String target = "\"from50\"";

        // Do
        final AgeRange ageRange = OBJECT_MAPPER.readValue(target, AgeRange.class);

        // Verify
        assertThat(ageRange.getFrom()).hasValue(50);
        assertThat(ageRange.getTo()).isNotPresent();
        assertThat(OBJECT_MAPPER.writeValueAsString(ageRange)).isEqualTo(target);
    }

    @Test
    public void testFromAndToDeserializeAndSerialize() throws Exception {
        final String target = "\"from0to14\"";

        // Do
        final AgeRange ageRange = OBJECT_MAPPER.readValue(target, AgeRange.class);

        // Verify
        assertThat(ageRange.getFrom()).hasValue(0);
        assertThat(ageRange.getTo()).hasValue(14);
        assertThat(OBJECT_MAPPER.writeValueAsString(ageRange)).isEqualTo(target);
    }

    @Test
    public void testIllegalPattern() throws Exception {
        final String target = "\"invalid_format_string\"";

        // Do & Verify
        assertThatThrownBy(() -> {
            OBJECT_MAPPER.readValue(target, AgeRange.class);
        }).hasRootCauseInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("invalid_format_string");
    }
}
