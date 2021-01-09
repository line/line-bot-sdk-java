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

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.objectmapper.StringArrayOrNullDeserializer;

import lombok.Builder;
import lombok.Value;

/**
 * Message content for sticker type.
 */
@JsonTypeName("sticker")
@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = StickerMessageContent.StickerMessageContentBuilder.class)
public class StickerMessageContent implements MessageContent {
    @JsonPOJOBuilder(withPrefix = "")
    public static class StickerMessageContentBuilder implements StickerMessageContentBuilderMeta {
        // Providing builder instead of public constructor. Class body is filled by lombok.
    }

    @SuppressWarnings("InterfaceMayBeAnnotatedFunctional")
    private interface StickerMessageContentBuilderMeta {
        @SuppressWarnings("unused")
        @JsonDeserialize(using = StringArrayOrNullDeserializer.class)
        StickerMessageContentBuilder keywords(List<String> keywords);
    }

    /**
     * Resource type of a Sticker message content.
     *
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#wh-sticker">//developers.line.biz/en/reference/messaging-api/#wh-sticker</a>
     */
    public enum StickerResourceType {
        /**
         * Image sticker.
         */
        STATIC,
        /**
         * Animated sticker.
         */
        ANIMATION,
        /**
         * Sticker with sound.
         */
        SOUND,
        /**
         * Animated sticker with sound.
         */
        ANIMATION_SOUND,
        /**
         * Pop-up sticker.
         */
        POPUP,
        /**
         * Pop-up sticker with sound.
         */
        POPUP_SOUND,
        /**
         * Custom sticker. You can't retrieve the sticker's custom text with the Messaging API.
         */
        NAME_TEXT,
        /**
         * Message sticker. You can't retrieve the sticker's custom text with the Messaging API.
         */
        PER_STICKER_TEXT,
        @JsonEnumDefaultValue
        UNKNOWN // For implementation of new sticker resource type in the future!
    }

    String id;
    String packageId;
    String stickerId;
    StickerResourceType stickerResourceType;

    /**
     * Experimental feature.
     * List of keywords describing the sticker.
     * If the type change in the future, this field will become null.
     */
    List<String> keywords;
}
