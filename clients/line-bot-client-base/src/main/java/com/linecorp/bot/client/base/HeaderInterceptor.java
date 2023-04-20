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

import java.io.IOException;

import com.linecorp.bot.client.base.channel.ChannelTokenSupplier;
import com.linecorp.bot.client.base.http.HttpChain;
import com.linecorp.bot.client.base.http.HttpInterceptor;
import com.linecorp.bot.client.base.http.HttpRequest;
import com.linecorp.bot.client.base.http.HttpResponse;

public class HeaderInterceptor implements HttpInterceptor {
    private static final String USER_AGENT =
            "line-botsdk-java/" + HeaderInterceptor.class.getPackage().getImplementationVersion();
    private final ChannelTokenSupplier channelTokenSupplier;

    private HeaderInterceptor(ChannelTokenSupplier channelTokenSupplier) {
        this.channelTokenSupplier = channelTokenSupplier;
    }

    public static HeaderInterceptor forChannelTokenSupplier(ChannelTokenSupplier channelTokenSupplier) {
        return new HeaderInterceptor(channelTokenSupplier);
    }

    @Override
    public HttpResponse intercept(HttpChain chain) throws IOException {
        final String channelToken = channelTokenSupplier.get();
        HttpRequest request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer " + channelToken)
                .addHeader("User-Agent", USER_AGENT)
                .build();
        return chain.proceed(request);
    }
}
