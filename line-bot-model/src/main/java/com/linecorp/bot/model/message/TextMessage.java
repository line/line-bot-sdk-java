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

package com.linecorp.bot.model.message;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.message.quickreply.QuickReply;
import com.linecorp.bot.model.message.sender.Sender;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@JsonTypeName("text")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonDeserialize(builder = TextMessage.TextMessageBuilder.class)
public class TextMessage implements Message {
    @NonNull String text;
    QuickReply quickReply;
    Sender sender;

    /**
     * Constructor without {@link #quickReply} parameter.
     *
     * <p>If you want use {@link QuickReply}, please use {@link #builder()} instead.
     */
    public TextMessage(final String text) {
        this(text, null, null);
    }

    public TextMessage(final @NonNull String text, final QuickReply quickReply) {
        this(text, quickReply, null);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class TextMessageBuilder {
    }
}
