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

import com.linecorp.bot.model.deprecated.content.metadata.RichMessageContentMetadata;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Deprecated
public class RichMessageContent extends AbstractContent {
    private final RichMessageContentMetadata contentMetadata;

    @JsonCreator
    public RichMessageContent(
            @JsonProperty("id") String id,
            @JsonProperty("from") String from,
            @JsonProperty("contentType") ContentType contentType,
            @JsonProperty("toType") RecipientType toType,
            @JsonProperty("downloadUrl") String downloadUrl,
            @JsonProperty("altText") String altText,
            @JsonProperty("markupJson") String markupJson) {
        super(id, from, contentType, toType);
        this.contentMetadata = new RichMessageContentMetadata(downloadUrl, altText, markupJson);
    }

    public RichMessageContent(RecipientType toType, String downloadUrl, String altText, String markupJson) {
        this(null, null, ContentType.RICH_MESSAGE, toType, downloadUrl, altText, markupJson);
    }
}
