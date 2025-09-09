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
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.github.tomakehurst.wiremock.WireMockServer;

import com.linecorp.bot.client.base.Result;
import com.linecorp.bot.liff.model.AddLiffAppRequest;
import com.linecorp.bot.liff.model.AddLiffAppResponse;
import com.linecorp.bot.liff.model.GetAllLiffAppsResponse;
import com.linecorp.bot.liff.model.LiffView;
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
    private LiffClient target;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());

        target = LiffClient.builder("MY_OWN_TOKEN")
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

        // Do
        Result<GetAllLiffAppsResponse> result = target.liffV1AppsGet().join();

        // Verify
        assertThat(result).isNotNull();
        assertThat(result.body()).isNotNull();
        verify(getRequestedFor(urlPathTemplate("/liff/v1/apps")));
    }

    @Test
    public void liffV1AppsLiffIdDeleteTest() {
        stubFor(delete(urlPathTemplate("/liff/v1/apps/{liffId}")).willReturn(
                aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("{}")));

        String liffId = "test-liff-id";
        // Do
        target.liffV1AppsLiffIdDelete(liffId).join();

        // Verify
        verify(deleteRequestedFor(urlPathTemplate("/liff/v1/apps/{liffId}"))
                .withPathParam("liffId", equalTo(liffId)));
    }

    @Test
    public void liffV1AppsLiffIdPutTest() {
        stubFor(put(urlPathTemplate("/liff/v1/apps/{liffId}")).willReturn(
                aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("{}")));

        String liffId = "test-liff-id2";
        // Do
        target.liffV1AppsLiffIdPut(liffId, new UpdateLiffAppRequest.Builder().build()).join();

        // Verify
        verify(putRequestedFor(urlPathTemplate("/liff/v1/apps/{liffId}"))
                .withPathParam("liffId", equalTo(liffId)));
    }

    @Test
    public void liffV1AppsPostTest() {
        stubFor(post(urlPathTemplate("/liff/v1/apps")).willReturn(
                aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("{}")));

        Result<AddLiffAppResponse> response = target.liffV1AppsPost(
                new AddLiffAppRequest.Builder(
                        new LiffView.Builder(
                                LiffView.Type.COMPACT,
                                URI.create("https://example.com")
                        ).build()
                ).build()
        ).join();

        assertThat(response).isNotNull();
        assertThat(response.body()).isNotNull();
        verify(
                postRequestedFor(urlPathTemplate("/liff/v1/apps"))
                        .withRequestBody(equalToJson("""
                                {
                                "view": {
                                    "type": "compact",
                                    "url": "https://example.com"
                                  }
                                }"""))
        );
    }

}
