/*
 * Copyright 2018 LINE Corporation
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
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;

import com.linecorp.bot.model.message.Message;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Send messages to multiple users, groups, and rooms at any time.
 */
@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class Multicast {
    /**
     * IDs of the receivers.
     *
     * <p>Max: 150
     *
     * <p>INFO: Use IDs returned via the webhook event of source users. IDs of groups or rooms cannot be used.
     */
    private final Set<String> to;

    /**
     * List of Message objects.
     *
     * <p>Max: 5
     */
    private final List<Message> messages;

    public Multicast(final Set<String> to, final Message message) {
        this.to = to;
        this.messages = Collections.singletonList(message);
    }
}
