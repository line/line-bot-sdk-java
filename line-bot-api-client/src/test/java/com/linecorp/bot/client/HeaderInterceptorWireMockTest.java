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

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class HeaderInterceptorWireMockTest extends AbstractWiremockTest {
    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    ChannelTokenSupplier channelTokenSupplier;

    @Test(timeout = ASYNC_TEST_TIMEOUT)
    public void forChannelTokenSupplier() throws Exception {
        // Do
        when(channelTokenSupplier.get()).thenReturn("1st");
        lineMessagingClient.getMessageContent("TEST");

        // Verify
        final RecordedRequest request1st = mockWebServer.takeRequest();
        assertThat(request1st.getHeaders().toMultimap())
                .containsEntry("Authorization", singletonList("Bearer 1st"));

        // Do again with another channel token.
        when(channelTokenSupplier.get()).thenReturn("2nd");
        lineMessagingClient.getMessageContent("TEST");

        // Verify
        final RecordedRequest request2nd = mockWebServer.takeRequest();
        assertThat(request2nd.getHeaders().toMultimap())
                .containsEntry("Authorization", singletonList("Bearer 2nd"));
    }

    @Override
    protected LineMessagingClientImpl createLineMessagingClient(final MockWebServer mockWebServer) {
        LineMessagingService lineMessagingService =
                LineMessagingServiceBuilder.create(channelTokenSupplier)
                                           .apiEndPoint("http://localhost:" + mockWebServer.getPort())
                                           .build();
        return new LineMessagingClientImpl(lineMessagingService);
    }
}
