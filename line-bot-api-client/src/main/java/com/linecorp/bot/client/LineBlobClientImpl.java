/*
 * Copyright 2019 LINE Corporation
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

import java.util.concurrent.CompletableFuture;

import com.linecorp.bot.client.exception.GeneralLineMessagingException;
import com.linecorp.bot.model.response.BotApiResponse;

import lombok.AllArgsConstructor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AllArgsConstructor
class LineBlobClientImpl implements LineBlobClient {
    private final LineBlobService retrofitImpl;

    @Override
    public CompletableFuture<MessageContentResponse> getMessageContent(final String messageId) {
        return toMessageContentResponseFuture(retrofitImpl.getMessageContent(messageId));
    }

    @Override
    public CompletableFuture<MessageContentResponse> getRichMenuImage(final String richMenuId) {
        return toMessageContentResponseFuture(retrofitImpl.getRichMenuImage(richMenuId));
    }

    @Override
    public CompletableFuture<BotApiResponse> setRichMenuImage(
            final String richMenuId, final String contentType, final byte[] content) {
        final RequestBody requestBody = RequestBody.create(MediaType.parse(contentType), content);
        return LineMessagingClientImpl.toBotApiFuture(
                retrofitImpl.uploadRichMenuImage(richMenuId, requestBody));
    }

    private static CompletableFuture<MessageContentResponse> toMessageContentResponseFuture(
            final Call<ResponseBody> callToWrap) {
        final ResponseBodyCallbackAdaptor future = new ResponseBodyCallbackAdaptor();
        callToWrap.enqueue(future);
        return future;
    }

    static class ResponseBodyCallbackAdaptor
            extends CompletableFuture<MessageContentResponse>
            implements Callback<ResponseBody> {

        @Override
        public void onResponse(final Call<ResponseBody> call, final Response<ResponseBody> response) {
            if (!response.isSuccessful()) {
                completeExceptionally(LineMessagingClientImpl.EXCEPTION_CONVERTER.apply(response));
                return;
            }

            try {
                complete(convert(response));
            } catch (RuntimeException exceptionInConvert) {
                completeExceptionally(
                        new GeneralLineMessagingException(exceptionInConvert.getMessage(),
                                                          null, exceptionInConvert));
            }
        }

        @Override
        public void onFailure(final Call<ResponseBody> call, final Throwable t) {
            completeExceptionally(
                    new GeneralLineMessagingException(t.getMessage(), null, t));
        }

        private MessageContentResponse convert(final Response<ResponseBody> response) {
            return MessageContentResponse
                    .builder()
                    .length(response.body().contentLength())
                    .allHeaders(response.headers().toMultimap())
                    .mimeType(response.body().contentType().toString())
                    .stream(response.body().byteStream())
                    .build();
        }
    }
}
