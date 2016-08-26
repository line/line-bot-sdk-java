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

import org.apache.http.impl.client.HttpClientBuilder;

import lombok.NonNull;

/**
 * A builder that creates a {@link LineBotClient}.
 * <pre>{@code
 * // Simple usage
 * LineBotClient client = LineBotClientBuilder
 *                          .create("YOUR_CHANNEL_ID", "YOUR_CHANNEL_SECRET", "YOUR_CHANNEL_MID")
 *                          .objectMapper(yourObjectMapper) // optional
 *                          .httpClientBuilder(yourHttpClientBuilder) // optional
 *                          .build();
 * }</pre>
 */
public final class LineBotClientBuilder {

    /**
     * Create a new {@link LineBotClientBuilder} with specified channel information.
     */
    public static LineBotClientBuilder create(String channelSecret, String channelToken) {
        return new LineBotClientBuilder(channelSecret, channelToken);
    }

    private final String channelSecret;
    private final String channelToken;

    private String apiEndPoint = DefaultLineBotClient.DEFAULT_API_END_POINT;

    private HttpClientBuilder httpClientBuilder;

    private LineBotClientBuilder(
            @NonNull String channelSecret,
            @NonNull String channelToken
    ) {
        this.channelSecret = channelSecret;
        this.channelToken = channelToken;
    }

    /**
     * Sets the LINE Bot API endpoint uri.
     */
    public LineBotClientBuilder apiEndPoint(@NonNull String apiEndPoint) {
        this.apiEndPoint = apiEndPoint;
        return this;
    }

    /**
     * Sets the http client builder to be used.
     */
    public LineBotClientBuilder httpClientBuilder(@NonNull HttpClientBuilder httpClientBuilder) {
        this.httpClientBuilder = httpClientBuilder;
        return this;
    }

    /**
     * Creates a new {@link LineBotClient}.
     */
    public LineBotClient build() {
        return new DefaultLineBotClient(
                channelSecret,
                channelToken,
                apiEndPoint,
                httpClientBuilder
        );
    }
}
