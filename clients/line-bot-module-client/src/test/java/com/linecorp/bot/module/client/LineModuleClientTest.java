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


package com.linecorp.bot.module.client;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static org.assertj.core.api.Assertions.assertThat;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import java.net.URI;

import com.linecorp.bot.client.base.Result;
import com.linecorp.bot.module.model.AcquireChatControlRequest;
import com.linecorp.bot.module.model.DetachModuleRequest;
import com.linecorp.bot.module.model.GetModulesResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.github.tomakehurst.wiremock.WireMockServer;

/**
* API tests for LineModuleClient
*/
@Timeout(5)
public class LineModuleClientTest {
    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private WireMockServer wireMockServer;
    private LineModuleClient target;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());


        target = LineModuleClient.builder("MY_OWN_TOKEN")
            .apiEndPoint(URI.create(wireMockServer.baseUrl()))
            .build();
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void acquireChatControlTest() {
        stubFor(post(urlPathTemplate("/v2/bot/chat/{chatId}/control/acquire")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

        String chatId = "test-chat-id";

        // Do
        target.acquireChatControl(chatId,
                new AcquireChatControlRequest.Builder().build()
        ).join();

        // Verify
        verify(postRequestedFor(urlPathTemplate("/v2/bot/chat/{chatId}/control/acquire"))
                .withPathParam("chatId", equalTo(chatId)));
    }

    @Test
    public void detachModuleTest() {
        stubFor(post(urlPathTemplate("/v2/bot/channel/detach")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

        // Do
        target.detachModule(new DetachModuleRequest.Builder().build()).join();

        // Verify
        verify(postRequestedFor(urlPathTemplate("/v2/bot/channel/detach")));
    }

    @Test
    public void getModulesTest() {
        stubFor(get(urlPathTemplate("/v2/bot/list")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

        final var start = "token";
        final var limit = 20;
        // Do
        Result<GetModulesResponse> response = target.getModules(start, limit).join();

        // Verify
        assertThat(response).isNotNull();
        assertThat(response.body()).isNotNull();
        verify(getRequestedFor(urlPathTemplate("/v2/bot/list"))
                .withQueryParam("start", equalTo(start))
                .withQueryParam("limit", equalTo(String.valueOf(limit))));
    }

    @Test
    public void releaseChatControlTest() {
        stubFor(post(urlPathTemplate("/v2/bot/chat/{chatId}/control/release")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

        String chatId = "test-chat-id";
        // Do
        target.releaseChatControl(chatId).join();

        // Verify
        verify(postRequestedFor(urlPathTemplate("/v2/bot/chat/{chatId}/control/release"))
                .withPathParam("chatId", equalTo(chatId)));
    }
}
