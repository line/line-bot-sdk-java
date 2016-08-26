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

package com.linecorp.bot.model.deprecated.content;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.linecorp.bot.model.deprecated.content.metadata.StickerContentMetadata;

import lombok.Getter;
import lombok.ToString;

/**
 * To send a sticker, the required values are as follows. You can use the stickers shown in the <a
 * href="https://developers.line.me/wp-content/uploads/2016/04/sticker_list.xlsx">sticker list</a>.
 */

@ToString
@Getter
@Deprecated
public class StickerContent extends AbstractContent {
    private final StickerContentMetadata contentMetadata;

    @JsonCreator
    public StickerContent(
            @JsonProperty("id") String id,
            @JsonProperty("from") String from,
            @JsonProperty("contentType") ContentType contentType,
            @JsonProperty("toType") RecipientType toType,
            @JsonProperty("contentMetadata") StickerContentMetadata contentMetadata) {
        super(id, from, contentType, toType);
        this.contentMetadata = contentMetadata;
    }

    public StickerContent(RecipientType toType, String stkpkgid, String stkid,
                          String stkver, String stktxt) {
        this(null, null, ContentType.STICKER, toType, new StickerContentMetadata(stkpkgid, stkid, stkver, stktxt));
    }
}
