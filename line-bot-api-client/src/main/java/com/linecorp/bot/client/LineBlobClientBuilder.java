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
public class LineBlobClientBuilder extends AbstractClientBuilder<LineBlobClientBuilder> {
    private static final ObjectMapper objectMapper = ModelObjectMapper.createNewObjectMapper();

    /**
     * Use {@link LineBlobClient#builder} to create instance.
     *
     * <p>Default value: {@link #apiEndPoint} = "https://api-data.line.me/".
     */
    @PackagePrivate
    LineBlobClientBuilder() {
        apiEndPoint(LineClientConstants.DEFAULT_BLOB_END_POINT);
    }

    /**
     * Creates a new {@link LineBlobService}.
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
     * Creates a new {@link LineBlobService}.
     */
    public LineBlobClient build() {
        return new LineBlobClientImpl(
                buildRetrofitIface(apiEndPoint, LineBlobService.class));
    }
}
