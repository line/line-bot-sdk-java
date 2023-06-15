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

package com.linecorp.bot.messaging.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

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
import com.linecorp.bot.messaging.model.TextMessage;

@ExtendWith(MockitoExtension.class)
@Timeout(5)
public class MessagingApiBlobClientExTest {
    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private WireMockServer wireMockServer;
    private MessagingApiBlobClient target;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());

        target = MessagingApiBlobClient.builder("MY_OWN_TOKEN")
                .apiEndPoint(URI.create(wireMockServer.baseUrl()))
                .build();
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void headerInterceptor() {
        stubFor(get(urlEqualTo("/v2/bot/message/aaaa/content")).willReturn(
                aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/octet-stream")
                        .withBody("JPG]]]]]]")));

        // Do
        target.getMessageContent("aaaa")
                .join();

        // Verify
        verify(
                getRequestedFor(
                        urlEqualTo("/v2/bot/message/aaaa/content")
                ).withHeader("Authorization", equalTo("Bearer MY_OWN_TOKEN"))
        );
    }

    @Test
    public void getMessageContent() throws IOException {
        stubFor(get(urlEqualTo("/v2/bot/message/aaaa/content")).willReturn(
                aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/octet-stream")
                        .withHeader("x-line-request-id", "ppp")
                        .withBody("JPG]]]]]]")));

        // Do
        Result<BlobContent> result = target.getMessageContent("aaaa")
                .join();

        // Verify
        assertThat(result.requestId()).isEqualTo("ppp");
        assertThat(requireNonNull(result.body()).string()).isEqualTo("JPG]]]]]]");
    }

    @Test
    public void setRichMenuImageTest() {
        stubFor(post(urlPathTemplate("/v2/bot/richmenu/{richMenuId}/content"))
                .withHeader("content-type", containing("image/jpeg")).willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("content-type", "application/json")
                                .withBody("{}")));

        String richMenuId = Arranger.some(String.class, Map.of(
                "message", () -> new TextMessage("hello"),
                "recipient", () -> null,
                "filter", () -> null));
        UploadFile body = UploadFile.fromString("HELLO_FILE", "image/jpeg");

        target.setRichMenuImage(richMenuId, body).join();
    }
}
