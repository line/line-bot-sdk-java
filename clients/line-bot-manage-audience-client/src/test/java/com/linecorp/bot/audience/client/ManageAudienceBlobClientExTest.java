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

import static com.github.tomakehurst.wiremock.client.WireMock.aMultipart;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.linecorp.bot.audience.model.CreateAudienceGroupResponse.Permission.READ_WRITE;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.github.tomakehurst.wiremock.WireMockServer;

import com.linecorp.bot.audience.model.CreateAudienceGroupResponse;
import com.linecorp.bot.client.base.Result;
import com.linecorp.bot.client.base.UploadFile;

@ExtendWith(MockitoExtension.class)
@Timeout(5)
public class ManageAudienceBlobClientExTest {
    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private WireMockServer wireMockServer;
    private ManageAudienceBlobClient target;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());

        target = ManageAudienceBlobClient.builder("MY_OWN_TOKEN")
                .apiEndPoint(URI.create(wireMockServer.baseUrl()))
                .build();
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testAddUserIdsToAudience() {
        stubFor(put(urlEqualTo("/v2/bot/audienceGroup/upload/byFile"))
                .withMultipartRequestBody(aMultipart()
                        .withName("file")
                        .withBody(equalTo("foobar"))
                        .withHeader("Content-Disposition", containing("filename=\"file\""))
                ).willReturn(
                aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("{}")));

        // Do
        target.addUserIdsToAudience(4649L, "Hello", UploadFile.fromString("foobar", "text/plain"))
                .join();

        // Verify
        verify(
                putRequestedFor(
                        urlEqualTo("/v2/bot/audienceGroup/upload/byFile")
                ).withHeader("Authorization", equalTo("Bearer MY_OWN_TOKEN"))
        );
    }

    @Test
    public void testCreateAudienceForUploadingUserIds() {
        stubFor(post(urlPathTemplate("/v2/bot/audienceGroup/upload/byFile"))
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("content-type", "application/json")
                                .withBody("""
                                        {
                                          "audienceGroupId": 1234567890123,
                                          "createRoute": "MESSAGING_API",
                                          "type": "UPLOAD",
                                          "description": "audienceGroupName_01",
                                          "created": 1613700237,
                                          "permission": "READ_WRITE",
                                          "expireTimestamp": 1629252237,
                                          "isIfaAudience": false
                                        }""")));
        // Do
        UploadFile body = UploadFile.fromByteArray(
                "HELLO_FILE".getBytes(StandardCharsets.UTF_8),
                "text/plain");
        Result<CreateAudienceGroupResponse> result = target.createAudienceForUploadingUserIds(
                null, false, "Test Audience", body).join();

        // Verify
        assertThat(result).isNotNull();
        assertThat(result.body()).isNotNull();
        assertThat(result.body().audienceGroupId()).isEqualTo(1234567890123L);
        assertThat(result.body().description()).isEqualTo("audienceGroupName_01");
        assertThat(result.body().permission()).isEqualTo(READ_WRITE);
        assertThat(result.body().expireTimestamp()).isEqualTo(1629252237L);
        assertThat(result.body().isIfaAudience()).isFalse();
    }
}
