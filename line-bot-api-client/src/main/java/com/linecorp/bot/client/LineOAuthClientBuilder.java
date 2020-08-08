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

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.objectmapper.ModelObjectMapper;

import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.PackagePrivate;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@ToString
@Accessors(fluent = true)
public class LineOAuthClientBuilder {
    /**
     * Creates a {@link LineOAuthClient}. Use {@link LineOAuthClient#builder()} instead.
     */
    @PackagePrivate
    LineOAuthClientBuilder() {
    }

    /**
     * API Endpoint.
     *
     * <p>Default value = "https://api.line.me/".
     */
    private URI apiEndPoint = LineClientConstants.DEFAULT_API_END_POINT;

    /**
     * API Endpoint.
     *
     * <p>Default value = "https://api.line.me/".
     */
    public LineOAuthClientBuilder apiEndPoint(URI apiEndPoint) {
        this.apiEndPoint = requireNonNull(apiEndPoint, "apiEndPoint");
        return this;
    }

    /**
     * Connection timeout.
     *
     * <p>Default value = {@value LineClientConstants#DEFAULT_CONNECT_TIMEOUT_MILLIS}ms.
     */
    @Setter
    private long connectTimeout = LineClientConstants.DEFAULT_CONNECT_TIMEOUT_MILLIS;

    /**
     * Connection timeout.
     *
     * <p>Default value = {@value LineClientConstants#DEFAULT_READ_TIMEOUT_MILLIS}ms.
     */
    @Setter
    private long readTimeout = LineClientConstants.DEFAULT_READ_TIMEOUT_MILLIS;

    /**
     * Write timeout.
     *
     * <p>Default value = {@value LineClientConstants#DEFAULT_WRITE_TIMEOUT_MILLIS}ms.
     */
    @Setter
    private long writeTimeout = LineClientConstants.DEFAULT_WRITE_TIMEOUT_MILLIS;

    /**
     * Custom {@link Retrofit.Builder} used internally.
     *
     * <p>If you want to use your own setting, specify {@link Retrofit.Builder} instance.
     * Default builder is used in case of {@code null} (default).
     *
     * <p>To use this method, please add dependency to 'com.squareup.retrofit2:retrofit'.
     *
     * @see #createDefaultRetrofitBuilder()
     */
    @Setter
    private Retrofit.Builder retrofitBuilder;

    private OkHttpClient.Builder okHttpClientBuilder;

    /**
     * Custom interceptors.
     *
     * <p>You can add your own interceptors.
     */
    @Setter
    private List<Interceptor> additionalInterceptors = new ArrayList<>();

    /**
     * Set customized {@link OkHttpClient.Builder}.
     *
     * <p>In case of you need your own customized {@link OkHttpClient},
     * this builder allows specify {@link OkHttpClient.Builder} instance.
     */
    public LineOAuthClientBuilder okHttpClientBuilder(@NonNull OkHttpClient.Builder okHttpClientBuilder) {
        this.okHttpClientBuilder = okHttpClientBuilder;
        return this;
    }

    /**
     * Creates a new {@link LineOAuthService}.
     */
    private LineOAuthService buildRetrofit() {
        if (okHttpClientBuilder == null) {
            okHttpClientBuilder = new OkHttpClient.Builder();
        }

        if (additionalInterceptors != null) {
            additionalInterceptors.forEach(okHttpClientBuilder::addInterceptor);
        }
        okHttpClientBuilder.addInterceptor(buildLoggingInterceptor());

        // Set timeout.
        okHttpClientBuilder
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);

        final OkHttpClient okHttpClient = okHttpClientBuilder.build();

        if (retrofitBuilder == null) {
            retrofitBuilder = createDefaultRetrofitBuilder();
        }
        retrofitBuilder.client(okHttpClient);
        retrofitBuilder.baseUrl(apiEndPoint.toString());

        final Retrofit retrofit = retrofitBuilder.build();

        return retrofit.create(LineOAuthService.class);
    }

    private static Interceptor buildLoggingInterceptor() {
        final Logger slf4jLogger = LoggerFactory.getLogger("com.linecorp.bot.client.wire");

        return new HttpLoggingInterceptor(slf4jLogger::info)
                .setLevel(Level.BODY);
    }

    private static Retrofit.Builder createDefaultRetrofitBuilder() {
        final ObjectMapper objectMapper = ModelObjectMapper.createNewObjectMapper();

        return new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(objectMapper));
    }

    /**
     * Creates a new {@link LineOAuthClient}.
     */
    public LineOAuthClient build() {
        return new LineOAuthClientImpl(buildRetrofit());
    }
}
