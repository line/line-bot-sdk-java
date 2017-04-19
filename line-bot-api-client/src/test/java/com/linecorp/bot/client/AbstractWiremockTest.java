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

import org.junit.After;
import org.junit.Before;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import com.linecorp.bot.model.error.ErrorResponse;

import lombok.SneakyThrows;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public abstract class AbstractWiremockTest {
    public static final int ASYNC_TEST_TIMEOUT = 1_000;
    private static final ObjectWriter ERROR_RESPONSE_READER = new ObjectMapper().writerFor(ErrorResponse.class);

    static {
        SLF4JBridgeHandler.install();
        SLF4JBridgeHandler.removeHandlersForRootLogger();
    }

    protected MockWebServer mockWebServer;
    protected LineMessagingClient lineMessagingClient;

    @Before
    public void setUpWireMock() {
        mockWebServer = new MockWebServer();
        lineMessagingClient = createLineMessagingClient(mockWebServer);
    }

    @After
    public void shutDownWireMock() throws Exception {
        mockWebServer.shutdown();
    }

    @SneakyThrows
    public void mocking(final int responseCode, final ErrorResponse errorResponse) {
        mockWebServer
                .enqueue(new MockResponse()
                                 .setResponseCode(responseCode)
                                 .setBody(ERROR_RESPONSE_READER.writeValueAsString(errorResponse)));
    }

    protected LineMessagingClientImpl createLineMessagingClient(final MockWebServer mockWebServer) {
        LineMessagingService lineMessagingService =
                LineMessagingServiceBuilder.create("token")
                                           .apiEndPoint("http://localhost:" + mockWebServer.getPort())
                                           .build();
        return new LineMessagingClientImpl(lineMessagingService);
    }
}
