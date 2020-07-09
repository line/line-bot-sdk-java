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

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.linecorp.bot.client.exception.GeneralLineMessagingException;
import com.linecorp.bot.model.Broadcast;
import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.Narrowcast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.response.BotApiResponse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Proxy implementation of {@link RetryableLineMessagingClient} to hind internal implementation.
 */
@Slf4j
@AllArgsConstructor
public class RetryableLineMessagingClientImpl implements RetryableLineMessagingClient {
    static final ExceptionConverter EXCEPTION_CONVERTER = new ExceptionConverter();

    private final LineMessagingService retrofitImpl;

    @Override
    public CompletableFuture<BotApiResponse> pushMessage(final UUID retryKey, final PushMessage pushMessage) {
        return toBotApiResponseFuture(retrofitImpl.pushMessage(retryKey.toString(), pushMessage));
    }

    @Override
    public CompletableFuture<BotApiResponse> multicast(final UUID retryKey, final Multicast multicast) {
        return toBotApiResponseFuture(retrofitImpl.multicast(retryKey.toString(), multicast));
    }

    @Override
    public CompletableFuture<BotApiResponse> broadcast(final UUID retryKey, final Broadcast broadcast) {
        return toBotApiResponseFuture(retrofitImpl.broadcast(retryKey.toString(), broadcast));
    }

    @Override
    public CompletableFuture<BotApiResponse> narrowcast(final UUID retryKey, final Narrowcast narrowcast) {
        return toBotApiResponseFuture(retrofitImpl.narrowcast(retryKey.toString(), narrowcast));
    }

    private static CompletableFuture<BotApiResponse> toBotApiResponseFuture(
            final Call<BotApiResponseBody> callToWrap) {
        final BotApiCallbackAdaptor completableFuture = new BotApiCallbackAdaptor();
        callToWrap.enqueue(completableFuture);
        return completableFuture;
    }

    static class BotApiCallbackAdaptor extends CompletableFuture<BotApiResponse>
            implements Callback<BotApiResponseBody> {
        @Override
        public void onResponse(final Call<BotApiResponseBody> call,
                               final Response<BotApiResponseBody> response) {
            if (response.isSuccessful()) {
                final String requestId = response.headers().get("x-line-request-id");
                complete(response.body().withRequestId(requestId));
            } else {
                completeExceptionally(EXCEPTION_CONVERTER.apply(response));
            }
        }

        @Override
        public void onFailure(final Call<BotApiResponseBody> call, final Throwable t) {
            completeExceptionally(
                    new GeneralLineMessagingException(t.getMessage(), null, t));
        }
    }
}
