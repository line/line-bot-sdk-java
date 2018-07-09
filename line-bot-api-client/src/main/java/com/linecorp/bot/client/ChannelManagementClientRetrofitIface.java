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

import com.linecorp.bot.liff.LiffView;
import com.linecorp.bot.liff.request.LiffAppAddRequest;
import com.linecorp.bot.liff.response.LiffAppAddResponse;
import com.linecorp.bot.liff.response.LiffAppsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Package private interface for Retrofit binding.
 *
 * <p>You can use LIFF API via {@link ChannelManagementSyncClient}.
 * It's independent from Retrofit implementation.
 */
interface ChannelManagementClientRetrofitIface {
    @POST("liff/v1/apps")
    Call<LiffAppAddResponse> addLiffApp(@Body LiffAppAddRequest liffAppAddRequest);

    @PUT("liff/v1/apps/{liffId}/view")
    Call<Void> updateLiffApp(@Path("liffId") String liffId, @Body LiffView liffView);

    @GET("liff/v1/apps")
    Call<LiffAppsResponse> getAllLiffApps();

    @DELETE("liff/v1/apps/{liffId}")
    Call<Void> deleteLiffApp(@Path("liffId") String liffId);
}
