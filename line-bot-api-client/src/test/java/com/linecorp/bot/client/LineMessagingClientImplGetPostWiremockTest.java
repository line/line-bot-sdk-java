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

import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import com.linecorp.bot.client.exception.BadRequestException;
import com.linecorp.bot.model.error.ErrorDetail;
import com.linecorp.bot.model.error.ErrorResponse;
import com.linecorp.bot.model.response.BotApiResponse;

import okhttp3.mockwebserver.RecordedRequest;

public class LineMessagingClientImplGetPostWiremockTest extends AbstractWiremockTest {
    @Rule
    public final Timeout timeoutRule = Timeout.seconds(5);

    @Test
    public void testGet() throws Exception {
        // Mocking
        final BotApiResponse mockResponse = new BotApiResponse("OK", singletonList("Sample"));
        mocking(200, mockResponse);

        // Do
        final BotApiResponse response =
                lineMessagingClient.get(URI.create("path/subPath"),
                                        singletonMap("key", "value"), BotApiResponse.class).get();

        // Verify
        assertThat(response).isEqualTo(mockResponse);

        final RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertThat(recordedRequest.getPath())
                .isEqualTo("/path/subPath?key=value");
    }

    @Test
    public void testPost() throws Exception {
        // Mocking
        final BotApiResponse mockResponse = new BotApiResponse("OK", singletonList("Sample"));
        mocking(200, mockResponse);

        // Do
        final BotApiResponse response =
                lineMessagingClient.post(URI.create("path/subPath"),
                                         singletonMap("key", "value"),
                                         singleton("RequestBody"),
                                         BotApiResponse.class).get();

        // Verify
        assertThat(response).isEqualTo(mockResponse);

        final RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertThat(recordedRequest.getPath())
                .isEqualTo("/path/subPath?key=value");
        assertThat(recordedRequest.getBody().readUtf8())
                .isEqualTo("[\"RequestBody\"]");
    }

    @Test
    public void test400FollowsBadRequestException() throws Exception {
        final ErrorResponse errorResponse =
                new ErrorResponse("requestId", "message", singletonList(new ErrorDetail("key", "value")));
        mocking(400, errorResponse);

        // Do
        final CompletableFuture<BotApiResponse> completableFuture =
                lineMessagingClient.get(
                        URI.create("path/subPath"),
                        singletonMap("key", "value"), BotApiResponse.class);

        // Verify
        try {
            completableFuture.get();
        } catch (Exception ignored) {
        }

        assertThat(completableFuture)
                .hasFailedWithThrowableThat()
                .isExactlyInstanceOf(BadRequestException.class);
    }
}
