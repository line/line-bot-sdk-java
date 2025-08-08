/*
 * Copyright 2024 LINE Corporation
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

package com.linecorp.bot.messaging.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.findAll;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.ocadotechnology.gembus.test.Arranger;

import com.linecorp.bot.client.base.BlobContent;
import com.linecorp.bot.client.base.Result;
import com.linecorp.bot.client.base.UploadFile;
import com.linecorp.bot.messaging.model.GetFollowersResponse;
import com.linecorp.bot.messaging.model.TextMessage;

@ExtendWith(MockitoExtension.class)
@Timeout(5)
public class MessagingApiClientExTest {
    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private WireMockServer wireMockServer;
    private MessagingApiClient target;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());

        target = MessagingApiClient.builder("MY_OWN_TOKEN")
                .apiEndPoint(URI.create(wireMockServer.baseUrl()))
                .build();
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void getFollowers() throws IOException {
        stubFor(get(urlPathEqualTo("/v2/bot/followers/ids"))
                .withQueryParam("limit", equalTo(String.valueOf(99)))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withHeader("x-line-request-id", "ppp")
                        .withBody("{\n" +
                                "  \"userIds\": [\"U4af4980629...\", \"U0c229f96c4...\", \"U95afb1d4df...\"],\n" +
                                "  \"next\": \"yANU9IA...\"\n" +
                                "}")));
        // Do
        Result<GetFollowersResponse> result = target.getFollowers(null, 99)
                .join();

        // Verify
        assertThat(result.requestId()).isEqualTo("ppp");
        GetFollowersResponse responseBody = requireNonNull(result.body());
        assertThat(responseBody.userIds()).containsExactly("U4af4980629...", "U0c229f96c4...", "U95afb1d4df...");
        assertThat(responseBody.next()).isEqualTo("yANU9IA...");

        verify(getRequestedFor(urlPathEqualTo("/v2/bot/followers/ids"))
                .withoutFormParam("start")
                .withQueryParam("limit", equalTo(String.valueOf(99))));
    }

    @Test
    public void listCoupon() {
        stubFor(get(urlPathEqualTo("/v2/bot/coupon"))
                .withQueryParam("status", equalTo("RUNNING"))
                .withQueryParam("status", equalTo("CLOSED"))
                .withQueryParam("start",  equalTo("startToken"))
                .withQueryParam("limit",  equalTo(String.valueOf(10)))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withHeader("x-line-request-id", "ppp")
                        .withBody("{\n" +
                                "  \"items\": [{ \"couponId\": \"abc\", \"title\": \"test\" }],\n" +
                                "  \"next\": \"nextToken\"\n" +
                                "}")));

        Set<String> status = Set.of("RUNNING", "CLOSED");
        final var result =
                target.listCoupon(status, "startToken", 10).join();

        assertThat(result.requestId()).isEqualTo("ppp");

        final var responseBody = requireNonNull(result.body());
        assertThat(responseBody.items()).hasSize(1);
        assertThat(responseBody.items().getFirst().couponId()).isEqualTo("abc");
        assertThat(responseBody.next()).isEqualTo("nextToken");

        verify(getRequestedFor(urlPathEqualTo("/v2/bot/coupon"))
                .withQueryParam("status", equalTo("RUNNING"))
                .withQueryParam("status", equalTo("CLOSED"))
                .withQueryParam("start",  equalTo("startToken"))
                .withQueryParam("limit",  equalTo(String.valueOf(10))));

        final var req = findAll(getRequestedFor(urlPathEqualTo("/v2/bot/coupon"))).get(0);
        assertThat(req.getUrl())
                .isEqualTo("/v2/bot/coupon?status=RUNNING&status=CLOSED&start=startToken&limit=10");
    }
}
