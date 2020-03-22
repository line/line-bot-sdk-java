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

package com.linecorp.bot.model.message.imagemap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

/**
 * Size of base image.
 */
@Value
public class ImagemapBaseSize {
    /**
     * Height of base image.
     *
     * <p>Set to the height that corresponds to a width of 1040px.
     */
    int height;

    /**
     * Width of base image. Must be 1040px fixed.
     */
    int width;

    @JsonCreator
    public ImagemapBaseSize(@JsonProperty("height") int height,
                            @JsonProperty("width") int width) {
        this.height = height;
        this.width = width;
    }
}
