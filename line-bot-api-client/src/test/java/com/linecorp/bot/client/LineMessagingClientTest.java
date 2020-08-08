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

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.profile.UserProfileResponse;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class LineMessagingClientTest {
    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private MockWebServer mockWebServer;
    private LineMessagingClient target;

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        final String apiEndPoint =
                "http://" + mockWebServer.getHostName() + ':' + mockWebServer.getPort()
                + "/CanContainsRelative/";
        target = LineMessagingClient
                .builder("SECRET")
                .apiEndPoint(URI.create(apiEndPoint))
                .build();
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
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

        mockWebServer.enqueue(new MockResponse()
                                      .setResponseCode(200)
                                      .setBody(new ObjectMapper()
                                                       .writeValueAsString(profileResponseMock)));

        // Do
        final UserProfileResponse actualResponse =
                target.getProfile("USER_TOKEN").get();

        // Verify
        final RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertThat(recordedRequest.getPath())
                .isEqualTo("/CanContainsRelative/v2/bot/profile/USER_TOKEN");
        assertThat(actualResponse).isEqualTo(profileResponseMock);
    }
}
