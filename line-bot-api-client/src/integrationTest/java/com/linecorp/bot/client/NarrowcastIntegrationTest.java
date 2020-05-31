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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Test;

import com.linecorp.bot.client.exception.ConflictException;
import com.linecorp.bot.model.Narrowcast;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.narrowcast.Filter;
import com.linecorp.bot.model.narrowcast.filter.AgeDemographicFilter;
import com.linecorp.bot.model.narrowcast.filter.AgeDemographicFilter.Age;
import com.linecorp.bot.model.narrowcast.filter.AppTypeDemographicFilter;
import com.linecorp.bot.model.narrowcast.filter.AppTypeDemographicFilter.AppType;
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
    public void narrowcastWithRetry() throws Exception {
        UUID retryKey = UUID.randomUUID();
        String message = "Narrowcast with retry at " + LocalDateTime.now().toString();

        Narrowcast request = new Narrowcast(new TextMessage(message),
                                            Filter.builder()
                                                  .build());
        BotApiResponse botApiResponse = target.narrowcast(retryKey, request).get();
        assertThat(botApiResponse).isNotNull();

        assertThatThrownBy(
                () -> target.narrowcast(retryKey, request).get()
        ).isInstanceOf(ExecutionException.class)
         .getCause()
         .isInstanceOf(ConflictException.class);
    }

    @Test
    public void narrowcastGender() throws Exception {
        testNarrowcast(new Narrowcast(new TextMessage("Narrowcast test(gender=male)"),
                                      Filter.builder()
                                            .demographic(
                                                    GenderDemographicFilter
                                                            .builder()
                                                            .oneOf(Collections.singletonList(Gender.MALE))
                                                            .build()
                                            ).build()));
    }

    @Test
    public void narrowcastAge() throws Exception {
        testNarrowcast(new Narrowcast(new TextMessage("Narrowcast test(Age)"),
                                      Filter.builder()
                                            .demographic(
                                                    AgeDemographicFilter
                                                            .builder()
                                                            .gte(Age.AGE_15)
                                                            .lt(Age.AGE_40)
                                                            .build()
                                            ).build()));
    }

    @Test
    public void narrowcastAppType() throws Exception {
        testNarrowcast(new Narrowcast(new TextMessage("Narrowcast test(AppType)"),
                                      Filter.builder()
                                            .demographic(
                                                    AppTypeDemographicFilter
                                                            .builder()
                                                            .oneOf(Collections.singletonList(AppType.IOS))
                                                            .build()
                                            ).build()));
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
