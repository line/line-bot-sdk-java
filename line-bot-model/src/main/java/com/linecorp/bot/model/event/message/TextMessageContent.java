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

package com.linecorp.bot.model.event.message;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;

/**
 * Message content for text.
 */
@JsonTypeName("text")
@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = TextMessageContent.TextMessageContentBuilder.class)
public class TextMessageContent implements MessageContent {
    @JsonPOJOBuilder(withPrefix = "")
    public static class TextMessageContentBuilder {
        // Providing builder instead of public constructor. Class body is filled by lombok.
    }

    String id;

    /**
     * Message text.
     */
    String text;

    /**
     * One or more LINE emoji.
     */
    List<Emoji> emojis;

    /**
     * Mention information.
     */
    Mention mention;

    @Value
    @Builder(toBuilder = true)
    @JsonDeserialize(builder = TextMessageContent.Emoji.EmojiBuilder.class)
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
         * The length of the LINE emoji string. For LINE emoji (hello), 7 is the length.
         */
        int length;

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

    @Value
    @Builder(toBuilder = true)
    @JsonDeserialize(builder = TextMessageContent.Mention.MentionBuilder.class)
    public static class Mention {

        /**
         * List of mentioned user information, max of 20.
         */
        List<Mentionee> mentionees;

        @JsonPOJOBuilder(withPrefix = "")
        public static class MentionBuilder {
            // Providing builder instead of public constructor. Class body is filled by lombok.
        }

        @Value
        @Builder(toBuilder = true)
        @JsonDeserialize(builder = TextMessageContent.Mention.Mentionee.MentioneeBuilder.class)
        public static class Mentionee {

            /**
             * Index position of the user mention for a character in text,
             * with the first character being at position 0.
             */
            int index;

            /**
             * The length of the text of the mentioned user.
             * For a mention @example, 8 is the length.
             */
            int length;

            /**
             * User ID of the mentioned user.
             * Only returned if the user consents to the
             * LINE Official Account obtaining their user profile information.
             * Or else will be null.
             */
            String userId;

            @JsonPOJOBuilder(withPrefix = "")
            public static class MentioneeBuilder {
                // Providing builder instead of public constructor. Class body is filled by lombok.
            }
        }
    }
}
