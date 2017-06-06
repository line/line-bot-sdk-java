/*
 * Copyright 2016 LINE Corporation
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.NonNull;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public final class LineMessagingServiceBuilder {
    public static final String DEFAULT_API_END_POINT = "https://api.line.me/";
    public static final long DEFAULT_CONNECT_TIMEOUT = 10_000;
    public static final long DEFAULT_READ_TIMEOUT = 10_000;
    public static final long DEFAULT_WRITE_TIMEOUT = 10_000;

    private String apiEndPoint = DEFAULT_API_END_POINT;
    private long connectTimeout = DEFAULT_CONNECT_TIMEOUT;
    private long readTimeout = DEFAULT_READ_TIMEOUT;
    private long writeTimeout = DEFAULT_WRITE_TIMEOUT;
    private List<Interceptor> interceptors = new ArrayList<>();

    private OkHttpClient.Builder okHttpClientBuilder;
    private Retrofit.Builder retrofitBuilder;

    /**
     * Create a new {@link LineMessagingServiceBuilder} with specified given fixed channelToken.
     */
    public static LineMessagingServiceBuilder create(@NonNull String fixedChannelToken) {
        return create(FixedChannelTokenSupplier.of(fixedChannelToken));
    }

    /**
     * Create a new {@link LineMessagingServiceBuilder} with specified {@link ChannelTokenSupplier}.
     */
    public static LineMessagingServiceBuilder create(@NonNull ChannelTokenSupplier channelTokenSupplier) {
        return new LineMessagingServiceBuilder(defaultInterceptors(channelTokenSupplier));
    }

    private LineMessagingServiceBuilder(List<Interceptor> interceptors) {
        this.interceptors.addAll(interceptors);
    }

    /**
     * Set apiEndPoint.
     */
    public LineMessagingServiceBuilder apiEndPoint(@NonNull String apiEndPoint) {
        this.apiEndPoint = apiEndPoint;
        return this;
    }

    /**
     * Set connectTimeout in milliseconds.
     */
    public LineMessagingServiceBuilder connectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    /**
     * Set readTimeout in milliseconds.
     */
    public LineMessagingServiceBuilder readTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    /**
     * Set writeTimeout in milliseconds.
     */
    public LineMessagingServiceBuilder writeTimeout(long writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    /**
     * Add interceptor
     */
    public LineMessagingServiceBuilder addInterceptor(Interceptor interceptor) {
        this.interceptors.add(interceptor);
        return this;
    }

    /**
     * Add interceptor first
     */
    public LineMessagingServiceBuilder addInterceptorFirst(Interceptor interceptor) {
        this.interceptors.add(0, interceptor);
        return this;
    }

    /**
     * Remove all interceptors
     */
    public LineMessagingServiceBuilder removeAllInterceptors() {
        this.interceptors.clear();
        return this;
    }

    /**
     * <p>If you want to use your own setting, specify {@link OkHttpClient.Builder} instance.</p>
     *
     * @deprecated use {@link #okHttpClientBuilder(OkHttpClient.Builder, boolean)} instead.
     */
    @Deprecated
    public LineMessagingServiceBuilder okHttpClientBuilder(
            @NonNull final OkHttpClient.Builder okHttpClientBuilder) {
        return okHttpClientBuilder(okHttpClientBuilder, false);
    }

    /**
     * <p>If you want to use your own setting, specify {@link OkHttpClient.Builder} instance.</p>
     *
     * @param resetDefaultInterceptors If true, all default okhttp interceptors ignored.
     * You should insert authentication headers yourself.
     */
    public LineMessagingServiceBuilder okHttpClientBuilder(
            @NonNull final OkHttpClient.Builder okHttpClientBuilder,
            final boolean resetDefaultInterceptors) {
        this.okHttpClientBuilder = okHttpClientBuilder;
        if (resetDefaultInterceptors) {
            this.removeAllInterceptors();
        }
        return this;
    }

    /**
     * <p>If you want to use your own setting, specify {@link Retrofit.Builder} instance.</p>
     *
     * <p>ref: {@link LineMessagingServiceBuilder#createDefaultRetrofitBuilder()} ()}.</p>
     */
    public LineMessagingServiceBuilder retrofitBuilder(@NonNull Retrofit.Builder retrofitBuilder) {
        this.retrofitBuilder = retrofitBuilder;
        return this;
    }

    /**
     * Creates a new {@link LineMessagingService}.
     */
    public LineMessagingService build() {
        if (okHttpClientBuilder == null) {
            okHttpClientBuilder = new OkHttpClient.Builder();
        }

        interceptors.forEach(okHttpClientBuilder::addInterceptor);
        okHttpClientBuilder
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);

        final OkHttpClient okHttpClient = okHttpClientBuilder.build();

        if (retrofitBuilder == null) {
            retrofitBuilder = createDefaultRetrofitBuilder();
        }
        retrofitBuilder.client(okHttpClient);
        retrofitBuilder.baseUrl(apiEndPoint);
        final Retrofit retrofit = retrofitBuilder.build();

        return retrofit.create(LineMessagingService.class);
    }

    private static List<Interceptor> defaultInterceptors(final ChannelTokenSupplier channelTokenSupplier) {
        final Logger slf4jLogger = LoggerFactory.getLogger("com.linecorp.bot.client.wire");
        final HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor(message -> slf4jLogger.info("{}", message));
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return Arrays.asList(
                HeaderInterceptor.forChannelTokenSupplier(channelTokenSupplier),
                httpLoggingInterceptor
        );
    }

    private static Retrofit.Builder createDefaultRetrofitBuilder() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Register JSR-310(java.time.temporal.*) module and read number as millsec.
        objectMapper.registerModule(new JavaTimeModule())
                    .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);

        return new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(objectMapper));
    }
}
