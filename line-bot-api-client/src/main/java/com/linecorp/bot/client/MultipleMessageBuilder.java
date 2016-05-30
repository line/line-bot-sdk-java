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

import java.util.List;

import com.linecorp.bot.client.exception.LineBotAPIException;
import com.linecorp.bot.model.content.AbstractContent;

import lombok.NonNull;

/**
 * Builder class for multiple message sending.
 */
public interface MultipleMessageBuilder {

    /**
     * Add text content
     *
     * @param message String you want to send. Messages can contain up to 1024 characters.
     */
    MultipleMessageBuilder addText(@NonNull String message);

    /**
     * Add content to the list of messages.
     */
    MultipleMessageBuilder add(@NonNull AbstractContent content);

    /**
     * Send messages to the MID.
     *
     * @param mid Target user's MID.
     */
    void send(@NonNull String mid) throws LineBotAPIException;

    /**
     * Send messages.
     *
     * @param mids Array of target users' MIDs. Max count: 150.
     * @throws LineBotAPIException
     */
    void send(List<String> mids) throws LineBotAPIException;
}
