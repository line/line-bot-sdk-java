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

package com.linecorp.bot.model.rich;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@EqualsAndHashCode
public class RichMessageImage {
    private final int x;
    private final int y;
    private final int w;
    private final int h;

    public RichMessageImage(int h) {
        this(0, 0, 1040, h);
    }

    /**
     * Creates a new Image Object cropped from the base image to the specified size
     *
     * @param x The horizontal position of the base image
     * @param y The vertical position of the base image
     * @param w The width of cropping area
     * @param h the height of cropping area
     */
    public RichMessageImage(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

}
