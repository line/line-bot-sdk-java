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


package com.linecorp.bot.shop.client;

import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import java.net.URI;


import com.linecorp.bot.shop.model.MissionStickerRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.github.tomakehurst.wiremock.WireMockServer;

/**
* API tests for ShopClient
*/
@Timeout(5)
public class ShopClientTest {
    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private WireMockServer wireMockServer;
    private ShopClient target;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());


        target = ShopClient.builder("MY_OWN_TOKEN")
            .apiEndPoint(URI.create(wireMockServer.baseUrl()))
            .build();
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void missionStickerV3Test() {
        stubFor(post(urlPathTemplate("/shop/v3/mission")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

        String userId = "U111...";
        String productId = "12345";
        String productType = "STICKER";
        Boolean sendPresentMessage = true;
        // Do
        target.missionStickerV3(new MissionStickerRequest.Builder(
            userId, productId, productType, sendPresentMessage
        ).build()).join();

        // Verify
        verify(
                postRequestedFor(urlPathEqualTo("/shop/v3/mission"))
                        .withRequestBody(equalToJson("""
                                {
                                  "to": "U111...",
                                  "productId": "12345",
                                  "productType": "STICKER",
                                  "sendPresentMessage": true
                                }"""))
        );
    }
}
