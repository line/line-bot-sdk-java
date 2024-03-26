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

package com.linecorp.bot.oauth.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;

import java.net.URI;
import java.util.concurrent.CompletionException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import com.linecorp.bot.oauth.model.IssueShortLivedChannelAccessTokenResponse;
import com.linecorp.bot.oauth.model.IssueStatelessChannelAccessTokenResponse;

public class ChannelAccessTokenClientExTest {
    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private static final IssueShortLivedChannelAccessTokenResponse ISSUE_SHORT_LIVED_TOKEN_RESPONSE =
            new IssueShortLivedChannelAccessTokenResponse(
                    "accessToken", 30, "Bearer"
            );

    private WireMockServer wireMockServer;
    private ChannelAccessTokenClient target;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());

        target = ChannelAccessTokenClient.builder()
                .apiEndPoint(URI.create(wireMockServer.baseUrl()))
                .build();
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void issueToken() {
        stubFor(post(urlEqualTo("/v2/oauth/accessToken")).willReturn(
                aResponse()
                        .withStatus(200)
                        .withBody("""
                                {
                                "access_token":"accessToken",
                                "expires_in":30,
                                "token_type":"Bearer"
                                }"""
                        )));

        // Do
        final IssueShortLivedChannelAccessTokenResponse actualResponse =
                target.issueChannelToken("client_credentials",
                                "clientId",
                                "clientSecret")
                        .join().body();

        // Verify
        verify(postRequestedFor(
                urlEqualTo("/v2/oauth/accessToken")
        ).withRequestBody(
                WireMock.equalTo(
                        "grant_type=client_credentials&client_id=clientId&client_secret=clientSecret")));
        assertThat(actualResponse).isEqualTo(ISSUE_SHORT_LIVED_TOKEN_RESPONSE);
    }

    @Test
    public void issueTokenError() throws Exception {
        stubFor(post(urlEqualTo("/v2/oauth/accessToken")).willReturn(
                aResponse()
                        .withStatus(400)
                        .withBody("""
                                {"error":"error",
                                "error_description":"errorDetail"}
                                """
                        )
        ));

        // Do
        final CompletionException actualException =
                catchThrowableOfType(() -> target.issueChannelToken("client_credentials",
                                "clientId",
                                "clientSecret").join(),
                        CompletionException.class);

        // Verify
        verify(
                postRequestedFor(urlEqualTo("/v2/oauth/accessToken"))
                        .withRequestBody(equalTo(
                                "grant_type=client_credentials&client_id=clientId&client_secret=clientSecret"
                        )));
        assertThat(actualException).hasCauseInstanceOf(ChannelAccessTokenClientException.class);
        final ChannelAccessTokenClientException e =
                (ChannelAccessTokenClientException) actualException.getCause();
        assertThat(e.getError()).isEqualTo("error");
        assertThat(e.getErrorDescription()).isEqualTo("errorDetail");
    }

    @Test
    public void revokeToken() throws Exception {
        stubFor(post(urlEqualTo("/v2/oauth/revoke")).willReturn(
                aResponse()
                        .withStatus(200)
        ));

        // Do
        target.revokeChannelToken("accessToken").join();

        // Verify
        verify(postRequestedFor(urlEqualTo("/v2/oauth/revoke"))
                .withRequestBody(equalTo("access_token=accessToken")));
    }

    @Test
    public void issueStatelessChannelToken() {
        stubFor(post(urlEqualTo("/oauth2/v3/token")).willReturn(
                aResponse()
                        .withStatus(200)
                        .withBody("""
                                {
                                "access_token":"accessToken",
                                "expires_in":30,
                                "token_type":"Bearer"
                                }"""
                        )));

        // Do
        final IssueStatelessChannelAccessTokenResponse actualResponse =
                target.issueStatelessChannelToken("client_credentials",
                                null,
                                null,
                                "1234",
                                "clientSecret")
                        .join().body();

        // Verify
        verify(postRequestedFor(
                urlEqualTo("/oauth2/v3/token")
        ).withRequestBody(
                WireMock.equalTo(
                        "grant_type=client_credentials&client_id=1234&client_secret=clientSecret")));
        assertThat(actualResponse.tokenType()).isEqualTo("Bearer");
    }
}
