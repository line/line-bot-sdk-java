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
    public static LineBotClientBuilder create(String channelId, String channelSecret) {
        return new LineBotClientBuilder(channelId, channelSecret);
    }

    private final String channelId;

    private final String channelSecret;

    private String apiEndPoint = DefaultLineBotClient.DEFAULT_API_END_POINT;

    private Long sendingMessageChannelId = DefaultLineBotClient.DEFAULT_SENDING_MESSAGE_CHANNEL_ID;

    private String sendingMessageEventId = DefaultLineBotClient.DEFAULT_SENDING_MESSAGE_EVENT_ID;

    private String sendingMultipleMessagesEventId =
            DefaultLineBotClient.DEFAULT_SENDING_MULTIPLE_MESSAGES_EVENT_ID;

    private HttpClientBuilder httpClientBuilder;

    private LineBotClientBuilder(@NonNull String channelId,
                                 @NonNull String channelSecret) {
        this.channelId = channelId;
        this.channelSecret = channelSecret;
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
     * Sets the channel id to send a message.
     */
    public LineBotClientBuilder sendingMessageChannelId(long sendingMessageChannelId) {
        this.sendingMessageChannelId = sendingMessageChannelId;
        return this;
    }

    /**
     * Sets the event type to send a single message.
     */
    public LineBotClientBuilder sendingMessageEventId(@NonNull String sendingMessageEventId) {
        this.sendingMessageEventId = sendingMessageEventId;
        return this;
    }

    /**
     * Sets the event type to send multiple messages.
     */
    public LineBotClientBuilder sendingMultipleMessagesEventId(@NonNull String sendingMultipleMessagesEventId) {
        this.sendingMultipleMessagesEventId = sendingMultipleMessagesEventId;
        return this;
    }

    /**
     * Creates a new {@link LineBotClient}.
     */
    public LineBotClient build() {
        return new DefaultLineBotClient(
                channelId,
                channelSecret,
                apiEndPoint,
                sendingMessageChannelId,
                sendingMessageEventId,
                sendingMultipleMessagesEventId,
                httpClientBuilder
        );
    }
}
