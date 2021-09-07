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

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.mockito.Mockito.when;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.tomakehurst.wiremock.WireMockServer;

@ExtendWith(MockitoExtension.class)
public class HeaderInterceptorWireMockTest extends AbstractWiremockTest {
    @Mock
    ChannelTokenSupplier channelTokenSupplier;

    @Test
    @Timeout(ASYNC_TEST_TIMEOUT)
    public void forChannelTokenSupplier() throws Exception {
        stubFor(get(urlEqualTo("/v2/bot/profile/TEST"))
                        .willReturn(aResponse().withStatus(200)
                                               .withBody("{}")));

        // Do
        when(channelTokenSupplier.get()).thenReturn("1st");
        lineMessagingClient.getProfile("TEST").get();

        // Verify
        verify(getRequestedFor(urlEqualTo("/v2/bot/profile/TEST"))
                       .withHeader("Authorization", equalTo("Bearer 1st")));

        // Do again with another channel token.
        when(channelTokenSupplier.get()).thenReturn("2nd");
        lineMessagingClient.getProfile("TEST").get();

        // Verify
        verify(getRequestedFor(urlEqualTo("/v2/bot/profile/TEST"))
                       .withHeader("Authorization", equalTo("Bearer 2nd")));
    }

    @Override
    protected LineMessagingClient createLineMessagingClient(final WireMockServer wireMockServer) {
        return LineMessagingClient.builder(channelTokenSupplier)
                                  .apiEndPoint(URI.create(wireMockServer.baseUrl()))
                                  .build();
    }
}
