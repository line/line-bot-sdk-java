/*
 * Copyright 2026 LINE Corporation
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

package com.linecorp.bot.client.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class IntegrationTestSettingsYamlTest {
    @Test
    void readsSettingsFromYaml() throws Exception {
        String yaml = """
                enabled: true
                token: "token:abc"
                apiEndpoint: https://example.com
                userId: U123
                audienceIfas:
                  - first
                  - second
                failOnUnknownProperties: false
                """;

        IntegrationTestSettings settings = IntegrationTestSettingsLoader.readYamlSettings(
                new ByteArrayInputStream(yaml.getBytes(StandardCharsets.UTF_8)));

        assertThat(settings.enabled()).isTrue();
        assertThat(settings.token()).isEqualTo("token:abc");
        assertThat(settings.apiEndpoint()).isEqualTo("https://example.com");
        assertThat(settings.userId()).isEqualTo("U123");
        assertThat(settings.audienceIfas()).containsExactly("first", "second");
        assertThat(settings.failOnUnknownProperties()).isFalse();
    }

    @Test
    void reportsMalformedYamlAsIOException() {
        byte[] yaml = "enabled: [unterminated".getBytes(StandardCharsets.UTF_8);

        assertThatThrownBy(() -> IntegrationTestSettingsLoader.readYamlSettings(
                new ByteArrayInputStream(yaml)))
                .isInstanceOf(IOException.class);
    }

    @Test
    void rejectsUnknownSettingAsIOException() {
        byte[] yaml = "enabled: true\nunknownSetting: true\n".getBytes(StandardCharsets.UTF_8);

        assertThatThrownBy(() -> IntegrationTestSettingsLoader.readYamlSettings(
                new ByteArrayInputStream(yaml)))
                .isInstanceOf(IOException.class)
                .hasMessageContaining("unknownSetting");
    }

    @ParameterizedTest
    @ValueSource(strings = {"enabled: true\n", "enabled: true\nfailOnUnknownProperties: null\n"})
    void defaultsMissingOrNullPrimitiveSettingToFalse(String yaml) throws IOException {
        IntegrationTestSettings settings = IntegrationTestSettingsLoader.readYamlSettings(
                new ByteArrayInputStream(yaml.getBytes(StandardCharsets.UTF_8)));

        assertThat(settings.failOnUnknownProperties()).isFalse();
    }

    @Test
    void readsOnlyFirstYamlDocument() throws IOException {
        byte[] yaml = "enabled: true\n---\nenabled: false\n".getBytes(StandardCharsets.UTF_8);

        IntegrationTestSettings settings = IntegrationTestSettingsLoader.readYamlSettings(
                new ByteArrayInputStream(yaml));

        assertThat(settings.enabled()).isTrue();
    }

    @Test
    void reportsYamlReadFailureAsIOExceptionWithOriginalRootCause() throws IOException {
        IOException failure = new SocketTimeoutException("settings read timed out");
        try (InputStream yaml = new InputStream() {
            @Override
            public int read() throws IOException {
                throw failure;
            }
        }) {
            assertThatThrownBy(() -> IntegrationTestSettingsLoader.readYamlSettings(yaml))
                    .isInstanceOf(IOException.class)
                    .rootCause()
                    .isSameAs(failure);
        }
    }
}
