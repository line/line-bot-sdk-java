/*
 * Copyright 2023 LINE Corporation
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

package com.linecorp.bot.client.base;

import static java.util.Objects.requireNonNull;

import java.net.Proxy;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.client.base.http.HttpAuthenticator;
import com.linecorp.bot.client.base.http.HttpChain;
import com.linecorp.bot.client.base.http.HttpInterceptor;
import com.linecorp.bot.client.base.http.HttpResponse;
import com.linecorp.bot.jackson.ModelObjectMapper;

import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;

public class ApiClientBuilder<T> {
    private final ObjectMapper objectMapper = ModelObjectMapper.createNewObjectMapper();
    private final Class<T> clientClass;
    private final ExceptionBuilder exceptionBuilder;

    public ApiClientBuilder(URI apiEndPoint, Class<T> clientClass, ExceptionBuilder exceptionBuilder) {
        this.apiEndPoint = apiEndPoint;
        this.clientClass = clientClass;
        this.exceptionBuilder = exceptionBuilder;
    }

    /**
     * API Endpoint.
     */
    private URI apiEndPoint;

    /**
     * Connection timeout.
     *
     * <p>Default value = 10 seconds.
     */
    private Duration connectTimeout = Duration.ofSeconds(10);

    /**
     * Connection timeout.
     *
     * <p>Default value = 10 seconds.
     */
    private Duration readTimeout = Duration.ofSeconds(10);

    /**
     * Write timeout.
     *
     * <p>Default value = 10 seconds.
     */
    private Duration writeTimeout = Duration.ofSeconds(10);

    /**
     * Custom interceptors.
     *
     * <p>You can add your own interceptors.
     */
    private List<Interceptor> additionalInterceptors = new ArrayList<>();

    private Proxy proxy;

    private HttpAuthenticator proxyAuthenticator;

    private Integer maxRequests = 64;

    private Integer maxRequestsPerHost = 5;

    /**
     * API Endpoint.
     */
    public ApiClientBuilder<T> apiEndPoint(URI apiEndPoint) {
        this.apiEndPoint = requireNonNull(apiEndPoint, "apiEndPoint");
        return this;
    }

    /**
     * Fail on unknown properties while JSON deserialization.
     * As a result,
     */
    public ApiClientBuilder<T> failOnUnknownProperties(Boolean failOnUnknownProperties) {
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                failOnUnknownProperties);
        return this;
    }

    public ApiClientBuilder<T> connectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public ApiClientBuilder<T> readTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public ApiClientBuilder<T> writeTimeout(Duration writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    public ApiClientBuilder<T> addInterceptor(HttpInterceptor interceptor) {
        this.additionalInterceptors.add(chain -> {
            HttpChain httpChain = new HttpChain(chain);
            HttpResponse httpResponse = interceptor.intercept(httpChain);
            return httpResponse.toOkHttpResponse();
        });
        return this;
    }

    /**
     * The maximum number of requests to execute concurrently.
     * Default: 64
     */
    public ApiClientBuilder<T> maxRequests(Integer maxRequests) {
        this.maxRequests = maxRequests;
        return this;
    }

    /**
     * The maximum number of requests for each host to execute concurrently.
     * Default: 5
     */
    // https://square.github.io/okhttp/4.x/okhttp/okhttp3/-dispatcher/max-requests-per-host/
    public ApiClientBuilder<T> maxRequestsPerHost(Integer maxRequestsPerHost) {
        this.maxRequestsPerHost = maxRequestsPerHost;
        return this;
    }

    private static Interceptor buildLoggingInterceptor() {
        final Logger slf4jLogger = LoggerFactory.getLogger("com.linecorp.bot.client.wire");

        return new HttpLoggingInterceptor(slf4jLogger::info)
                .setLevel(Level.BODY);
    }

    public ApiClientBuilder<T> proxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    public ApiClientBuilder<T> proxyAuthenticator(HttpAuthenticator proxyAuthenticator) {
        this.proxyAuthenticator = proxyAuthenticator;
        return this;
    }

    // visible for testing
    OkHttpClient.Builder createBuilder() {
        return new OkHttpClient.Builder();
    }

    // visible for testing
    Dispatcher createDispatcher() {
        return new Dispatcher();
    }

    /**
     * Creates a new Client.
     */
    public T build() {
        OkHttpClient.Builder okHttpClientBuilder = createBuilder();
        additionalInterceptors.forEach(okHttpClientBuilder::addInterceptor);
        okHttpClientBuilder.addInterceptor(buildLoggingInterceptor());

        // Set timeout.
        okHttpClientBuilder
                .connectTimeout(connectTimeout)
                .readTimeout(readTimeout)
                .writeTimeout(writeTimeout);

        // configure dispatcher
        Dispatcher dispatcher = createDispatcher();
        dispatcher.setMaxRequests(maxRequests);
        dispatcher.setMaxRequestsPerHost(maxRequestsPerHost);
        okHttpClientBuilder.dispatcher(dispatcher);

        // Error handling.
        okHttpClientBuilder.addInterceptor(chain -> {
            Request request = chain.request();
            Response response = chain.proceed(request);
            if (response.isSuccessful()) {
                return response;
            } else {
                throw this.exceptionBuilder.build(response);
            }
        });

        if (this.proxy != null) {
            okHttpClientBuilder.proxy(this.proxy);
        }

        if (this.proxyAuthenticator != null) {
            okHttpClientBuilder.proxyAuthenticator((route, response) ->
                    this.proxyAuthenticator.authenticate(new HttpResponse(response))
                            .toOkHttpRequest());
        }

        final Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(BotAwareJacksonConverter.create(objectMapper));
        retrofitBuilder.client(okHttpClientBuilder.build());
        retrofitBuilder.addCallAdapterFactory(new ResultCallAdapterFactory());
        retrofitBuilder.baseUrl(apiEndPoint.toString());

        final Retrofit retrofit = retrofitBuilder.build();

        return retrofit.create(clientClass);
    }

    @Override
    public String toString() {
        return "ApiClientBuilder{" +
                "objectMapper=" + objectMapper +
                ", clientClass=" + clientClass +
                ", exceptionBuilder=" + exceptionBuilder +
                ", apiEndPoint=" + apiEndPoint +
                ", connectTimeout=" + connectTimeout +
                ", readTimeout=" + readTimeout +
                ", writeTimeout=" + writeTimeout +
                ", additionalInterceptors=" + additionalInterceptors +
                ", maxRequests=" + maxRequests +
                ", maxRequestsPerHost=" + maxRequestsPerHost +
                '}';
    }
}
