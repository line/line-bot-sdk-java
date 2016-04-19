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

package com.linecorp.bot.model.content;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.linecorp.bot.model.content.metadata.AudioContentMetadata;

import lombok.Getter;
import lombok.ToString;

/**
 * To send a voice message, place the audio file on your BOT API server, then relay the audio file to the LINE
 * platform.
 * The required values are as follows.
 */

@ToString
@Getter
public class AudioContent extends AbstractContent {
    /**
     * URL of audio file. The "m4a" format is recommended.
     */
    private final String originalContentUrl;
    private final AudioContentMetadata contentMetadata;

    @JsonCreator
    public AudioContent(@JsonProperty("id") String id,
                        @JsonProperty("from") String from,
                        @JsonProperty("contentType") ContentType contentType,
                        @JsonProperty("toType") RecipientType toType,
                        @JsonProperty("originalContentUrl") String originalContentUrl,
                        @JsonProperty("contentMetadata") AudioContentMetadata contentMetadata) {
        super(id, from, contentType, toType);
        this.originalContentUrl = originalContentUrl;
        this.contentMetadata = contentMetadata;
    }

    public AudioContent(RecipientType toType,
                        String originalContentUrl,
                        String audlen) {
        this(null, null, ContentType.AUDIO, toType, originalContentUrl, new AudioContentMetadata(audlen));
    }
}
