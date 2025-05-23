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

package com.linecorp.bot.audience.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import java.net.URI;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.github.tomakehurst.wiremock.WireMockServer;

import com.linecorp.bot.audience.model.AddAudienceToAudienceGroupRequest;
import com.linecorp.bot.audience.model.Audience;

@ExtendWith(MockitoExtension.class)
@Timeout(5)
public class ManageAudienceClientExTest {
    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private WireMockServer wireMockServer;
    private ManageAudienceClient target;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());

        target = ManageAudienceClient.builder("MY_OWN_TOKEN")
                .apiEndPoint(URI.create(wireMockServer.baseUrl()))
                .build();
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void addAudienceToAudienceGroup() {
        stubFor(put(urlEqualTo("/v2/bot/audienceGroup/upload")).willReturn(
                aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("{}")));

        // Do
        target.addAudienceToAudienceGroup(new AddAudienceToAudienceGroupRequest(
                        400L,
                        "This is dummy",
                        Arrays.asList(
                                new Audience("AAAA"),
                                new Audience("BBBB")
                        )
                ))
                .join();

        // Verify
        verify(
                putRequestedFor(
                        urlEqualTo("/v2/bot/audienceGroup/upload")
                )
                        .withRequestBody(equalTo("""
                                {"audienceGroupId":400,
                                "uploadDescription":"This is dummy",
                                "audiences":[{"id":"AAAA"},{"id":"BBBB"}]}""".replace("\n", "")))
        );
    }
}
