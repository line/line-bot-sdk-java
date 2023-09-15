/*
 * Copyright 2023 LINE Corporation
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

import static com.linecorp.bot.client.utils.ClientBuilder.buildClient;
import static com.linecorp.bot.client.utils.FollowerCountCheckerForNarrowcast.checkNumberOfFollowersForNarrowcast;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import com.linecorp.bot.client.base.Result;
import com.linecorp.bot.client.utils.IntegrationTestSettings;
import com.linecorp.bot.client.utils.IntegrationTestSettingsLoader;
import com.linecorp.bot.messaging.client.MessagingApiClient;
import com.linecorp.bot.messaging.model.AgeDemographic;
import com.linecorp.bot.messaging.model.AgeDemographicFilter;
import com.linecorp.bot.messaging.model.AppTypeDemographic;
import com.linecorp.bot.messaging.model.AppTypeDemographicFilter;
import com.linecorp.bot.messaging.model.Filter;
import com.linecorp.bot.messaging.model.GenderDemographic;
import com.linecorp.bot.messaging.model.GenderDemographicFilter;
import com.linecorp.bot.messaging.model.NarrowcastProgressResponse;
import com.linecorp.bot.messaging.model.NarrowcastRequest;
import com.linecorp.bot.messaging.model.TextMessage;

public class NarrowcastIntegrationTest {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(NarrowcastIntegrationTest.class);
    private MessagingApiClient target;

    @BeforeAll
    public static void beforeAll() throws IOException {
        checkNumberOfFollowersForNarrowcast();
    }

    @BeforeEach
    public void setUp() throws IOException {
        IntegrationTestSettings settings = IntegrationTestSettingsLoader.load();
        target = buildClient(settings, MessagingApiClient.builder(settings.token()));
    }

    @Test
    public void narrowcastGender() throws Exception {
        testNarrowcast(new NarrowcastRequest(
                List.of(new TextMessage("Narrowcast test(gender=male)")),
                null,
                new Filter(
                        new GenderDemographicFilter(Collections.singletonList(GenderDemographic.MALE))
                ), null, false));
    }

    @Test
    public void narrowcastAge() throws Exception {
        testNarrowcast(new NarrowcastRequest(
                List.of(new TextMessage("Narrowcast test(Age)")),
                null,
                new Filter(
                        new AgeDemographicFilter(AgeDemographic.AGE_15, AgeDemographic.AGE_40)
                ),
                null,
                false));
    }

    @Test
    public void narrowcastAppType() throws Exception {
        testNarrowcast(new NarrowcastRequest(List.of(new TextMessage("Narrowcast test(AppType)")),
                null,
                new Filter(
                        new AppTypeDemographicFilter(Collections.singletonList(AppTypeDemographic.IOS))
                ), null, false));
    }

    private void testNarrowcast(NarrowcastRequest narrowcast) throws Exception {
        Result<Object> response = target.narrowcast(null, narrowcast).get();
        log.info("Narrowcast response={}", response);
        for (int i = 0; i < 10; i++) {
            NarrowcastProgressResponse progressResponse = target.getNarrowcastProgress(
                    response.requestId()).get().body();
            log.info("Progress={}", progressResponse);
            log.info("Progress response={}", progressResponse);
            if (progressResponse.phase() == NarrowcastProgressResponse.Phase.SUCCEEDED
                    || progressResponse.phase() == NarrowcastProgressResponse.Phase.FAILED) {
                break;
            }
            Thread.sleep(1000);
        }
    }
}
