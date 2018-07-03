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

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.liff.LiffView;
import com.linecorp.bot.liff.LiffView.Type;
import com.linecorp.bot.liff.request.LiffAppAddRequest;
import com.linecorp.bot.liff.response.LiffAppAddResponse;
import com.linecorp.bot.model.objectmapper.ModelObjectMapper;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

public class ChannelManagementSyncClientIntegrationWiremockTest
        extends AbstractWiremockTest {
    private static final ObjectMapper OBJECT_MAPPER = ModelObjectMapper.createNewObjectMapper();

    @Test(timeout = ASYNC_TEST_TIMEOUT)
    public void testAddLiffMenu() throws Exception {
        // Mocking
        LiffAppAddResponse response = new LiffAppAddResponse("NEW_LIFF_ID");
        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                                                .setBody(OBJECT_MAPPER.writeValueAsString(response)));

        // Do
        LiffView liffView = new LiffView(Type.COMPACT, URI.create("https://example.com"));
        LiffAppAddRequest request = new LiffAppAddRequest(liffView);
        final LiffAppAddResponse liffAppAddResponse = channelManagementSyncClient.addLiffApp(request);

        // Verify
        final RecordedRequest recordedRequest = mockWebServer.takeRequest();
        final LiffAppAddRequest requestedBody = OBJECT_MAPPER
                .readValue(recordedRequest.getBody().readString(StandardCharsets.UTF_8),
                           LiffAppAddRequest.class);
        assertThat(requestedBody)
                .isEqualTo(request);
        assertThat(recordedRequest.getPath())
                .isEqualTo("/liff/v1/apps");
        assertThat(liffAppAddResponse.getLiffId())
                .isEqualTo("NEW_LIFF_ID");
    }
}
