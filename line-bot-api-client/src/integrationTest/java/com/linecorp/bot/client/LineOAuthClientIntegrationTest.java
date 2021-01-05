/*
 * Copyright 2020 LINE Corporation
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
import static org.assertj.core.api.Assumptions.assumeThat;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.nimbusds.jose.jwk.JWK;

import com.linecorp.bot.model.oauth.ChannelAccessTokenKeyIdsResponse;
import com.linecorp.bot.model.oauth.IssueChannelAccessTokenResponse;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.jackson.io.JacksonSerializer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LineOAuthClientIntegrationTest {
    private static final URL TEST_RESOURCE = ClassLoader.getSystemResource("integration_test_settings.yml");

    private LineOAuthClient target;
    private String endpoint;
    private JWK jwk;
    private String channelId;
    private String channelSecret;
    private String kid;

    @Before
    public void setUp() throws IOException, ParseException {
        assumeThat(TEST_RESOURCE)
                .isNotNull();

        final Map<?, ?> map = new ObjectMapper()
                .convertValue(new Yaml().load(TEST_RESOURCE.openStream()), Map.class);

        endpoint = (String) map.get("endpoint");
        target = LineOAuthClient
                .builder()
                .apiEndPoint(URI.create(endpoint))
                .build();

        // 1. Issue new "Assertion Signing Key" in the LINE Developer Center.
        // 2. You get the private key of your new assertion signing key in JWK format.
        jwk = JWK.parse((String) map.get("jwk"));

        kid = jwk.getKeyID();
        assumeThat(kid).describedAs("kid must not be null")
                       .isNotNull();

        channelId = String.valueOf(map.get("channelId"));
        channelSecret = (String) map.get("channelSecret");
    }

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    @Test
    public void gwtTokenIntegrationTest() throws Exception {
        final Map<String, Object> header = ImmutableMap.of(
                "alg", "RS256",
                "typ", "JWT",
                "kid", kid
        );

        final Map<String, Object> body = ImmutableMap.of(
                "iss", channelId,
                "sub", channelId,
                "aud", endpoint,
                "exp", Instant.now().plusSeconds(10).getEpochSecond(),
                "token_exp", Duration.ofMinutes(1).getSeconds()
        );

        byte[] rsaPrivateKey = jwk.toRSAKey().toRSAPrivateKey().getEncoded();

        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(rsaPrivateKey));

        String jws = Jwts.builder()
                         .serializeToJsonWith(new JacksonSerializer<>(new ObjectMapper()))
                         .setHeader(header)
                         .setClaims(body)
                         .signWith(privateKey, SignatureAlgorithm.RS256)
                         .compact();

        log.info("{}", jws);

        // Issue
        IssueChannelAccessTokenResponse issueChannelAccessTokenResponse =
                target.issueChannelTokenByJWT(jws).get();

        log.info("{}", issueChannelAccessTokenResponse);
        assertThat(issueChannelAccessTokenResponse.getExpiresInSecs()).isEqualTo(60);
        assertThat(issueChannelAccessTokenResponse.getKeyId()).isNotBlank();

        ChannelAccessTokenKeyIdsResponse keyIdsResponse =
                target.getsAllValidChannelAccessTokenKeyIdsByJWT(jws).get();
        assertThat(keyIdsResponse.getKids().size()).isGreaterThan(0);
        log.info("{}", keyIdsResponse);

        // Revoke
        target.revokeChannelTokenByJWT(
                channelId,
                channelSecret,
                issueChannelAccessTokenResponse.getAccessToken())
              .get();
    }
}
