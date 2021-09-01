/*
 * Copyright 2018 LINE Corporation
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

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.net.URI;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class LineMessagingClientBuilderTest extends AbstractWiremockTest {
    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void testBuildWithFixedToken() throws InterruptedException {
        lineMessagingClient = new LineMessagingClientBuilder()
                .channelToken("MOCKED_TOKEN")
                .apiEndPoint(URI.create(wireMockServer.baseUrl()))
                .build();

        // Do
        lineMessagingClient.getProfile("TEST");

        // Verify
        verify(getRequestedFor(urlEqualTo("/v2/bot/profile/TEST"))
                       .withHeader("Authorization", equalTo("Bearer MOCKED_TOKEN")));
    }

    @Test
    public void testBuilderWithChannelTokenSupplier() throws InterruptedException {
        lineMessagingClient =
                LineMessagingClient.builder(() -> "MOCKED_TOKEN")
                                   .apiEndPoint(URI.create(wireMockServer.baseUrl()))
                                   .build();

        // Do
        lineMessagingClient.getProfile("TEST");

        // Verify
        verify(getRequestedFor(urlEqualTo("/v2/bot/profile/TEST"))
                       .withHeader("Authorization", equalTo("Bearer MOCKED_TOKEN")));
    }

    @Test
    public void testBuildWithoutChannelToken() {
        assertThatThrownBy(() -> {
            new LineMessagingClientBuilder().build();
        }).isInstanceOf(NullPointerException.class)
          .hasMessageContaining("channelTokenSupplier");
    }

}
