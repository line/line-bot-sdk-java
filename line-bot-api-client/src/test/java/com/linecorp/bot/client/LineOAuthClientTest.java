/*
 * Copyright 2019 LINE Corporation
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
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;

import java.net.URI;
import java.util.concurrent.CompletionException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;

import com.linecorp.bot.model.oauth.ChannelAccessTokenException;
import com.linecorp.bot.model.oauth.IssueChannelAccessTokenRequest;
import com.linecorp.bot.model.oauth.IssueChannelAccessTokenResponse;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class LineOAuthClientTest {
    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String ISSUE_TOKEN_RESPONSE_JSON =
            "{\"access_token\":\"accessToken\",\"expires_in\":30,\"token_type\":\"Bearer\"}";
    private static final IssueChannelAccessTokenResponse ISSUE_TOKEN_RESPONSE =
            IssueChannelAccessTokenResponse.builder()
                                           .expiresInSecs(30)
                                           .accessToken("accessToken")
                                           .build();

    private MockWebServer mockWebServer;
    private LineOAuthClient target;

    @Before
    public void setUp() {
        mockWebServer = new MockWebServer();
        final String apiEndPoint = mockWebServer.url("/").toString();
        target = LineOAuthClient.builder()
                                .apiEndPoint(URI.create(apiEndPoint))
                                .build();
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    public void issueToken() throws Exception {

        mockWebServer.enqueue(new MockResponse()
                                      .setResponseCode(200)
                                      .setBody(ISSUE_TOKEN_RESPONSE_JSON));

        // Do
        final IssueChannelAccessTokenResponse actualResponse =
                target.issueChannelToken(IssueChannelAccessTokenRequest.builder()
                                                                       .clientId("clientId")
                                                                       .clientSecret("clientSecret")
                                                                       .build())
                      .join();

        // Verify
        final RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertThat(recordedRequest.getPath())
                .isEqualTo("/v2/oauth/accessToken");
        assertThat(recordedRequest.getBody().readUtf8())
                .isEqualTo("grant_type=client_credentials&client_id=clientId&client_secret=clientSecret");
        assertThat(actualResponse).isEqualTo(ISSUE_TOKEN_RESPONSE);
    }

    @Test
    public void issueTokenError() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                                      .setResponseCode(400)
                                      .setBody(OBJECT_MAPPER.writeValueAsString(ImmutableMap.of(
                                              "error", "error",
                                              "error_description", "errorDetail"
                                      ))));

        // Do
        final CompletionException actualException =
                catchThrowableOfType(() -> target.issueChannelToken(IssueChannelAccessTokenRequest
                                                                            .builder()
                                                                            .clientId("clientId")
                                                                            .clientSecret("clientSecret")
                                                                            .build()).join(),
                                     CompletionException.class);

        // Verify
        final RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertThat(recordedRequest.getPath())
                .isEqualTo("/v2/oauth/accessToken");
        assertThat(recordedRequest.getBody().readUtf8())
                .isEqualTo("grant_type=client_credentials&client_id=clientId&client_secret=clientSecret");
        assertThat(actualException).hasCauseInstanceOf(ChannelAccessTokenException.class);
        final ChannelAccessTokenException e = (ChannelAccessTokenException) actualException.getCause();
        assertThat(e.getError()).isEqualTo("error");
        assertThat(e.getErrorDescription()).isEqualTo("errorDetail");
    }

    @Test
    public void revokeToken() throws Exception {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        // Do
        target.revokeChannelToken("accessToken").join();

        // Verify
        final RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertThat(recordedRequest.getPath())
                .isEqualTo("/v2/oauth/revoke");
        assertThat(recordedRequest.getBody().readUtf8())
                .isEqualTo("access_token=accessToken");
    }
}
