/*
 * Copyright 2022 LINE Corporation
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
 * Request body of the message validation.
 */
@Value
public class ValidateMessage {

    /**
     * List of Message objects.
     *
     * <p>Max: 5
     */
    List<Message> messages;

    public ValidateMessage(Message messages) {
        this(singletonList(messages));
    }

    @JsonCreator
    public ValidateMessage(@JsonProperty("messages") List<Message> messages) {
        this.messages = messages;
    }
}
