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
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import com.linecorp.bot.client.base.Result;
import com.linecorp.bot.client.utils.IntegrationTestSettings;
import com.linecorp.bot.client.utils.IntegrationTestSettingsLoader;
import com.linecorp.bot.insight.client.InsightClient;
import com.linecorp.bot.insight.model.GetMessageEventResponse;
import com.linecorp.bot.insight.model.GetNumberOfFollowersResponse;
import com.linecorp.bot.insight.model.GetNumberOfMessageDeliveriesResponse;
import com.linecorp.bot.messaging.client.MessagingApiClient;
import com.linecorp.bot.messaging.model.Filter;
import com.linecorp.bot.messaging.model.GenderDemographic;
import com.linecorp.bot.messaging.model.GenderDemographicFilter;
import com.linecorp.bot.messaging.model.NarrowcastProgressResponse;
import com.linecorp.bot.messaging.model.NarrowcastRequest;
import com.linecorp.bot.messaging.model.TextMessage;

public class InsightIntegrationTest {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(InsightIntegrationTest.class);
    private MessagingApiClient messagingApiClient;
    private InsightClient insightClient;

    @BeforeEach
    public void setUp() throws IOException {
        IntegrationTestSettings settings = IntegrationTestSettingsLoader.load();
        messagingApiClient = buildClient(settings, MessagingApiClient.builder(settings.token()));
        insightClient = buildClient(settings, InsightClient.builder(settings.token()));
    }

    @Test
    public void testGetMessageEvent() throws Exception {
        checkNumberOfFollowersForNarrowcast();

        // Send narrowcast message.
        Result<Void> result = messagingApiClient.narrowcast(
                null, new NarrowcastRequest(
                        List.of(new TextMessage("Narrowcast test(gender=male)")),
                        null,
                        new Filter(
                                new GenderDemographicFilter(
                                        List.of(
                                                GenderDemographic.MALE
                                        )
                                )
                        ), null, false)).get();
        log.info("Narrowcast response={}", result);

        // Waiting sending process
        for (int i = 0; i < 10; i++) {
            NarrowcastProgressResponse progressResponse = messagingApiClient.getNarrowcastProgress(
                    result.requestId()).get().body();
            log.info("Progress={}", progressResponse);
            log.info("Progress response={}", progressResponse);
            if (progressResponse.phase() == NarrowcastProgressResponse.Phase.SUCCEEDED
                    || progressResponse.phase() == NarrowcastProgressResponse.Phase.FAILED) {
                break;
            }
            Thread.sleep(1000);
        }

        GetMessageEventResponse messageEvent = insightClient.getMessageEvent(
                result.requestId()).get().body();
        log.info("messageEvent={}", messageEvent);
    }

    @Test
    public void getNumberOfMessageDeliveries() throws Exception {
        final GetNumberOfMessageDeliveriesResponse getNumberOfMessageDeliveriesResponse =
                insightClient.getNumberOfMessageDeliveries("20191231").get().body();

        log.info(getNumberOfMessageDeliveriesResponse.toString());
    }

    @Test
    public void getNumberOfFollowers() throws Exception {
        final GetNumberOfFollowersResponse getNumberOfFollowersResponse =
                insightClient.getNumberOfFollowers("20191231").get().body();

        log.info(getNumberOfFollowersResponse.toString());
    }
}
