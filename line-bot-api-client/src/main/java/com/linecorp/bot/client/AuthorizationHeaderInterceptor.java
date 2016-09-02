package com.linecorp.bot.client;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This interceptor injects Authorization header
 */
public class AuthorizationHeaderInterceptor implements Interceptor {
    private final String channelToken;

    public AuthorizationHeaderInterceptor(String channelToken) {
        this.channelToken = channelToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                               // will be deprecate
                               .addHeader("X-LINE-ChannelToken", channelToken)
                               .addHeader("Authorization", "Bearer " + channelToken)
                               .build();
        return chain.proceed(request);
    }
}
