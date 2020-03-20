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
 * Defines the size of the full imagemap with the width as 1040px.
 * The top left is used as the origin of the area.
 */
@Value
public class ImagemapArea {
    /**
     * Horizontal position of the tappable area.
     */
    int x;

    /**
     * Vertical position of the tappable area.
     */
    int y;

    /**
     * Width of the tappable area.
     */
    int width;

    /**
     * Height of the tappable area.
     */
    int height;

    @JsonCreator
    public ImagemapArea(@JsonProperty("x") int x,
                        @JsonProperty("y") int y,
                        @JsonProperty("width") int width,
                        @JsonProperty("height") int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
