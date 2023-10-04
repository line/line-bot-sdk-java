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

import static org.assertj.core.api.Assertions.assertThat;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import com.linecorp.bot.client.base.BlobContent;
import com.linecorp.bot.client.base.UploadFile;

import java.net.URI;

import java.util.Map;


import com.linecorp.bot.oauth.model.ChannelAccessTokenKeyIdsResponse;
import com.linecorp.bot.oauth.model.ErrorResponse;
import com.linecorp.bot.oauth.model.IssueChannelAccessTokenResponse;
import com.linecorp.bot.oauth.model.IssueShortLivedChannelAccessTokenResponse;
import com.linecorp.bot.oauth.model.IssueStatelessChannelAccessTokenResponse;
import com.linecorp.bot.oauth.model.VerifyChannelAccessTokenResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.ocadotechnology.gembus.test.Arranger;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.github.tomakehurst.wiremock.WireMockServer;

/**
* API tests for ChannelAccessTokenClient
*/
@Timeout(5)
public class ChannelAccessTokenClientTest {
    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private WireMockServer wireMockServer;
    private ChannelAccessTokenClient api;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());


        api = ChannelAccessTokenClient.builder("MY_OWN_TOKEN")
            .apiEndPoint(URI.create(wireMockServer.baseUrl()))
            .build();
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void getsAllValidChannelAccessTokenKeyIdsTest() {
        stubFor(get(urlPathTemplate("/oauth2/v2.1/tokens/kid")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String clientAssertionType = Arranger.some(String.class);
            String clientAssertion = Arranger.some(String.class);

        ChannelAccessTokenKeyIdsResponse response = api.getsAllValidChannelAccessTokenKeyIds(clientAssertionType, clientAssertion).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void issueChannelTokenTest() {
        stubFor(post(urlPathTemplate("/v2/oauth/accessToken")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String grantType = Arranger.some(String.class);
            String clientId = Arranger.some(String.class);
            String clientSecret = Arranger.some(String.class);

        IssueShortLivedChannelAccessTokenResponse response = api.issueChannelToken(grantType, clientId, clientSecret).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void issueChannelTokenByJWTTest() {
        stubFor(post(urlPathTemplate("/oauth2/v2.1/token")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String grantType = Arranger.some(String.class);
            String clientAssertionType = Arranger.some(String.class);
            String clientAssertion = Arranger.some(String.class);

        IssueChannelAccessTokenResponse response = api.issueChannelTokenByJWT(grantType, clientAssertionType, clientAssertion).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void issueStatelessChannelTokenTest() {
        stubFor(post(urlPathTemplate("/oauth2/v3/token")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String grantType = Arranger.some(String.class);
            String clientAssertionType = Arranger.some(String.class);
            String clientAssertion = Arranger.some(String.class);
            String clientId = Arranger.some(String.class);
            String clientSecret = Arranger.some(String.class);

        IssueStatelessChannelAccessTokenResponse response = api.issueStatelessChannelToken(grantType, clientAssertionType, clientAssertion, clientId, clientSecret).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void revokeChannelTokenTest() {
        stubFor(post(urlPathTemplate("/v2/oauth/revoke")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String accessToken = Arranger.some(String.class);

        api.revokeChannelToken(accessToken).join().body();

        // TODO: test validations
    }

    @Test
    public void revokeChannelTokenByJWTTest() {
        stubFor(post(urlPathTemplate("/oauth2/v2.1/revoke")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String clientId = Arranger.some(String.class);
            String clientSecret = Arranger.some(String.class);
            String accessToken = Arranger.some(String.class);

        api.revokeChannelTokenByJWT(clientId, clientSecret, accessToken).join().body();

        // TODO: test validations
    }

    @Test
    public void verifyChannelTokenTest() {
        stubFor(post(urlPathTemplate("/v2/oauth/verify")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String accessToken = Arranger.some(String.class);

        VerifyChannelAccessTokenResponse response = api.verifyChannelToken(accessToken).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void verifyChannelTokenByJWTTest() {
        stubFor(get(urlPathTemplate("/oauth2/v2.1/verify")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String accessToken = Arranger.some(String.class);

        VerifyChannelAccessTokenResponse response = api.verifyChannelTokenByJWT(accessToken).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

}
