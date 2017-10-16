package com.linecorp.bot.client;

import lombok.NonNull;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class LineMessagingClientBuilder {
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
