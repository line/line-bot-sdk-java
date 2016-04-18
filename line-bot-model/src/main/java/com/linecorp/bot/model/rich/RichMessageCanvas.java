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

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RichMessageCanvas {
    private final int height;

    public RichMessageCanvas(int height) {
        this.height = vaidateHeight(height);
    }

    /**
     * A width of the canvas area.
     */
    @JsonProperty("width")
    public int getWidth() {
        return 1040;
    }

    /**
     * A height of the canvas area.
     */
    @JsonProperty("height")
    public int getHeight() {
        return height;
    }

    /**
     * Validate height.
     */
    private int vaidateHeight(int height) {
        if (height > 2048) {
            // Integer value. Max value is 2080px.
            throw new IllegalArgumentException("RichMessageCanvas's height should be less than or equals 2080px.");
        }
        return height;
    }

    /**
     * An initial scene name.
     */
    @JsonProperty("initialScene")
    public String getInitialScene() {
        return "scene1";
    }
}
