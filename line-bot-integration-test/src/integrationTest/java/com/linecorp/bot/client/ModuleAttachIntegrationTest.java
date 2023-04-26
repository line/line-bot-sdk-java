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

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.linecorp.bot.client.base.LineClientJsonParseException;
import com.linecorp.bot.client.utils.IntegrationTestSettings;
import com.linecorp.bot.client.utils.IntegrationTestSettingsLoader;
import com.linecorp.bot.moduleattach.client.LineModuleAttachClient;
import com.linecorp.bot.moduleattach.client.LineModuleAttachClientException;

public class ModuleAttachIntegrationTest {
    private LineModuleAttachClient target;

    @BeforeEach
    public void setUp() throws IOException {
        IntegrationTestSettings settings = IntegrationTestSettingsLoader.load();
        target = LineModuleAttachClient.builder(settings.token())
                .failOnUnknownProperties(settings.failOnUnknownProperties())
                .build();
    }

    @Test
    public void createAudienceGroup() {
        // https://developers.line.biz/en/reference/partner-docs/#link-attach-by-operation-module-channel-provider
        assertThatThrownBy(() -> {
            target
                    .attachModule(
                            "authorization_code",
                            "1234567890abcde",
                            "https://example.com/auth?key=value",
                            "ayjtZgTunh96nHCvgLEiXzqVQOOC0SwMRs39bh1l5dx",
                            "1234567890",
                            "1234567890abcdefghij1234567890ab",
                            "JP",
                            "@linedevelopers",
                            "message%3Asend%20message%3Areceive",
                            "premium"
                    ).get();
        }).isInstanceOf(ExecutionException.class)
                .cause()
                .isInstanceOf(LineModuleAttachClientException.class)
                .hasFieldOrPropertyWithValue("code", 400);
    }
}
