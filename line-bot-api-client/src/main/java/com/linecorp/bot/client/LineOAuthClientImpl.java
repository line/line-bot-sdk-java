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

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.oauth.ChannelAccessTokenException;
import com.linecorp.bot.model.oauth.ChannelAccessTokenKeyIdsResponse;
import com.linecorp.bot.model.oauth.IssueChannelAccessTokenRequest;
import com.linecorp.bot.model.oauth.IssueChannelAccessTokenResponse;
import com.linecorp.bot.model.objectmapper.ModelObjectMapper;

import lombok.AllArgsConstructor;
import lombok.experimental.PackagePrivate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An implementation of {@link LineOAuthClient} that issues or revokes channel access tokens.
 */
@AllArgsConstructor
@PackagePrivate
class LineOAuthClientImpl implements LineOAuthClient {
    private static final ObjectMapper objectMapper = ModelObjectMapper.createNewObjectMapper();

    private final LineOAuthService service;

    @Override
    public CompletableFuture<ChannelAccessTokenKeyIdsResponse> getsAllValidChannelAccessTokenKeyIdsByJWT(
            String jwt) {
        return toFuture(service.getsAllValidChannelAccessTokenKeyIds(
                "urn:ietf:params:oauth:client-assertion-type:jwt-bearer",
                jwt));
    }

    @Override
    public CompletableFuture<IssueChannelAccessTokenResponse> issueChannelTokenByJWT(final String jwt) {
        return toFuture(service.issueChannelTokenByJWT(
                "client_credentials",
                "urn:ietf:params:oauth:client-assertion-type:jwt-bearer",
                jwt));
    }

    @Override
    public CompletableFuture<Void> revokeChannelTokenByJWT(
            String clientId, String clientSecret, String accessToken) {
        return toFuture(service.revokeChannelTokenByJWT(clientId, clientSecret, accessToken));
    }

    @Override
    public CompletableFuture<IssueChannelAccessTokenResponse> issueChannelToken(
            IssueChannelAccessTokenRequest req) {
        return toFuture(service.issueChannelToken(req.getGrantType(),
                                                  req.getClientId(),
                                                  req.getClientSecret()));
    }

    @Override
    public CompletableFuture<Void> revokeChannelToken(String accessToken) {
        return toFuture(service.revokeChannelToken(accessToken));
    }

    private static <T> CompletableFuture<T> toFuture(Call<T> call) {
        final CallbackCompletableFuture<T> future = new CallbackCompletableFuture<>();
        call.enqueue(future);
        return future;
    }

    static class CallbackCompletableFuture<T> extends CompletableFuture<T> implements Callback<T> {
        @Override
        public void onResponse(final Call<T> call, final Response<T> response) {
            if (response.isSuccessful()) {
                complete(response.body());
                return;
            }
            if (response.code() == 400) {
                try {
                    completeExceptionally(objectMapper.readValue(response.errorBody().string(),
                                                                 ChannelAccessTokenException.class));
                    return;
                } catch (IOException e) {
                    completeExceptionally(e);
                }
            }
            completeExceptionally(new ChannelAccessTokenException(response.message()));
        }

        @Override
        public void onFailure(final Call<T> call, final Throwable t) {
            completeExceptionally(new ChannelAccessTokenException(t.getMessage(), t));
        }
    }
}
