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

import static java.lang.ClassLoader.getSystemResourceAsStream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.util.StreamUtils.copyToByteArray;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.response.demographics.AppTypeTile.AppType;
import com.linecorp.bot.model.response.demographics.SubscriptionPeriod.WellKnownValue;
import com.linecorp.bot.model.testutil.TestUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetFriendsDemographicsResponseTest {
    private static final ObjectMapper OBJECT_MAPPER = TestUtil.objectMapperWithProductionConfiguration(true);

    @Test
    public void reconstructTest() throws IOException {
        final byte[] bytes = copyToByteArray(getSystemResourceAsStream("friends-demographics.json"));

        // Do
        final GetFriendsDemographicsResponse deserialized =
                OBJECT_MAPPER.readValue(bytes, GetFriendsDemographicsResponse.class);

        log.info("De-serialized: {}", deserialized);

        // Verify
        assertThat(deserialized.isAvailable()).isTrue();
        assertThat(deserialized.getAges())
                .containsExactly(ageTile("unknown", 37.6),
                                 ageTile("from50", 17.3));
        assertThat(deserialized.getAppTypes())
                .containsExactly(osTile(AppType.IOS, 62.4),
                                 osTile(AppType.ANDROID, 27.7),
                                 osTile(AppType.OTHERS, 9.9));
        assertThat(deserialized.getAreas())
                .containsExactly(areaTile("unknown", 42.9),
                                 areaTile("徳島", 2.9));
        assertThat(deserialized.getAges())
                .containsExactly(ageTile("unknown", 37.6),
                                 ageTile("from50", 17.3));
        assertThat(deserialized.getSubscriptionPeriods())
                .containsExactly(subscriptionPeriodTile(WellKnownValue.OVER365DAYS, 96.4),
                                 subscriptionPeriodTile(WellKnownValue.WITHIN365DAYS, 1.9),
                                 subscriptionPeriodTile(WellKnownValue.WITHIN180DAYS, 1.2),
                                 subscriptionPeriodTile(WellKnownValue.WITHIN90DAYS, 0.5),
                                 subscriptionPeriodTile(WellKnownValue.WITHIN30DAYS, 0.1),
                                 subscriptionPeriodTile(WellKnownValue.WITHIN7DAYS, 0.0));
    }

    @Test
    public void testEmptyJsonDontCreateNullField() throws IOException {
        // Do
        final GetFriendsDemographicsResponse deserialized =
                OBJECT_MAPPER.readValue("{}", GetFriendsDemographicsResponse.class);

        log.info("De-serialized: {}", deserialized);

        // Verify
        assertThat(deserialized)
                .hasNoNullFieldsOrProperties();
    }

    private static AreaTile areaTile(final String area, final double percentage) {
        return AreaTile.builder().area(area).percentage(percentage).build();
    }

    private static AppTypeTile osTile(final AppType appType, final double percentage) {
        return AppTypeTile.builder().appType(appType).percentage(percentage).build();
    }

    private static AgeTile ageTile(final String age, final double percentage) {
        return AgeTile.builder().age(AgeRangeImpl.valueOf(age)).percentage(percentage).build();
    }

    private SubscriptionPeriodTile subscriptionPeriodTile(
            final SubscriptionPeriod subscriptionPeriod,
            double percentage) {
        return SubscriptionPeriodTile
                .builder().subscriptionPeriod(subscriptionPeriod).percentage(percentage).build();
    }
}
