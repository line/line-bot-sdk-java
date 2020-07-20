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

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

@ExtendWith(MockitoExtension.class)
public class HeaderInterceptorWireMockTest extends AbstractWiremockTest {

    @Mock
    ChannelTokenSupplier channelTokenSupplier;

    @Test
    @Timeout(ASYNC_TEST_TIMEOUT)
    public void forChannelTokenSupplier() throws Exception {
        // Do
        when(channelTokenSupplier.get()).thenReturn("1st");
        lineMessagingClient.getProfile("TEST");

        // Verify
        final RecordedRequest request1st = mockWebServer.takeRequest();
        assertThat(request1st.getHeaders().toMultimap())
                .containsEntry("Authorization", singletonList("Bearer 1st"));

        // Do again with another channel token.
        when(channelTokenSupplier.get()).thenReturn("2nd");
        lineMessagingClient.getProfile("TEST");

        // Verify
        final RecordedRequest request2nd = mockWebServer.takeRequest();
        assertThat(request2nd.getHeaders().toMultimap())
                .containsEntry("Authorization", singletonList("Bearer 2nd"));
    }

    @Override
    protected LineMessagingClient createLineMessagingClient(final MockWebServer mockWebServer) {
        return LineMessagingClient.builder(channelTokenSupplier)
                                  .apiEndPoint(URI.create("http://localhost:" + mockWebServer.getPort()))
                                  .build();
    }
}
