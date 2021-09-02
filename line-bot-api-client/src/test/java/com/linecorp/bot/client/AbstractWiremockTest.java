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

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import java.net.URI;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

public abstract class AbstractWiremockTest {
    public static final int ASYNC_TEST_TIMEOUT = 1_000;

    static {
        SLF4JBridgeHandler.install();
        SLF4JBridgeHandler.removeHandlersForRootLogger();
    }

    protected WireMockServer wireMockServer;
    protected LineMessagingClient lineMessagingClient;
    protected LineBlobClient lineBlobClient;
    protected ChannelManagementSyncClient channelManagementSyncClient;

    @BeforeEach
    public void setUpWireMock() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());

        lineMessagingClient = createLineMessagingClient(wireMockServer);
        lineBlobClient = createLineBlobClient(wireMockServer);
        channelManagementSyncClient = createChannelManagementSyncClient(wireMockServer);
    }

    @AfterEach
    public void shutDownWireMock() {
        wireMockServer.stop();
    }

    protected LineMessagingClient createLineMessagingClient(final WireMockServer wireMockServer) {
        return LineMessagingClient.builder("token")
                                  .apiEndPoint(URI.create(wireMockServer.baseUrl()))
                                  .blobEndPoint(URI.create(wireMockServer.baseUrl()))
                                  .build();
    }

    protected LineBlobClient createLineBlobClient(WireMockServer wireMockServer) {
        return LineBlobClient.builder("token")
                             .apiEndPoint(URI.create(wireMockServer.baseUrl()))
                             .build();
    }

    protected ChannelManagementSyncClient createChannelManagementSyncClient(
            final WireMockServer wireMockServer) {
        return ChannelManagementSyncClient
                .builder(() -> "token")
                .apiEndPoint(URI.create(wireMockServer.baseUrl()))
                .build();
    }
}
