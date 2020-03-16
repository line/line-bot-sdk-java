/*
 * Copyright 2020 LINE Corporation
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

package com.linecorp.bot.client;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.linecorp.bot.model.Narrowcast;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.narrowcast.filter.AgeDemographicFilter;
import com.linecorp.bot.model.narrowcast.filter.AgeDemographicFilter.Age;
import com.linecorp.bot.model.narrowcast.filter.AppTypeDemographicFilter;
import com.linecorp.bot.model.narrowcast.filter.AppTypeDemographicFilter.AppType;
import com.linecorp.bot.model.narrowcast.filter.Filter;
import com.linecorp.bot.model.narrowcast.filter.GenderDemographicFilter;
import com.linecorp.bot.model.narrowcast.filter.GenderDemographicFilter.Gender;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.model.response.NarrowcastProgressResponse;
import com.linecorp.bot.model.response.NarrowcastProgressResponse.Phase;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NarrowcastIntegrationTest {
    private LineMessagingClient target;

    @Before
    public void setUp() throws IOException {
        IntegrationTestSettings settings = IntegrationTestSettingsLoader.load();
        target = LineMessagingClientFactory.create(settings);
    }

    @Test
    public void narrowcastGender() throws Exception {
        testNarrowcast(new Narrowcast(new TextMessage("Narrowcast test(gender=male)"), new Filter(
                new GenderDemographicFilter(Gender.MALE)
        )));
    }

    @Test
    public void narrowcastAge() throws Exception {
        testNarrowcast(new Narrowcast(new TextMessage("Narrowcast test(Age)"), new Filter(
                new AgeDemographicFilter(Age.AGE_15, Age.AGE_40)
        )));
    }

    @Test
    public void narrowcastAppType() throws Exception {
        testNarrowcast(new Narrowcast(new TextMessage("Narrowcast test(AppType)"), new Filter(
                new AppTypeDemographicFilter(AppType.IOS)
        )));
    }

    private void testNarrowcast(Narrowcast narrowcast) throws Exception {
        BotApiResponse response = target.narrowcast(narrowcast).get();
        log.info("Narrowcast response={}", response);
        for (int i = 0; i < 10; i++) {
            NarrowcastProgressResponse progressResponse = target.getNarrowcastProgress(
                    response.getRequestId()).get();
            log.info("Progress={}", progressResponse);
            log.info("Progress response={}", progressResponse);
            if (progressResponse.getPhase() == Phase.SUCCEEDED
                || progressResponse.getPhase() == Phase.FAILED) {
                break;
            }
            Thread.sleep(1000);
        }
    }
}
