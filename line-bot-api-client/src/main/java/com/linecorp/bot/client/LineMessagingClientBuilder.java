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

import lombok.NonNull;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class LineMessagingClientBuilder {
    public static final String DEFAULT_API_END_POINT = "https://api.line.me/";
    public static final long DEFAULT_CONNECT_TIMEOUT = 10_000;
    public static final long DEFAULT_READ_TIMEOUT = 10_000;
    public static final long DEFAULT_WRITE_TIMEOUT = 10_000;

    //TODO: Move into all builder logic into this class from LineMessagingClientBuilder.
    private final LineMessagingServiceBuilder delegate;

    /**
     * Create a new {@link LineMessagingServiceBuilder} with specified given fixed channelToken.
     */
    public LineMessagingClientBuilder(final String fixedChannelToken) {
        delegate = LineMessagingServiceBuilder.create(fixedChannelToken);
    }

    /**
     * Create a new {@link LineMessagingServiceBuilder} with specified {@link ChannelTokenSupplier}.
     */
    public LineMessagingClientBuilder(final ChannelTokenSupplier channelTokenSupplier) {
        delegate = LineMessagingServiceBuilder.create(channelTokenSupplier);
    }

    /**
     * Set apiEndPoint.
     */
    public LineMessagingClientBuilder apiEndPoint(@NonNull String apiEndPoint) {
        delegate.apiEndPoint(apiEndPoint);
        return this;
    }

    /**
     * Set connectTimeout in milliseconds.
     */
    public LineMessagingClientBuilder connectTimeout(long connectTimeout) {
        delegate.connectTimeout(connectTimeout);
        return this;
    }

    /**
     * Set readTimeout in milliseconds.
     */
    public LineMessagingClientBuilder readTimeout(long readTimeout) {
        delegate.readTimeout(readTimeout);
        return this;
    }

    /**
     * Set writeTimeout in milliseconds.
     */
    public LineMessagingClientBuilder writeTimeout(long writeTimeout) {
        delegate.writeTimeout(writeTimeout);
        return this;
    }

    /**
     * Add interceptor
     */
    public LineMessagingClientBuilder addInterceptor(Interceptor interceptor) {
        delegate.addInterceptor(interceptor);
        return this;
    }

    /**
     * Add interceptor first
     */
    public LineMessagingClientBuilder addInterceptorFirst(Interceptor interceptor) {
        delegate.addInterceptorFirst(interceptor);
        return this;
    }

    /**
     * Remove all interceptors
     */
    public LineMessagingClientBuilder removeAllInterceptors() {
        delegate.removeAllInterceptors();
        return this;
    }

    /**
     * <p>If you want to use your own setting, specify {@link OkHttpClient.Builder} instance.</p>
     *
     * @deprecated use {@link #okHttpClientBuilder(OkHttpClient.Builder, boolean)} instead.
     */
    @Deprecated
    public LineMessagingClientBuilder okHttpClientBuilder(
            @NonNull final OkHttpClient.Builder okHttpClientBuilder) {
        delegate.okHttpClientBuilder(okHttpClientBuilder);
        return this;
    }

    /**
     * <p>If you want to use your own setting, specify {@link OkHttpClient.Builder} instance.</p>
     *
     * @param resetDefaultInterceptors If true, all default okhttp interceptors ignored.
     * You should insert authentication headers yourself.
     */
    public LineMessagingClientBuilder okHttpClientBuilder(
            @NonNull final OkHttpClient.Builder okHttpClientBuilder,
            final boolean resetDefaultInterceptors) {
        delegate.okHttpClientBuilder(okHttpClientBuilder, resetDefaultInterceptors);
        return this;
    }

    /**
     * <p>If you want to use your own setting, specify {@link Retrofit.Builder} instance.</p>
     *
     * <p>ref: {@link LineMessagingServiceBuilder#createDefaultRetrofitBuilder()} ()}.</p>
     */
    public LineMessagingClientBuilder retrofitBuilder(@NonNull Retrofit.Builder retrofitBuilder) {
        delegate.retrofitBuilder(retrofitBuilder);
        return this;
    }

    /**
     * Creates a new {@link LineMessagingService}.
     */
    public LineMessagingClient build() {
        return new LineMessagingClientImpl(delegate.build());
    }
}
