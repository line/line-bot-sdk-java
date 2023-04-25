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

import static org.assertj.core.api.Assumptions.assumeThat;

import java.util.List;

public record IntegrationTestSettings(
        Boolean enabled,
        String token,
        String apiEndpoint,
        String userId,
        List<String> audienceIfas,
        String retargetingRequestId,
        boolean failOnUnknownProperties,
        // only for oauth test
        String channelId,
        // only for oauth test
        String channelSecret,
        // only for oauth test
        // 1. Generate assertion signing key.
        //    see: https://developers.line.biz/en/docs/messaging-api/generate-json-web-token/#create-an-assertion-signing-key
        // 2. Register **public key** to the developer center. And get kid.
        // 3. Write your **private key** to your integration_test_settings.yml.
        String privateKeyJWK,
        String kid
) {
    public String userId() {
        assumeThat(userId)
                .as("userId in integration_test_settings.yml is not null")
                .isNotNull();
        return userId;
    }

    public List<String> audienceIfas() {
        assumeThat(audienceIfas)
                .isNotNull()
                .isNotEmpty();
        return audienceIfas;
    }

    public String retargetingRequestId() {
        assumeThat(retargetingRequestId)
                .isNotNull();
        return retargetingRequestId;
    }

    public String privateKeyJWK() {
        assumeThat(privateKeyJWK)
                .as("privateKeyJWK in integration_test_settings.yml is not null")
                .isNotNull();
        return privateKeyJWK;
    }

    public String channelId() {
        assumeThat(channelId)
                .as("channelId in integration_test_settings.yml is not null")
                .isNotNull();
        return channelId;
    }

    public String channelSecret() {
        assumeThat(channelSecret)
                .as("channelId in integration_test_settings.yml is not null")
                .isNotNull();
        return channelSecret;
    }

    public String kid() {
        assumeThat(kid)
                .as("kid in integration_test_settings.yml is not null")
                .isNotNull();
        return kid;
    }
}
