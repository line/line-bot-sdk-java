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


package com.linecorp.bot.insight.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import java.net.URI;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.github.tomakehurst.wiremock.WireMockServer;

import com.linecorp.bot.client.base.Result;
import com.linecorp.bot.insight.model.GetNumberOfFollowersResponse;
import com.linecorp.bot.insight.model.GetStatisticsPerUnitResponse;

/**
* API tests for InsightClient
*/
@Timeout(5)
public class InsightClientExTest {
    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private WireMockServer wireMockServer;
    private InsightClient target;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());


        target = InsightClient.builder("MY_OWN_TOKEN")
            .apiEndPoint(URI.create(wireMockServer.baseUrl()))
            .build();
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void getStatisticsPerUnit() {
        stubFor(get(urlPathTemplate("/v2/bot/insight/message/event/aggregation"))
                .withQueryParam("customAggregationUnit", equalTo("a_bc_de"))
                .withQueryParam("from", equalTo("20210301"))
                .withQueryParam("to", equalTo("20210331"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("""
                                {
                                  "overview": {
                                    "uniqueImpression": 40,
                                    "uniqueClick": 30,
                                    "uniqueMediaPlayed": 25
                                  },
                                  "messages": [
                                    {
                                      "seq": 1,
                                      "impression": 42,
                                      "uniqueImpression": 40,
                                      "mediaPlayed": 30,
                                      "uniqueMediaPlayed": 25
                                    }
                                  ],
                                  "clicks": [
                                    {
                                      "seq": 1,
                                      "url": "https://developers.line.biz/",
                                      "click": 35,
                                      "uniqueClick": 25
                                    },
                                    {
                                      "seq": 1,
                                      "url": "https://lineapiusecase.com/",
                                      "click": 29
                                    }
                                  ]
                                }""")));
        // Do
        Result<GetStatisticsPerUnitResponse> result = target.getStatisticsPerUnit(
                "a_bc_de", "20210301", "20210331").join();

        // Verify
        assertThat(result).isNotNull();
        GetStatisticsPerUnitResponse responseBody = requireNonNull(result.body());
        assertThat(responseBody.overview().uniqueImpression()).isEqualTo(40);
        assertThat(responseBody.overview().uniqueClick()).isEqualTo(30);
        assertThat(responseBody.overview().uniqueMediaPlayed100Percent()).isNull();
        assertThat(responseBody.messages()).hasSize(1);
        assertThat(responseBody.messages().get(0).seq()).isEqualTo(1);
        assertThat(responseBody.messages().get(0).impression()).isEqualTo(42);
        assertThat(responseBody.messages().get(0).mediaPlayed()).isEqualTo(30);
        assertThat(responseBody.clicks()).hasSize(2);
        assertThat(responseBody.clicks().get(0).seq()).isEqualTo(1);
        assertThat(responseBody.clicks().get(0).url()).isEqualTo("https://developers.line.biz/");
        assertThat(responseBody.clicks().get(0).uniqueClickOfRequest()).isNull();

        verify(getRequestedFor(urlPathEqualTo("/v2/bot/insight/message/event/aggregation"))
                .withQueryParam("customAggregationUnit", equalTo("a_bc_de"))
                .withQueryParam("from", equalTo("20210301"))
                .withQueryParam("to", equalTo("20210331")));
    }

    @Test
    public void getNumberOfFollowers() {
        stubFor(get(urlPathTemplate("/v2/bot/insight/followers"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("""
                                {
                                  "status": "ready",
                                  "followers": 1000,
                                  "targetedReaches": 900,
                                  "blocks": 100
                                }""")));
        // Do
        Result<GetNumberOfFollowersResponse> result = target.getNumberOfFollowers(null).join();
        // Verify
        assertThat(result).isNotNull();
        GetNumberOfFollowersResponse responseBody = requireNonNull(result.body());
        assertThat(responseBody.status()).isEqualTo(GetNumberOfFollowersResponse.Status.READY);
        assertThat(responseBody.followers()).isEqualTo(1000);
        assertThat(responseBody.targetedReaches()).isEqualTo(900);
        assertThat(responseBody.blocks()).isEqualTo(100);
    }
}
