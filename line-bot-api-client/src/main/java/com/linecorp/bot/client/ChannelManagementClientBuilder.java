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

import static com.linecorp.bot.client.LineMessagingClientBuilder.buildAuthenticationInterceptor;
import static com.linecorp.bot.client.LineMessagingClientBuilder.buildLoggingInterceptor;
import static com.linecorp.bot.client.LineMessagingClientBuilder.createDefaultRetrofitBuilder;

import java.net.URI;

import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import retrofit2.Retrofit;

@Setter
@Accessors(fluent = true)
public class ChannelManagementClientBuilder {
    private URI apiEndPoint = LineClientConstants.DEFAULT_API_END_POINT;
    private ChannelTokenSupplier channelTokenSupplier;

    /**
     * Create a new {@link ChannelManagementClientBuilder} with specified {@link ChannelTokenSupplier}.
     */
    public static ChannelManagementClientBuilder create(@NonNull ChannelTokenSupplier channelTokenSupplier) {
        return new ChannelManagementClientBuilder()
                .channelTokenSupplier(channelTokenSupplier);
    }

    /**
     * Build a new {@link ChannelManagementSyncClient}.
     */
    public ChannelManagementSyncClient build() {
        final Builder okHttpClientBuilder = new Builder();

        okHttpClientBuilder
                .addInterceptor(buildAuthenticationInterceptor(channelTokenSupplier))
                .addInterceptor(buildLoggingInterceptor());

        final OkHttpClient okHttpClient = okHttpClientBuilder.build();

        final Retrofit.Builder retrofitBuilder = createDefaultRetrofitBuilder();

        retrofitBuilder.client(okHttpClient);
        retrofitBuilder.baseUrl(apiEndPoint.toString());
        final Retrofit retrofit = retrofitBuilder.build();

        final ChannelManagementClientRetrofitIface retrofitIface =
                retrofit.create(ChannelManagementClientRetrofitIface.class);
        return ChannelManagementSyncClientImpl.of(retrofitIface);
    }
}
