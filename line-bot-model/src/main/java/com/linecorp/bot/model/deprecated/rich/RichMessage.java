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

import java.util.HashMap;
import java.util.Map;

import com.linecorp.bot.model.deprecated.rich.action.AbstractRichMessageAction;
import com.linecorp.bot.model.deprecated.rich.action.WebRichMessageAction;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@ToString
@Getter
@EqualsAndHashCode
public class RichMessage {
    private final RichMessageCanvas canvas;
    private final Map<String, RichMessageImage> images;
    private final Map<String, AbstractRichMessageAction> actions;
    private final Map<String, RichMessageScene> scenes;

    public RichMessage(int height) {
        this("scene1", 1040, height);
    }

    /**
     * Creates a new RichMessage Object with the specified size and initial scene name.
     */
    public RichMessage(String initialScene, int width, int height) {
        this(new RichMessageCanvas(initialScene, width, height));
    }

    /**
     * Creates a new RichMessage Object with the specified canvas.
     */
    public RichMessage(RichMessageCanvas canvas) {
        this.canvas = canvas;
        this.images = new HashMap<>();
        this.actions = new HashMap<>();
        this.scenes = new HashMap<>();
    }

    public void addAction(@NonNull String name, @NonNull AbstractRichMessageAction action) {
        this.actions.put(name, action);
    }

    public void addWebAction(@NonNull String name, @NonNull String text, @NonNull String linkUri) {
        this.addAction(name, new WebRichMessageAction(text, linkUri));
    }

    public void addImage(@NonNull String name, @NonNull RichMessageImage image) {
        this.images.put(name, image);
    }

    public void addImage(@NonNull String name, int h) {
        addImage(name, new RichMessageImage(0, 0, 1040, h));
    }

    public void addScene(@NonNull String name, @NonNull RichMessageScene scene) {
        this.scenes.put(name, scene);
    }
}
