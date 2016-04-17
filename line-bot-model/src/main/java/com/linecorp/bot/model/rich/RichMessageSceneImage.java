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

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class RichMessageSceneImage {
    private int w;
    private int h;

    /**
     * Create new image.
     *
     * @param w width of image. one of 1040, 700, 460, 300, 240.
     * @param h height of image(0 < h < 2080)
     */
    public RichMessageSceneImage(int w, int h) {
        setW(w);
        setH(h);
    }

    /**
     * The image ID.
     */
    public String getImage() {
        return "image1";
    }

    /**
     * x-coordinate value.
     */
    public int getX() {
        return 0;
    }

    /**
     * y-coordinate value.
     */
    public int getY() {
        return 0;
    }

    public int getW() {
        return w;
    }

    /**
     * Set width
     *
     * @param w one of 1040, 700, 460, 300, 240.
     */
    private void setW(int w) {
        // 1040, 700, 460, 300, 240
        if ((w != 1040) && (w != 700) && (w != 460) && (w != 300) && (w != 240)) {
            throw new IllegalArgumentException("Scene width must be one of 1040, 700, 460, 300 or 240.");
        }
        this.w = w;
    }

    public int getH() {
        return h;
    }

    private void setH(int h) {
        if (h > 2080) {
            // Integer value. Max value is 2080px.
            throw new IllegalArgumentException("RichMessageImage's height should be less than or equals 2080px.");
        }
        if (h < 1) {
            // Integer value. Max value is 2080px.
            throw new IllegalArgumentException("RichMessageImage's height should be greater than 0px.");
        }

        this.h = h;
    }
}
