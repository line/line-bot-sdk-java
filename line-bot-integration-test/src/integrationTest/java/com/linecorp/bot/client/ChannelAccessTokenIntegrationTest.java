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

package com.linecorp.bot.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URI;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWK;

import com.linecorp.bot.client.utils.IntegrationTestSettings;
import com.linecorp.bot.client.utils.IntegrationTestSettingsLoader;
import com.linecorp.bot.oauth.client.ChannelAccessTokenClient;
import com.linecorp.bot.oauth.model.ChannelAccessTokenKeyIdsResponse;
import com.linecorp.bot.oauth.model.IssueChannelAccessTokenResponse;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.jackson.io.JacksonSerializer;

public class ChannelAccessTokenIntegrationTest {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ChannelAccessTokenIntegrationTest.class);

    private ChannelAccessTokenClient target;
    private JWK jwk;
    private String channelId;
    private String channelSecret;
    private IntegrationTestSettings settings;

    @BeforeEach
    public void setUp() throws IOException, ParseException {
        this.settings = IntegrationTestSettingsLoader.load();

        target = ChannelAccessTokenClient
                .builder(settings.token())
                .apiEndPoint(URI.create(settings.apiEndpoint()))
                .build();

        jwk = JWK.parse(settings.privateKeyJWK());

        channelId = settings.channelId();
        channelSecret = settings.channelSecret();
    }

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    @Test
    public void gwtTokenIntegrationTest() throws Exception {
        final Map<String, Object> header = Map.of(
                "alg", "RS256",
                "typ", "JWT",
                "kid", settings.kid()
        );

        final Map<String, Object> body = Map.of(
                "iss", channelId,
                "sub", channelId,
                "aud", settings.apiEndpoint(),
                "exp", Instant.now().plusSeconds(10).getEpochSecond(),
                "token_exp", Duration.ofMinutes(1).getSeconds()
        );

        byte[] rsaPrivateKey = jwk.toRSAKey().toRSAPrivateKey().getEncoded();

        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(rsaPrivateKey));

        String jws = Jwts.builder()
                .json(new JacksonSerializer<>(new ObjectMapper()))
                .header()
                .add(header)
                .and()
                .claims(body)
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();

        log.info("{}", jws);

        // Issue
        IssueChannelAccessTokenResponse issueChannelAccessTokenResponse =
                target.issueChannelTokenByJWT(
                        "client_credentials",
                        "urn:ietf:params:oauth:client-assertion-type:jwt-bearer",
                        jws).get().body();

        log.info("{}", issueChannelAccessTokenResponse);
        assertThat(issueChannelAccessTokenResponse.expiresIn()).isEqualTo(60);
        assertThat(issueChannelAccessTokenResponse.keyId()).isNotBlank();

        ChannelAccessTokenKeyIdsResponse keyIdsResponse =
                target.getsAllValidChannelAccessTokenKeyIds(
                        "urn:ietf:params:oauth:client-assertion-type:jwt-bearer", jws).get().body();
        assertThat(keyIdsResponse.kids().size()).isGreaterThan(0);
        log.info("{}", keyIdsResponse);

        // Revoke
        target.revokeChannelTokenByJWT(
                        channelId,
                        channelSecret,
                        issueChannelAccessTokenResponse.accessToken())
                .get();
    }
}
