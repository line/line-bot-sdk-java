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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
     * One or more LINE emoji.
     *
     * <p>Max: 20 LINE emoji
     */
    @JsonInclude(Include.NON_NULL)
    List<Emoji> emojis;

    /**
     * Constructor without {@link #quickReply} parameter.
     *
     * <p>If you want use {@link QuickReply}, please use {@link #builder()} instead.
     */
    public TextMessage(final String text) {
        this(text, null, null, null);
    }

    public TextMessage(final @NonNull String text, final QuickReply quickReply) {
        this(text, quickReply, null, null);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class TextMessageBuilder {
    }

    @Value
    @Builder(toBuilder = true)
    @JsonDeserialize(builder = Emoji.EmojiBuilder.class)
    public static class Emoji {
        /**
         * Index position for a character in text, with the first character being at position 0.
         *
         * <p>The specified position must correspond to a $ character,
         * which serves as a placeholder for the LINE emoji.
         * If you specify a position that doesn't contain a $ character, the API returns HTTP 400 Bad request.
         * See the <a href="https://developers.line.biz/en/reference/messaging-api/#text-message">text message example</a> for details.
         */
        int index;

        /**
         * Product ID for a set of LINE emoji.
         *
         * @see <a href="https://d.line-scdn.net/r/devcenter/sendable_line_emoji_list.pdf">Sendable LINE emoji list</a>
         */
        String productId;

        /**
         * ID for a LINE emoji inside a set.
         *
         * @see <a href="https://d.line-scdn.net/r/devcenter/sendable_line_emoji_list.pdf">Sendable LINE emoji list</a>
         */
        String emojiId;

        @JsonPOJOBuilder(withPrefix = "")
        public static class EmojiBuilder {
            // Providing builder instead of public constructor. Class body is filled by lombok.
        }
    }
}
