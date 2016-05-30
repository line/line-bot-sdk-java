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
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class RichMessageCanvas {
    private final String initialScene;
    private final int width;
    private final int height;

    public RichMessageCanvas(int height) {
        this("scene1", 1040, height);
    }

    /**
     * Creates a new Canvas Object
     *
     * @param initialScene The initial scene
     * @param width The base width of the canvas
     * @param height The base height of the canvas (less than 2080px)
     */
    public RichMessageCanvas(@NonNull String initialScene, int width, int height) {
        this.initialScene = initialScene;
        this.width = width;
        this.height = validateHeight(height);
    }

    /**
     * Validate height.
     */
    private int validateHeight(int height) {
        if (height > 2080) {
            // Integer value. Max value is 2080px.
            throw new IllegalArgumentException(
                    "RichMessageCanvas's height should be less than or equals 2080px.");
        }
        return height;
    }
}
