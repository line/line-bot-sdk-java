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


package com.linecorp.bot.moduleattach.client;

import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import java.net.URI;
import java.util.List;


import com.linecorp.bot.client.base.Result;
import com.linecorp.bot.moduleattach.model.AttachModuleResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.github.tomakehurst.wiremock.WireMockServer;

/**
* API tests for LineModuleAttachClient
*/
@Timeout(5)
public class LineModuleAttachClientExTest {
    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private WireMockServer wireMockServer;
    private LineModuleAttachClient target;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());


        target = LineModuleAttachClient.builder("MY_OWN_TOKEN")
            .apiEndPoint(URI.create(wireMockServer.baseUrl()))
            .build();
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void attachModuleTest() {
        stubFor(post(urlPathTemplate("/module/auth/v1/token"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("""
                                {
                                  "bot_id": "U111...",
                                  "scopes": ["message:send", "message:receive"]
                                }""")));

        // Do
        Result<AttachModuleResponse> result = target.attachModule(
                "authorization_code",
                "test-code",
                "https://example2.com/callback?key=value",
                null, null, null, null, null,
                "message:send message:receive",
                null
                )
            .join();

        // Verify
        assertThat(result).isNotNull();
        AttachModuleResponse responseBody = requireNonNull(result.body());
        assertThat(responseBody.botId()).isEqualTo("U111...");
        assertThat(responseBody.scopes()).isEqualTo(List.of("message:send", "message:receive"));
        verify(postRequestedFor(urlPathTemplate("/module/auth/v1/token"))
                .withRequestBody(containing("grant_type=authorization_code"))
                .withRequestBody(containing("code=test-code"))
                .withRequestBody(containing("redirect_uri=https%3A%2F%2Fexample2.com%2Fcallback%3Fkey%3Dvalue"))
                .withRequestBody(containing("scope=message%3Asend+message%3Areceive"))
                );
    }
}
