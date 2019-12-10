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

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

interface LineBlobService {
    /**
     * Method for Retrofit.
     *
     * @see LineBlobClient#getMessageContent(String)
     */
    @Streaming
    @GET("v2/bot/message/{messageId}/content")
    Call<ResponseBody> getMessageContent(@Path("messageId") String messageId);

    /**
     * Method for Retrofit.
     *
     * @see LineBlobClient#getRichMenuImage(String)
     */
    @Streaming
    @GET("v2/bot/richmenu/{richMenuId}/content")
    Call<ResponseBody> getRichMenuImage(@Path("richMenuId") String richMenuId);

    /**
     * Method for Retrofit.
     *
     * @see LineBlobClient#setRichMenuImage(String, String, byte[])
     */
    @POST("v2/bot/richmenu/{richMenuId}/content")
    Call<Void> uploadRichMenuImage(
            @Path("richMenuId") String richMenuId,
            @Body RequestBody requestBody);
}
