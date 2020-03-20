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

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.narrowcast.Filter;
import com.linecorp.bot.model.narrowcast.Limit;
import com.linecorp.bot.model.narrowcast.recipient.Recipient;

import lombok.Value;

/**
 * Sends a push message to multiple users. You can specify recipients using attributes (such as age, gender, OS,
 * and region) or by retargeting (audiences). Messages cannot be sent to groups or rooms.
 */
@Value
public class Narrowcast {

    /**
     * List of Message objects.
     *
     * <p>Max: 5
     */
    List<Message> messages;

    /**
     * Recipient objects represent audiences. You can specify recipients based on a combination of criteria
     * using logical operator objects.
     */
    Recipient recipient;

    /**
     * Demographic filter object. You can use friends' attributes to filter the list of recipients.
     * If this is omitted, messages will be sent to everyone – including users with attribute values of
     * "unknown".
     */
    Filter filter;

    /**
     * The maximum number of narrowcast messages to send. Use this parameter to limit the number of narrowcast
     * messages sent. The recipients will be chosen at random.
     */
    Limit limit;

    /**
     * Whether sends a push notification to message receivers or not. If {@literal true}, the user doesn't
     * receive a push notification when the message is sent. And if {@literal false}, the user receives a push
     * notification when the message is sent (unless they have disabled push notifications in LINE and/or their
     * device).
     */
    boolean notificationDisabled;

    public Narrowcast(Message message, Filter filter) {
        this(Collections.singletonList(message), null, filter, null, false);
    }

    @JsonCreator
    public Narrowcast(
            @JsonProperty("messages") List<Message> messages,
            @JsonProperty("recipient") Recipient recipient,
            @JsonProperty("filter") Filter filter,
            @JsonProperty("limit") Limit limit,
            @JsonProperty("notificationDisabled") boolean notificationDisabled) {
        this.messages = messages;
        this.recipient = recipient;
        this.filter = filter;
        this.limit = limit;
        this.notificationDisabled = notificationDisabled;
    }
}
