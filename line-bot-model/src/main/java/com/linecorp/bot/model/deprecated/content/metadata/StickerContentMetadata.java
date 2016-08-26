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

@ToString
@Getter
public class StickerContentMetadata {
    /**
     * The package ID of the sticker.
     */
    @JsonProperty("STKPKGID")
    private final String stkpkgid;

    /**
     * The sticker ID.
     */
    @JsonProperty("STKID")
    private final String stkid;

    /**
     * The sticker's version number.
     */
    @JsonProperty("STKVER")
    private final String stkver;

    /**
     * The text of the sticker.
     */
    @JsonProperty("STKTXT")
    private final String stktxt;

    @JsonCreator
    public StickerContentMetadata(@JsonProperty("STKPKGID") String stkpkgid, @JsonProperty("STKID") String stkid,
                                  @JsonProperty("STKVER") String stkver, @JsonProperty("STKTXT") String stktxt) {
        this.stkpkgid = stkpkgid;
        this.stkid = stkid;
        this.stkver = stkver;
        this.stktxt = stktxt;
    }
}
