/*
 * Copyright 2023 LINE Corporation
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


package com.linecorp.bot.webhook.model;

import java.time.Instant;

import java.util.Objects;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import com.linecorp.bot.webhook.model.MessageContent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 * StickerMessageContent
 */
@JsonTypeName("sticker")
@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record StickerMessageContent (
    /**
     * Message ID
     */

    @JsonProperty("id")
    String id,
    /**
     * Package ID
     */

    @JsonProperty("packageId")
    String packageId,
    /**
     * Sticker ID
     */

    @JsonProperty("stickerId")
    String stickerId,
    /**
     * Get stickerResourceType
     */

    @JsonProperty("stickerResourceType")
    StickerResourceType stickerResourceType,
    /**
     * Array of up to 15 keywords describing the sticker. If a sticker has 16 or more keywords, a random selection of 15 keywords will be returned. The keyword selection is random for each event, so different keywords may be returned for the same sticker. 
     */

    @JsonProperty("keywords")
    List<String> keywords,
    /**
     * Any text entered by the user. This property is only included for message stickers. Max character limit: 100 
     */

    @JsonProperty("text")
    String text,
    /**
     * Quote token to quote this message. 
     */

    @JsonProperty("quoteToken")
    String quoteToken,
    /**
     * Message ID of a quoted message. Only included when the received message quotes a past message.  
     */

    @JsonProperty("quotedMessageId")
    String quotedMessageId
) implements MessageContent  {



    /**
     * Gets or Sets stickerResourceType
     */
    public enum StickerResourceType {
      @JsonProperty("STATIC")
      STATIC,
      @JsonProperty("ANIMATION")
      ANIMATION,
      @JsonProperty("SOUND")
      SOUND,
      @JsonProperty("ANIMATION_SOUND")
      ANIMATION_SOUND,
      @JsonProperty("POPUP")
      POPUP,
      @JsonProperty("POPUP_SOUND")
      POPUP_SOUND,
      @JsonProperty("CUSTOM")
      CUSTOM,
      @JsonProperty("MESSAGE")
      MESSAGE,
      @JsonProperty("NAME_TEXT")
      NAME_TEXT,
      @JsonProperty("PER_STICKER_TEXT")
      PER_STICKER_TEXT,
      @JsonEnumDefaultValue
      UNDEFINED;
    }






}

