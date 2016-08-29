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

import java.util.Collections;
import java.util.List;

import com.linecorp.bot.client.exception.LineBotAPIException;
import com.linecorp.bot.client.exception.LineBotAPIJsonProcessingException;
import com.linecorp.bot.model.event.CallbackRequest;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.response.BotApiResponse;

import lombok.NonNull;

/**
 * The line bot client interface.
 */
public interface LineBotClient {
    /**
     * Send event request to the server.
     *
     * @param messages List of messages
     *
     * @return API response bean.
     */
    BotApiResponse reply(List<String> to, List<Message> messages)
            throws LineBotAPIException;

    BotApiResponse push(List<String> to, List<Message> messages)
            throws LineBotAPIException;

    default BotApiResponse push(String to, Message messages)
            throws LineBotAPIException {
        return push(Collections.singletonList(to), Collections.singletonList(messages));
    }

    /**
     * Use the following API to retrieve the content of a user's message which is an image or video file.
     * <p>
     * Note: Contents are only stored on our servers for a certain period of time, after which they can no longer be
     * received. We are unable to provide details about how long contents are stored.
     *
     * @param messageId The message ID
     */
    CloseableMessageContent getMessageContent(String messageId) throws LineBotAPIException;

    /**
     * When a user sends a message to the BOT API server via the LINE platform using "Receiving messages", you will
     * receive a thumbnail preview of the message if the message is an image or video file.
     * <p>
     * Note: Content is only stored on our servers for a certain period of time, after which they can no longer be
     * received. We are unable to provide details about how long content is stored.
     *
     * @param messageId The message ID
     */
    CloseableMessageContent getPreviewMessageContent(String messageId) throws LineBotAPIException;

    /**
     * The profile information of any specified user can be obtained.
     *
     //     * @param mids Required. Lists the MIDs of the users whose information is to be retrieved, separated by commas.
     */
//    UserProfileResponse getUserProfile(Collection<String> mids) throws LineBotAPIException;

    boolean validateSignature(@NonNull String jsonText, @NonNull String headerSignature) throws LineBotAPIException;

    boolean validateSignature(@NonNull byte[] jsonText, @NonNull String headerSignature) throws LineBotAPIException;

    byte[] createSignature(@NonNull byte[] jsonText) throws LineBotAPIException;

    /**
     * Reads {@link CallbackRequest} from the given json.
     *
     * @param jsonText The byte array to be parsed.
     * @return parsed {@link CallbackRequest} object.
     */
    CallbackRequest readCallbackRequest(@NonNull byte[] jsonText) throws LineBotAPIJsonProcessingException;

    /**
     * Reads {@link CallbackRequest} from the given json.
     *
     * @param jsonText The text to be parsed.
     * @return parsed {@link CallbackRequest} object.
     */
    CallbackRequest readCallbackRequest(@NonNull String jsonText) throws LineBotAPIJsonProcessingException;

}
