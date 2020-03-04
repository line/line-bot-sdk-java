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

import java.util.List;

import org.junit.Assume;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.client.IntegrationTestSettings.IntegrationTestSettingsBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@AllArgsConstructor
@Value
@Builder
@JsonDeserialize(builder = IntegrationTestSettingsBuilder.class)
public class IntegrationTestSettings {
    private String token;
    private String endpoint;
    private String userId;
    private List<String> audienceIfas;
    private String retargetingRequestId;
    private boolean failOnUnknownProperties = true;

    public String getUserId() {
        Assume.assumeNotNull(userId);
        return userId;
    }

    public List<String> getAudienceIfas() {
        Assume.assumeNotNull(audienceIfas);
        Assume.assumeFalse(audienceIfas.isEmpty());
        return audienceIfas;
    }

    public String getRetargetingRequestId() {
        Assume.assumeNotNull(retargetingRequestId);
        return retargetingRequestId;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class IntegrationTestSettingsBuilder {
        // filled by lombok.
    }
}
