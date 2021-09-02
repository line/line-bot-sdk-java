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

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

import com.linecorp.bot.model.Broadcast;
import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.Narrowcast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.narrowcast.Filter;
import com.linecorp.bot.model.narrowcast.filter.GenderDemographicFilter;
import com.linecorp.bot.model.narrowcast.filter.GenderDemographicFilter.Gender;
import com.linecorp.bot.model.response.BotApiResponse;

import okhttp3.Headers;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@ExtendWith(MockitoExtension.class)
@Timeout(5)
public class RetryableLineMessagingClientImplTest {
    private static final String REQUEST_ID_FIXTURE = "REQUEST_ID_FIXTURE";
    private static final BotApiResponseBody BOT_API_SUCCESS_RESPONSE_BODY =
            new BotApiResponseBody("", emptyList());
    private static final BotApiResponse BOT_API_SUCCESS_RESPONSE =
            BOT_API_SUCCESS_RESPONSE_BODY.withRequestId(REQUEST_ID_FIXTURE);

    @Mock
    private LineMessagingService retrofitMock;

    @InjectMocks
    private RetryableLineMessagingClientImpl target;

    @Test
    public void pushMessageTest() throws Exception {
        UUID retryKey = UUID.randomUUID();
        whenCall(retrofitMock.pushMessage(any(String.class), any(PushMessage.class)),
                 BOT_API_SUCCESS_RESPONSE_BODY);
        final PushMessage pushMessage = new PushMessage("TO", new TextMessage("text"));

        // Do
        final BotApiResponse botApiResponse =
                target.pushMessage(retryKey, pushMessage).get();

        // Verify
        verify(retrofitMock, only()).pushMessage(retryKey.toString(), pushMessage);
        assertThat(botApiResponse).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    @Test
    public void multicastTest() throws Exception {
        UUID retryKey = UUID.randomUUID();
        whenCall(retrofitMock.multicast(any(String.class), any(Multicast.class)),
                 BOT_API_SUCCESS_RESPONSE_BODY);
        final Multicast multicast = new Multicast(singleton("TO"), new TextMessage("text"));

        // Do
        final BotApiResponse botApiResponse =
                target.multicast(retryKey, multicast).get();

        // Verify
        verify(retrofitMock, only()).multicast(retryKey.toString(), multicast);
        assertThat(botApiResponse).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    @Test
    public void broadcast() {
        UUID retryKey = UUID.randomUUID();
        whenCall(retrofitMock.broadcast(any(String.class), any(Broadcast.class)),
                 BOT_API_SUCCESS_RESPONSE_BODY);
        final Broadcast broadcast = new Broadcast(singletonList(new TextMessage("text")), true);

        final BotApiResponse botApiResponse = target.broadcast(retryKey, broadcast).join();
        verify(retrofitMock).broadcast(retryKey.toString(), broadcast);
        assertThat(botApiResponse).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    @Test
    public void narrowcast() {
        UUID retryKey = UUID.randomUUID();
        whenCall(retrofitMock.narrowcast(any(String.class), any(Narrowcast.class)),
                 BOT_API_SUCCESS_RESPONSE_BODY);
        final Narrowcast narrowcast = new Narrowcast(
                new TextMessage("text"),
                Filter.builder()
                      .demographic(
                              GenderDemographicFilter.builder()
                                                     .oneOf(singletonList(Gender.FEMALE))
                                                     .build()
                      ).build());

        final BotApiResponse botApiResponse = target.narrowcast(retryKey, narrowcast).join();
        verify(retrofitMock).narrowcast(retryKey.toString(), narrowcast);
        assertThat(botApiResponse).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    private static <T> void whenCall(Call<T> call, T value) {
        final OngoingStubbing<Call<T>> callOngoingStubbing = when(call);
        callOngoingStubbing.thenReturn(enqueue(value));
    }

    private static <T> Call<T> enqueue(T value) {
        return new Call<T>() {
            @Override
            public Response<T> execute() throws IOException {
                throw new UnsupportedOperationException();
            }

            @Override
            public void enqueue(Callback<T> callback) {
                final Headers headers = Headers.of(singletonMap("x-line-request-id", REQUEST_ID_FIXTURE));
                callback.onResponse(this, Response.success(value, headers));
            }

            @Override
            public boolean isExecuted() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void cancel() {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean isCanceled() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Call<T> clone() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Request request() {
                throw new UnsupportedOperationException();
            }

            @Override
            public okio.Timeout timeout() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
