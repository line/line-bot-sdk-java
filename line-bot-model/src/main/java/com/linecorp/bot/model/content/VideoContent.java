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

import lombok.Getter;
import lombok.ToString;

/**
 * To send a video, place a video file and a thumbnail image to be used as preview on your BOT API server, then relay
 * the video to the LINE platform. The required values are as follows.
 */

@ToString
@Getter
public class VideoContent extends AbstractContent {
    /**
     * URL of the movie. The "mp4" format is recommended.
     */
    private final String originalContentUrl;
    /**
     * URL of thumbnail image used as a preview.
     */
    private final String previewImageUrl;

    @JsonCreator
    public VideoContent(@JsonProperty("id") String id,
                        @JsonProperty("from") String from,
                        @JsonProperty("contentType") ContentType contentType,
                        @JsonProperty("toType") RecipientType toType,
                        @JsonProperty("originalContentUrl") String originalContentUrl,
                        @JsonProperty("previewImageUrl") String previewImageUrl) {
        super(id, from, contentType, toType);
        this.originalContentUrl = originalContentUrl;
        this.previewImageUrl = previewImageUrl;
    }

    public VideoContent(RecipientType toType, String originalContentUrl, String previewImageUrl) {
        this(null, null, ContentType.VIDEO, toType, originalContentUrl, previewImageUrl);
    }
}
