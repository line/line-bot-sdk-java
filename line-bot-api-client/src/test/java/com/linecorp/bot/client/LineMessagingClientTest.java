/*
 * Copyright 2016 LINE Corporation
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

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import com.linecorp.bot.model.profile.UserProfileResponse;

public class LineMessagingClientTest {
    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private WireMockServer wireMockServer;
    private LineMessagingClient target;

    @BeforeEach
    public void setUp() throws Exception {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());

        final String apiEndPoint = wireMockServer.url("/CanContainsRelative/");
        target = LineMessagingClient
                .builder("SECRET")
                .apiEndPoint(URI.create(apiEndPoint))
                .build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        wireMockServer.stop();
    }

    @Test
    public void relativeRequestTest() throws Exception {
        final UserProfileResponse profileResponseMock = UserProfileResponse
                .builder()
                .displayName("name")
                .userId("userId")
                .pictureUrl(URI.create("https://line.me/picture_url"))
                .statusMessage("Status message")
                .language("en")
                .build();

        stubFor(
                get(urlEqualTo("/CanContainsRelative/v2/bot/profile/USER_TOKEN")).willReturn(
                        aResponse().withStatus(200)
                                   .withBody(new ObjectMapper().writeValueAsString(profileResponseMock))
                ));

        // Do
        final UserProfileResponse actualResponse =
                target.getProfile("USER_TOKEN").get();

        // Verify
        verify(getRequestedFor(urlEqualTo("/CanContainsRelative/v2/bot/profile/USER_TOKEN")));
        assertThat(actualResponse).isEqualTo(profileResponseMock);
    }
}
