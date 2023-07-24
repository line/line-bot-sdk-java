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

package com.linecorp.bot.liff.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.ocadotechnology.gembus.test.Arranger;

import com.linecorp.bot.liff.model.AddLiffAppRequest;
import com.linecorp.bot.liff.model.AddLiffAppResponse;
import com.linecorp.bot.liff.model.GetAllLiffAppsResponse;
import com.linecorp.bot.liff.model.UpdateLiffAppRequest;

/**
 * line-openapi introduce the breaking change in <a href="https://github.com/line/line-openapi/pull/26">line-openapi#26</a>.
 * This test checks this SDK is backward compatible.
 */
@SuppressWarnings("deprecation")
@Timeout(5)
public class LiffClientBackwardCompatibilityTest {
    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private WireMockServer wireMockServer;
    private LiffClient api;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());

        api = LiffClient.builder("MY_OWN_TOKEN")
                .apiEndPoint(URI.create(wireMockServer.baseUrl()))
                .build();
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void liffV1AppsGetTest() {
        stubFor(get(urlPathTemplate("/liff/v1/apps")).willReturn(
                aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("{}")));

        GetAllLiffAppsResponse response = api.liffV1AppsGet().join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void liffV1AppsLiffIdDeleteTest() {
        stubFor(delete(urlPathTemplate("/liff/v1/apps/{liffId}")).willReturn(
                aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("{}")));

        String liffId = Arranger.some(String.class);

        api.liffV1AppsLiffIdDelete(liffId).join().body();

        // TODO: test validations
    }

    @Test
    public void liffV1AppsLiffIdPutTest() {
        stubFor(put(urlPathTemplate("/liff/v1/apps/{liffId}")).willReturn(
                aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("{}")));

        String liffId = Arranger.some(String.class);
        UpdateLiffAppRequest updateLiffAppRequest = Arranger.some(UpdateLiffAppRequest.class);

        api.liffV1AppsLiffIdPut(liffId, updateLiffAppRequest).join().body();

        // TODO: test validations
    }

    @Test
    public void liffV1AppsPostTest() {
        stubFor(post(urlPathTemplate("/liff/v1/apps")).willReturn(
                aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("{}")));

        AddLiffAppRequest addLiffAppRequest = Arranger.some(AddLiffAppRequest.class);

        AddLiffAppResponse response = api.liffV1AppsPost(addLiffAppRequest).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

}
