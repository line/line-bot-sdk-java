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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

/**
 * Interface of Message object.
 *
 * <h2>JSON Deserialization</h2>
 *
 * <p>If you want serialize/deserialize of this object, please use Jackson's ObjectMapper with
 *
 * <pre>.registerModule(new <a href="https://github.com/FasterXML/jackson-modules-java8/tree/master/parameter-names">ParameterNamesModule</a>());</pre>
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(TextMessage.class),
        @JsonSubTypes.Type(ImageMessage.class),
        @JsonSubTypes.Type(StickerMessage.class),
        @JsonSubTypes.Type(LocationMessage.class),
        @JsonSubTypes.Type(AudioMessage.class),
        @JsonSubTypes.Type(VideoMessage.class),
        @JsonSubTypes.Type(ImagemapMessage.class),
        @JsonSubTypes.Type(TemplateMessage.class),
})
public interface Message {
}
