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
 * Send messages to users, groups, and rooms at any time.
 */
@Value
@AllArgsConstructor
public class PushMessage {
    /**
     * ID of the receiver.
     */
    String to;

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

    /**
     * List of aggregation unit name. Case-sensitive.
     * This functions can only be used by corporate users who have submitted the required applications.
     *
     * <p>Max: 1</p>
     * <p>Maximum aggregation unit name length: 30 characters </p>
     * <p>Supported character types: Half-width alphanumeric characters and underscore</p>
     */
    List<String> customAggregationUnits;

    public PushMessage(String to, Message message) {
        this(to, Collections.singletonList(message), false, null);
    }

    public PushMessage(String to, List<Message> messages) {
        this(to, messages, false, null);
    }

    public PushMessage(String to, Message message, boolean notificationDisabled) {
        this(to, Collections.singletonList(message), notificationDisabled, null);
    }

    public PushMessage(String to, List<Message> messages, boolean notificationDisabled) {
        this(to, messages, notificationDisabled, null);
    }
}
