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

package com.linecorp.bot.client.utils;

import static com.linecorp.bot.client.utils.ClientBuilder.buildClient;
import static org.assertj.core.api.Assumptions.assumeThat;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.linecorp.bot.insight.client.InsightClient;
import com.linecorp.bot.insight.model.GetNumberOfFollowersResponse;

public class FollowerCountCheckerForNarrowcast {
    public static void checkNumberOfFollowersForNarrowcast() throws IOException {
        IntegrationTestSettings settings = IntegrationTestSettingsLoader.load();
        InsightClient messagingApiClient = buildClient(settings, InsightClient.builder(settings.token()));
        GetNumberOfFollowersResponse numberOfFollowers = messagingApiClient.getNumberOfFollowers(
                LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        ).join().body();
        Long followers = numberOfFollowers.followers();

        // narrowcast operation requires 100+ followers.
        // https://developers.line.biz/ja/reference/messaging-api/#send-narrowcast-message-restrictions
        assumeThat(followers)
                .as("narrowcast operation requires 100+ followers.")
                .isGreaterThan(100);
    }
}
