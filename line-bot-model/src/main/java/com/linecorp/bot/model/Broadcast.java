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

package com.linecorp.bot.model;

import static java.util.Collections.singletonList;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.linecorp.bot.model.message.Message;

import lombok.Value;

/**
 * Sends push messages to multiple users at any time.
 */
@Value
public class Broadcast {

    /**
     * List of Message objects.
     *
     * <p>Max: 5
     */
    List<Message> messages;

    /**
     * Whether sends a push notification to message receivers or not. If {@literal true}, the user doesn't
     * receive a push notification when the message is sent. And if {@literal false}, the user receives a push
     * notification when the message is sent (unless they have disabled push notifications in LINE and/or their
     * device).
     */
    boolean notificationDisabled;

    public Broadcast(Message messages) {
        this(singletonList(messages), false);
    }

    public Broadcast(List<Message> messages) {
        this(messages, false);
    }

    public Broadcast(Message messages, boolean notificationDisabled) {
        this(singletonList(messages), notificationDisabled);
    }

    @JsonCreator
    public Broadcast(@JsonProperty("messages") List<Message> messages,
                     @JsonProperty("notificationDisabled") boolean notificationDisabled) {
        this.messages = messages;
        this.notificationDisabled = notificationDisabled;
    }
}
