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

package com.linecorp.bot.model.deprecated.content.metadata;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RichMessageContentMetadata {
    @JsonProperty("DOWNLOAD_URL")
    private final String downloadUrl;

    @JsonProperty("SPEC_REV")
    private final String specRev;

    @JsonProperty("ALT_TEXT")
    private final String altText;

    /**
     * Escaped string of the JSON string representing the rich message object.
     */
    @JsonProperty("MARKUP_JSON")
    private final String markupJson;

    @JsonCreator
    public RichMessageContentMetadata(
            @JsonProperty("DOWNLOAD_URL") String downloadUrl,
            @JsonProperty("ALT_TEXT") String altText,
            @JsonProperty("MARKUP_JSON") String markupJson) {
        this.downloadUrl = downloadUrl;
        this.altText = altText;
        this.markupJson = markupJson;
        this.specRev = "1";
    }
}
