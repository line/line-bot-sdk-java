/*
 * Copyright 2021 LINE Corporation
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

package com.linecorp.bot.model.oauth;

import static java.lang.ClassLoader.getSystemResourceAsStream;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.testutil.TestUtil;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OAuthDeserializeTest {
    @Test
    public void testIssueChannelAccessToken() {
        final IssueChannelAccessTokenResponse target =
                parse("oauth/oauth2/v2.1/token.json", IssueChannelAccessTokenResponse.class);

        assertThat(target.getAccessToken()).isEqualTo("eyJhbGciOiJIUz.....");
        assertThat(target.getTokenType()).isEqualTo("Bearer");
        assertThat(target.getExpiresInSecs()).isEqualTo(2592000L);
        assertThat(target.getKeyId()).isEqualTo("sDTOzw5wIfxxxxPEzcmeQA");
    }

    @Test
    public void testTokenKids() {
        final ChannelAccessTokenKeyIdsResponse target =
                parse("oauth/oauth2/v2.1/tokens/kid.json", ChannelAccessTokenKeyIdsResponse.class);

        assertThat(target.getKids()).hasSize(6);
        assertThat(target.getKids().get(0)).isEqualTo("U_gdnFYKTWRxxxxDVZexGg");
        assertThat(target.getKids().get(5)).isEqualTo("G82YP96jhHwyKSxxxx7IFA");
    }

    @SneakyThrows
    private static <T> T parse(final String resourceName, final Class<T> clazz) {
        final ObjectMapper objectMapper = TestUtil.objectMapperWithProductionConfiguration(true);

        try (InputStream is = getSystemResourceAsStream(resourceName)) {
            return objectMapper.readValue(is, clazz);
        }
    }
}
