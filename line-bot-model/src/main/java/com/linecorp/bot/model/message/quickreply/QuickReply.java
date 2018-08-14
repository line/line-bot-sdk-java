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

package com.linecorp.bot.model.message.quickreply;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

/**
 * QuickReply objects which contains {@link QuickReplyItem}.
 *
 * <p>When a user receives a message that contains quick reply buttons from a bot,
 * those buttons appear at the bottom of the chat screen.
 * The user can simply tap one of the buttons to reply to the bot.
 *
 * <p>The quick reply feature can be used in a one-on-one chat with a bot,
 * a group, and a room. You can set up to 13 quick reply buttons to a message of any type.
 *
 * @see <a href="https://developers.line.me/en/docs/messaging-api/using-quick-reply/">//developers.line.me/en/docs/messaging-api/using-quick-reply/</a>
 */
@Value
@Builder
@AllArgsConstructor(staticName = "items", access = AccessLevel.PUBLIC)
@JsonDeserialize(builder = QuickReply.QuickReplyBuilder.class)
public class QuickReply {
    @Singular
    List<QuickReplyItem> items;

    @JsonPOJOBuilder(withPrefix = "")
    public static class QuickReplyBuilder {}
}
