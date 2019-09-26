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

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.response.demographics.SubscriptionPeriod.UnknownValue;
import com.linecorp.bot.model.response.demographics.SubscriptionPeriod.WellKnownValue;
import com.linecorp.bot.model.testutil.TestUtil;

public class SubscriptionPeriodTest {
    private static final ObjectMapper OBJECT_MAPPER = TestUtil.objectMapperWithProductionConfiguration(true);

    @Test
    public void testWellKnownValueDeserializeAndSerialize() throws Exception {
        final String target = "\"within7days\"";

        // Do
        final SubscriptionPeriod subscriptionPeriod = OBJECT_MAPPER.readValue(target, SubscriptionPeriod.class);

        // Verify
        assertThat(subscriptionPeriod).isEqualTo(WellKnownValue.WITHIN7DAYS);
        assertThat(subscriptionPeriod.getWithin()).hasValue(7);
        assertThat(subscriptionPeriod.getJsonRawValue()).isEqualTo("within7days");
        assertThat(OBJECT_MAPPER.writeValueAsString(subscriptionPeriod)).isEqualTo(target);
    }

    @Test
    public void testUnknownValueDeserializeAndSerialize() throws Exception {
        final String target = "\"within123days\"";

        // Do
        final SubscriptionPeriod subscriptionPeriod = OBJECT_MAPPER.readValue(target, SubscriptionPeriod.class);

        // Verify
        assertThat(subscriptionPeriod).isInstanceOf(UnknownValue.class);
        assertThat(subscriptionPeriod.getWithin()).hasValue(123);
        assertThat(subscriptionPeriod.getJsonRawValue()).isEqualTo("within123days");
        assertThat(subscriptionPeriod.toString()).isEqualTo("within123days");
        assertThat(OBJECT_MAPPER.writeValueAsString(subscriptionPeriod)).isEqualTo(target);
    }

    @Test
    public void testUnknownValueWithUnknownFormatDeserializeAndSerialize() throws Exception {
        final String target = "\"within2years\"";

        // Do
        final SubscriptionPeriod subscriptionPeriod = OBJECT_MAPPER.readValue(target, SubscriptionPeriod.class);

        // Verify
        assertThat(subscriptionPeriod).isInstanceOf(UnknownValue.class);
        assertThat(subscriptionPeriod.getWithin()).isNotPresent();
        assertThat(subscriptionPeriod.getJsonRawValue()).isEqualTo("within2years");
        assertThat(subscriptionPeriod.toString()).isEqualTo("within2years");
        assertThat(OBJECT_MAPPER.writeValueAsString(subscriptionPeriod)).isEqualTo(target);
    }
}
