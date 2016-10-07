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

package com.linecorp.bot.model;

import java.util.Collections;
import java.util.List;

import com.linecorp.bot.model.message.Message;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * <p>
 * Respond to events from users, groups, and rooms.
 * </p>
 * <p>
 * Webhooks are used to notify you when an event occurs. For events that you can respond to, a replyToken is issued for replying to messages.
 * </p>
 * <p>
 * Because the replyToken becomes invalid after a certain period of time, responses should be sent as soon as a message is received. Reply tokens can only be used once.
 * </p>
 */
@Value
@AllArgsConstructor
public class ReplyMessage {
    /**
     * replyToken received via webhook
     */
    private final String replyToken;

    /**
     * List of messages.<br>
     * Max: 5
     */
    private final List<Message> messages;

    public ReplyMessage(String replyToken, Message message) {
        this(replyToken, Collections.singletonList(message));
    }
}
