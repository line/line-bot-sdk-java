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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = As.PROPERTY,
        property = "contentType",
        visible = true,
        defaultImpl = Object.class)
@JsonSubTypes({
                      @JsonSubTypes.Type(value = TextContent.class, name = "1"),
                      @JsonSubTypes.Type(value = ImageContent.class, name = "2"),
                      @JsonSubTypes.Type(value = VideoContent.class, name = "3"),
                      @JsonSubTypes.Type(value = AudioContent.class, name = "4"),
                      @JsonSubTypes.Type(value = LocationContent.class, name = "7"),
                      @JsonSubTypes.Type(value = StickerContent.class, name = "8"),
                      @JsonSubTypes.Type(value = ContactContent.class, name = "10"),
                      @JsonSubTypes.Type(value = RichMessageContent.class, name = "12")
              })
public abstract class AbstractContent implements Content {

    private final String id;
    private final String from;
    private final ContentType contentType;
    private final RecipientType toType;

    public AbstractContent(String id, String from, ContentType contentType, RecipientType toType) {
        this.id = id;
        this.from = from;
        this.contentType = contentType;
        this.toType = toType;
    }

    public ContentType getContentType() {
        return contentType;
    }

    /**
     * Type of recipient set in the to property. (1 = user)
     */
    public RecipientType getToType() {
        return toType;
    }

    public String getId() {
        return this.id;
    }

    public String getFrom() {
        return from;
    }
}
