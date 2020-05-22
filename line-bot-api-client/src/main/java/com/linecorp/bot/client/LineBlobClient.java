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

import java.util.concurrent.CompletableFuture;

import com.linecorp.bot.model.response.BotApiResponse;

/**
 * API client of blob (binary large object).
 */
public interface LineBlobClient {
    /**
     * Download image, video, and audio data sent from users.
     *
     * @see <a href="https://developers.line.me/en/reference/messaging-api/#get-content">//developers.line.me/en/reference/messaging-api/#get-content</a>
     */
    CompletableFuture<MessageContentResponse> getMessageContent(String messageId);

    /**
     * Download rich menu image.
     *
     * @see <a href="https://developers.line.me/en/docs/messaging-api/reference/#download-rich-menu-image">//developers.line.me/en/docs/messaging-api/reference/#download-rich-menu-image</a>
     */
    CompletableFuture<MessageContentResponse> getRichMenuImage(String richMenuId);

    /**
     * Set RichMenu image.
     *
     * @see <a href="https://developers.line.me/en/docs/messaging-api/reference/#upload-rich-menu-image">//developers.line.me/en/docs/messaging-api/reference/#upload-rich-menu-image</a>
     */
    CompletableFuture<BotApiResponse> setRichMenuImage(
            String richMenuId, String contentType, byte[] content);

    static LineBlobClientBuilder builder(String channelToken) {
        return builder(FixedChannelTokenSupplier.of(channelToken));
    }

    static LineBlobClientBuilder builder(ChannelTokenSupplier channelTokenSupplier) {
        return new LineBlobClientBuilder().channelTokenSupplier(channelTokenSupplier);
    }
}
