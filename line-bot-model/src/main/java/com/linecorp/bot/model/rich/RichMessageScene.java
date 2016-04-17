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

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
public class RichMessageScene {
    private final List<RichMessageSceneImage> draws;
    private final List<RichMessageSceneListener> listeners;

    public RichMessageScene() {
        this.draws = new ArrayList<>();
        this.listeners = new ArrayList<>();
    }

    /**
     * Add new image.
     *
     * @param w width of image. one of 1040, 700, 460, 300, 240.
     * @param h height of image(0 < h < 2080)
     */
    public void addDraw(int w, int h) {
        this.draws.add(new RichMessageSceneImage(w, h));
    }

    public void addListener(int x, int y, int width, int height, @NonNull String action) {
        this.listeners.add(new RichMessageSceneListener(action, x, y, width, height));
    }
}
