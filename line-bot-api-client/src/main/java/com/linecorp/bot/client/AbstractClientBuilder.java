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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;

@ToString
@Accessors(fluent = true)
@Getter
@FieldDefaults(level = AccessLevel.PROTECTED)
@SuppressWarnings({ "VisibilityModifier", "SummaryJavadoc" })
class AbstractClientBuilder<T extends AbstractClientBuilder<?>> {
    /**
     * API Endpoint.
     *
     * <p>Default value = "https://api.line.me/".
     */
    protected URI apiEndPoint = LineClientConstants.DEFAULT_API_END_POINT;

    public T apiEndPoint(URI apiEndPoint) {
        this.apiEndPoint = apiEndPoint;
        return thisAsT();
    }

    /**
     * Connection timeout.
     *
     * <p>Default value = {@value LineClientConstants#DEFAULT_CONNECT_TIMEOUT_MILLIS}ms.
     */
    protected long connectTimeout = LineClientConstants.DEFAULT_CONNECT_TIMEOUT_MILLIS;

    /**
     * @see #connectTimeout
     */
    public T connectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
        return thisAsT();
    }

    /**
     * Connection timeout.
     *
     * <p>Default value = {@value LineClientConstants#DEFAULT_READ_TIMEOUT_MILLIS}ms.
     */
    protected long readTimeout = LineClientConstants.DEFAULT_READ_TIMEOUT_MILLIS;

    /**
     * @see #readTimeout
     */
    public T readTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
        return thisAsT();
    }

    /**
     * Write timeout.
     *
     * <p>Default value = {@value LineClientConstants#DEFAULT_WRITE_TIMEOUT_MILLIS}ms.
     */
    protected long writeTimeout = LineClientConstants.DEFAULT_WRITE_TIMEOUT_MILLIS;

    /**
     * @see #writeTimeout
     */
    public T writeTimeout(long writeTimeout) {
        this.writeTimeout = writeTimeout;
        return thisAsT();
    }

    /**
     * Channel token supplier of this client.
     *
     * <p>MUST BE NULL except you configured your own
     */
    protected ChannelTokenSupplier channelTokenSupplier;

    /**
     * @see #channelTokenSupplier
     */
    public T channelTokenSupplier(ChannelTokenSupplier channelTokenSupplier) {
        this.channelTokenSupplier = channelTokenSupplier;
        return thisAsT();
    }

    /**
     * Set fixed channel token. This overwrites {@link #channelTokenSupplier(ChannelTokenSupplier)}.
     */
    public T channelToken(String channelToken) {
        channelTokenSupplier(FixedChannelTokenSupplier.of(channelToken));
        return thisAsT();
    }

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
    protected Retrofit.Builder retrofitBuilder;

    /**
     * @see #retrofitBuilder
     */
    public T retrofitBuilder(Builder retrofitBuilder) {
        this.retrofitBuilder = retrofitBuilder;
        return thisAsT();
    }

    /**
     * Add authentication header.
     *
     * <p>Default = {@value}. If you manage authentication header yourself, set to {@doe false}.
     */
    protected boolean addAuthenticationHeader = true;

    /**
     * @see #addAuthenticationHeader
     */
    public T addAuthenticationHeader(boolean addAuthenticationHeader) {
        this.addAuthenticationHeader = addAuthenticationHeader;
        return thisAsT();
    }

    protected OkHttpClient.Builder okHttpClientBuilder;

    /**
     * @see #okHttpClientBuilder
     */
    public AbstractClientBuilder<T> okHttpClientBuilder(OkHttpClient.Builder okHttpClientBuilder) {
        this.okHttpClientBuilder = okHttpClientBuilder;
        return this;
    }

    /**
     * Custom interceptors.
     *
     * <p>You can add your own interceptors.
     *
     * <p>Note: Authentication interceptor is automatically added by default.
     *
     * @see #addAuthenticationHeader(boolean)
     */
    protected List<Interceptor> additionalInterceptors = new ArrayList<>();

    /**
     * @see #additionalInterceptors
     */
    public T additionalInterceptors(List<Interceptor> additionalInterceptors) {
        this.additionalInterceptors = additionalInterceptors;
        return thisAsT();
    }

    ///////////////////

    @SuppressWarnings("unchecked")
    T thisAsT() {
        return (T) this;
    }
}
