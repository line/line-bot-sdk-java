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

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.manageaudience.ManageAudienceException;
import com.linecorp.bot.model.manageaudience.response.CreateAudienceForUploadingResponse;
import com.linecorp.bot.model.objectmapper.ModelObjectMapper;
import com.linecorp.bot.model.response.BotApiResponse;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Builder;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageAudienceBlobClientImpl implements ManageAudienceBlobClient {
    private static final ObjectMapper objectMapper = ModelObjectMapper.createNewObjectMapper();
    private final ManageAudienceBlobService retrofitImpl;

    public ManageAudienceBlobClientImpl(ManageAudienceBlobService retrofitImpl) {
        this.retrofitImpl = retrofitImpl;
    }

    @Override
    public CompletableFuture<CreateAudienceForUploadingResponse> createAudienceForUploadingUserIds(
            String description, boolean isIfaAudience, String uploadDescription, File file) {
        MultipartBody parts = new MultipartBody.Builder()
                .addFormDataPart("description", description)
                .addFormDataPart("isIfaAudience", String.valueOf(isIfaAudience))
                .addFormDataPart("uploadDescription", uploadDescription)
                .addFormDataPart("file", file.getName(),
                                 RequestBody.create(MediaType.get("text/plain"), file))
                .build();

        return toFuture(retrofitImpl.createAudienceForUploadingUserIds(parts));
    }

    @Override
    public CompletableFuture<BotApiResponse> addUserIdsToAudience(long audienceGroupId,
                                                                  String uploadDescription,
                                                                  File file) {
        MultipartBody parts = new Builder()
                .addFormDataPart("audienceGroupId", String.valueOf(audienceGroupId))
                .addFormDataPart("uploadDescription", uploadDescription)
                .addFormDataPart("file", file.getName(),
                                 RequestBody.create(MediaType.get("text/plain"), file))
                .build();

        return LineMessagingClientImpl.toBotApiFuture(retrofitImpl.addUserIdsToAudience(
                parts
        ));
    }

    private static <T> CompletableFuture<T> toFuture(Call<T> call) {
        final CallbackCompletableFuture<T> future = new CallbackCompletableFuture<>();
        call.enqueue(future);
        return future;
    }

    private static class CallbackCompletableFuture<T> extends CompletableFuture<T> implements Callback<T> {
        @Override
        public void onResponse(final Call<T> call, final Response<T> response) {
            if (response.isSuccessful()) {
                complete(response.body());
                return;
            }
            if (response.code() == 400) {
                try {
                    completeExceptionally(objectMapper.readValue(response.errorBody().string(),
                                                                 ManageAudienceException.class));
                    return;
                } catch (IOException e) {
                    completeExceptionally(e);
                }
            }
            completeExceptionally(new ManageAudienceException(response.message()));
        }

        @Override
        public void onFailure(final Call<T> call, final Throwable t) {
            completeExceptionally(new ManageAudienceException(t.getMessage(), t));
        }
    }
}
