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
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.objectmapper.ModelObjectMapper;

import lombok.NonNull;
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
public class ManageAudienceClientBuilder extends AbstractClientBuilder<ManageAudienceClientBuilder> {
    private static final ObjectMapper objectMapper = ModelObjectMapper.createNewObjectMapper();

    /**
     * Use {@link ManageAudienceClient#builder()} to create instance.
     */
    @PackagePrivate
    ManageAudienceClientBuilder() {
    }

    /**
     * Set customized OkHttpClient.Builder.
     *
     * <p>In case of you need your own customized {@link OkHttpClient},
     * this builder allows specify {@link OkHttpClient.Builder} instance.
     *
     * <p>To use this method, please add dependency to 'com.squareup.retrofit2:retrofit'.
     *
     * @param addAuthenticationHeader If true, all default okhttp interceptors ignored.
     *         You should insert authentication headers yourself.
     */
    public ManageAudienceClientBuilder okHttpClientBuilder(
            final @NonNull OkHttpClient.Builder okHttpClientBuilder,
            final boolean addAuthenticationHeader) {
        this.okHttpClientBuilder = okHttpClientBuilder;
        this.addAuthenticationHeader = addAuthenticationHeader;

        return this;
    }

    /**
     * Creates a new {@link LineMessagingService}.
     */
    <T> T buildRetrofitIface(URI apiEndPoint, Class<T> retrofitIFace) {
        if (okHttpClientBuilder == null) {
            okHttpClientBuilder = new OkHttpClient.Builder();
        }

        // Add interceptors.
        if (addAuthenticationHeader) {
            okHttpClientBuilder.addInterceptor(buildAuthenticationInterceptor(channelTokenSupplier));
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

        return retrofit.create(retrofitIFace);
    }

    static HeaderInterceptor buildAuthenticationInterceptor(ChannelTokenSupplier channelTokenSupplier) {
        requireNonNull(channelTokenSupplier, "channelTokenSupplier");
        return HeaderInterceptor.forChannelTokenSupplier(channelTokenSupplier);
    }

    static Interceptor buildLoggingInterceptor() {
        final Logger slf4jLogger = LoggerFactory.getLogger("com.linecorp.bot.client.wire");

        return new HttpLoggingInterceptor(slf4jLogger::info)
                .setLevel(Level.BODY);
    }

    static Retrofit.Builder createDefaultRetrofitBuilder() {
        return new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(objectMapper));
    }

    /**
     * Creates a new {@link LineMessagingService}.
     */
    public ManageAudienceClient build() {
        return new ManageAudienceClientImpl(
                buildRetrofitIface(apiEndPoint, ManageAudienceService.class));
    }
}
