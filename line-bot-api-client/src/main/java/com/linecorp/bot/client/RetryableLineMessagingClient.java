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

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.linecorp.bot.model.Broadcast;
import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.Narrowcast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.response.BotApiResponse;

public interface RetryableLineMessagingClient {
    /**
     * Send messages to users when you want to.
     *
     * <p>INFO: Use of the Push Message API is limited to certain plans.
     *
     * @see <a href="https://developers.line.me/en/reference/messaging-api/#send-push-message">//developers.line.me/en/reference/messaging-api/#send-push-message</a>
     */
    CompletableFuture<BotApiResponse> pushMessage(UUID retryKey, PushMessage pushMessage);

    /**
     * Send messages to multiple users at any time. <strong>IDs of groups or rooms cannot be used.</strong>
     *
     * <p>INFO: Only available for plans which support push messages.
     * Messages cannot be sent to groups or rooms.
     *
     * <p>INFO: Use IDs returned via the webhook event of source users. IDs of groups or rooms cannot be used.
     * Do not use the LINE ID found on the LINE app.</p>
     *
     * @see <a href="https://developers.line.me/en/reference/messaging-api/#send-multicast-messages">//developers.line.me/en/reference/messaging-api/#send-multicast-messages</a>
     */
    CompletableFuture<BotApiResponse> multicast(UUID retryKey, Multicast multicast);

    /**
     * Sends push messages to multiple users at any time.
     * Note: LINE@ accounts cannot call this API endpoint. Please migrate it to a LINE official account.
     * For more information, see <a href="https://developers.line.biz/en/docs/messaging-api/migrating-line-at/">
     * Migration of LINE@ accounts</a>.
     */
    CompletableFuture<BotApiResponse> broadcast(UUID retryKey, Broadcast broadcast);

    /**
     * Sends a push message to multiple users. You can specify recipients using attributes (such as age, gender,
     * OS, and region) or by retargeting (audiences). Messages cannot be sent to groups or rooms.
     *
     * <p>Note: LINE-@ accounts cannot call this API endpoint. Please migrate it to a LINE official account.
     * For more information, see <a href="https://developers.line.biz/en/docs/messaging-api/migrating-line-at/">
     * Migration of LINE@ accounts</a>.
     */
    CompletableFuture<BotApiResponse> narrowcast(UUID retryKey, Narrowcast broadcast);

    static RetryableLineMessagingClientBuilder builder(String channelToken) {
        return builder(FixedChannelTokenSupplier.of(channelToken));
    }

    static RetryableLineMessagingClientBuilder builder(ChannelTokenSupplier channelTokenSupplier) {
        return new RetryableLineMessagingClientBuilder().channelTokenSupplier(channelTokenSupplier);
    }
}
