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

import com.linecorp.bot.client.LineMessagingClientImpl.VoidToBotApiCallbackAdaptor;
import com.linecorp.bot.model.manageaudience.AudienceGroupCreateRoute;
import com.linecorp.bot.model.manageaudience.AudienceGroupStatus;
import com.linecorp.bot.model.manageaudience.request.AddAudienceToAudienceGroupRequest;
import com.linecorp.bot.model.manageaudience.request.CreateAudienceGroupRequest;
import com.linecorp.bot.model.manageaudience.request.CreateClickBasedAudienceGroupRequest;
import com.linecorp.bot.model.manageaudience.request.CreateImpBasedAudienceGroupRequest;
import com.linecorp.bot.model.manageaudience.request.UpdateAudienceGroupAuthorityLevelRequest;
import com.linecorp.bot.model.manageaudience.request.UpdateAudienceGroupDescriptionRequest;
import com.linecorp.bot.model.manageaudience.response.CreateAudienceGroupResponse;
import com.linecorp.bot.model.manageaudience.response.CreateClickBasedAudienceGroupResponse;
import com.linecorp.bot.model.manageaudience.response.CreateImpBasedAudienceGroupResponse;
import com.linecorp.bot.model.manageaudience.response.GetAudienceDataResponse;
import com.linecorp.bot.model.manageaudience.response.GetAudienceGroupAuthorityLevelResponse;
import com.linecorp.bot.model.manageaudience.response.GetAudienceGroupsResponse;
import com.linecorp.bot.model.oauth.ChannelAccessTokenException;
import com.linecorp.bot.model.objectmapper.ModelObjectMapper;
import com.linecorp.bot.model.response.BotApiResponse;

import lombok.AllArgsConstructor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

/**
 * An implementation of {@link ManageAudienceClient} that issues or revokes channel access tokens.
 */
@AllArgsConstructor
class ManageAudienceClientImpl implements ManageAudienceClient {
    private static final ObjectMapper objectMapper = ModelObjectMapper.createNewObjectMapper();

    private final ManageAudienceService retrofitImpl;

    @Override
    public CompletableFuture<CreateAudienceGroupResponse> createAudienceGroup(
            CreateAudienceGroupRequest request) {
        return toFuture(retrofitImpl.createAudienceGroup(request));
    }

    @Override
    public CompletableFuture<BotApiResponse> addAudienceToAudienceGroup(
            AddAudienceToAudienceGroupRequest request) {
        return toBotApiFuture(retrofitImpl.addAudienceToAudienceGroup(request));
    }

    @Override
    public CompletableFuture<CreateClickBasedAudienceGroupResponse> createClickBasedAudienceGroup(
            CreateClickBasedAudienceGroupRequest request) {
        return toFuture(retrofitImpl.createClickBasedAudienceGroup(request));
    }

    @Override
    public CompletableFuture<CreateImpBasedAudienceGroupResponse> createImpBasedAudienceGroup(
            CreateImpBasedAudienceGroupRequest request) {
        return toFuture(retrofitImpl.createImpBasedAudienceGroup(request));
    }

    @Override
    public CompletableFuture<BotApiResponse> updateAudienceGroupDescription(
            long audienceGroupId, UpdateAudienceGroupDescriptionRequest request) {
        return toBotApiFuture(retrofitImpl.updateAudienceGroupDescription(audienceGroupId, request));
    }

    @Override
    public CompletableFuture<BotApiResponse> deleteAudienceGroup(long audienceGroupId) {
        return toBotApiFuture(retrofitImpl.deleteAudienceGroup(audienceGroupId));
    }

    @Override
    public CompletableFuture<GetAudienceGroupsResponse> getAudienceGroups(
            long page, String description, AudienceGroupStatus status, Long size,
            Boolean includesExternalPublicGroups, AudienceGroupCreateRoute createRoute) {
        return toFuture(retrofitImpl.getAudienceGroups(
                page, description, status, size,
                includesExternalPublicGroups, createRoute));
    }

    @Override
    public CompletableFuture<GetAudienceDataResponse> getAudienceData(long audienceGroupId) {
        return toFuture(retrofitImpl.getAudienceData(audienceGroupId));
    }

    @Override
    public CompletableFuture<GetAudienceGroupAuthorityLevelResponse> getAudienceGroupAuthorityLevel() {
        return toFuture(retrofitImpl.getAudienceGroupAuthorityLevel());
    }

    @Override
    public CompletableFuture<BotApiResponse> updateAudienceGroupAuthorityLevel(
            @Body UpdateAudienceGroupAuthorityLevelRequest request) {
        return toBotApiFuture(retrofitImpl.updateAudienceGroupAuthorityLevel(request));
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

    static CompletableFuture<BotApiResponse> toBotApiFuture(Call<Void> callToWrap) {
        final VoidToBotApiCallbackAdaptor completableFuture = new VoidToBotApiCallbackAdaptor();
        callToWrap.enqueue(completableFuture);
        return completableFuture;
    }
}
