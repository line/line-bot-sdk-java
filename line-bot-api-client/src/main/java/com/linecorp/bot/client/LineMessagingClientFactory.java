package com.linecorp.bot.client;

import java.net.URI;

import lombok.Builder;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

@Builder
public class LineMessagingClientFactory {
    /**
     * API Endpoint.
     *
     * <p>Default value = "https://api.line.me/".
     */
    @Builder.Default URI apiEndPoint = LineClientConstants.DEFAULT_API_END_POINT;

    /**
     * Blob Endpoint.
     *
     * <p>Default value = "https://api-data.line.me/".
     */
    @Builder.Default URI blobEndPoint = LineClientConstants.DEFAULT_BLOB_END_POINT;

    /**
     * Connection timeout.
     *
     * <p>Default value = {@value LineClientConstants#DEFAULT_CONNECT_TIMEOUT_MILLIS}ms.
     */
    @Builder.Default long connectTimeout = LineClientConstants.DEFAULT_CONNECT_TIMEOUT_MILLIS;

    /**
     * Connection timeout.
     *
     * <p>Default value = {@value LineClientConstants#DEFAULT_READ_TIMEOUT_MILLIS}ms.
     */
    @Builder.Default long readTimeout = LineClientConstants.DEFAULT_READ_TIMEOUT_MILLIS;

    /**
     * Write timeout.
     *
     * <p>Default value = {@value LineClientConstants#DEFAULT_WRITE_TIMEOUT_MILLIS}ms.
     */
    @Builder.Default long writeTimeout = LineClientConstants.DEFAULT_WRITE_TIMEOUT_MILLIS;

    @Builder.Default Dispatcher dispatcher = new Dispatcher();
    @Builder.Default ConnectionPool connectionPool = new ConnectionPool();

    public LineMessagingClient createClient(ChannelTokenSupplier channelTokenSupplier) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .connectionPool(connectionPool);

        return new LineMessagingClientBuilder()
                .apiEndPoint(apiEndPoint)
                .blobEndPoint(blobEndPoint)
                .connectTimeout(connectTimeout)
                .writeTimeout(writeTimeout)
                .readTimeout(readTimeout)
                .channelTokenSupplier(channelTokenSupplier)
                .okHttpClientBuilder(okHttpClientBuilder, true)
                .build();
    }

    public LineMessagingClient createClient(String channelToken) {
        return this.createClient(FixedChannelTokenSupplier.of(channelToken));
    }
}
