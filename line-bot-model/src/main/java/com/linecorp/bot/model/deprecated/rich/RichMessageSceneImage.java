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

package com.linecorp.bot.model.deprecated.rich;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@ToString
@Getter
@EqualsAndHashCode
public class RichMessageSceneImage {

    private final String image;
    private final int x;
    private final int y;
    private final int w;
    private final int h;

    public RichMessageSceneImage(int w, int h) {
        this("image1", 0, 0, w, h);
    }

    /**
     * Creates a new Draw Object indicating the position to draw the image.
     *
     * @param image The name of image object
     * @param x The horizontal position in the canvas
     * @param y The vertical position in the canvas
     * @param w The width to draw the image in the canvas
     * @param h The height to draw the image in the canvas
     */
    public RichMessageSceneImage(@NonNull String image, int x, int y, int w, int h) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = validateHeight(h);
    }

    /**
     * Validate height.
     *
     * @param h the height of image (0 < h <= 2080).
     */
    private int validateHeight(int h) {
        if (h > 2080) {
            // Integer value. Max value is 2080px.
            throw new IllegalArgumentException(
                    "RichMessageImage's height should be less than or equals 2080px.");
        }
        if (h < 1) {
            // Integer value. Max value is 2080px.
            throw new IllegalArgumentException("RichMessageImage's height should be greater than 0px.");
        }

        return h;
    }
}
