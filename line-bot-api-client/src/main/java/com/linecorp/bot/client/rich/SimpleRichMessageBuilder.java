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

package com.linecorp.bot.client.rich;

import java.util.ArrayList;
import java.util.List;

import com.linecorp.bot.model.deprecated.rich.RichMessage;
import com.linecorp.bot.model.deprecated.rich.RichMessageImage;
import com.linecorp.bot.model.deprecated.rich.RichMessageScene;
import com.linecorp.bot.model.deprecated.rich.RichMessageSceneImage;
import com.linecorp.bot.model.deprecated.rich.RichMessageSceneListener;
import com.linecorp.bot.model.deprecated.rich.action.AbstractRichMessageAction;
import com.linecorp.bot.model.deprecated.rich.action.SendMessageRichMessageAction;
import com.linecorp.bot.model.deprecated.rich.action.WebRichMessageAction;

import lombok.NonNull;

/**
 * A builder that creates a RichMessage with single scene+image and multi actions.
 */
public final class SimpleRichMessageBuilder {

    private static final String DEFAULT_SCENE_NAME = "scene1";

    private static final String DEFAULT_IMAGE_NAME = "image1";

    /**
     * Creates a new builder with the specified base canvas size
     */
    public static SimpleRichMessageBuilder create(int width, int height) {
        return new SimpleRichMessageBuilder(width, height);
    }

    private final int width;
    private final int height;

    private final List<ActionEntry> actions = new ArrayList<>(5);

    private SimpleRichMessageBuilder(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public SimpleRichMessageBuilder addAction(int x, int y, int width, int height,
                                              @NonNull AbstractRichMessageAction action) {
        actions.add(new ActionEntry(x, y, width, height, action));
        return this;
    }

    public SimpleRichMessageBuilder addWebAction(int x, int y, int width, int height,
                                                 @NonNull String text, @NonNull String url) {
        return addAction(x, y, width, height, new WebRichMessageAction(text, url));
    }

    public SimpleRichMessageBuilder addSendMessageAction(int x, int y, int width, int height,
                                                         @NonNull String message) {
        return addAction(x, y, width, height, new SendMessageRichMessageAction(message));
    }

    public RichMessage build() {
        final RichMessage richMessage = new RichMessage(DEFAULT_SCENE_NAME, width, height);
        richMessage.addImage(DEFAULT_IMAGE_NAME, new RichMessageImage(0, 0, width, height));

        final RichMessageScene scene = new RichMessageScene();
        scene.addDraw(new RichMessageSceneImage(DEFAULT_IMAGE_NAME, 0, 0, width, height));
        richMessage.addScene(DEFAULT_SCENE_NAME, scene);

        int seqNo = 1;
        for (ActionEntry entry : actions) {
            final String name = "action" + (seqNo++);
            richMessage.addAction(name, entry.action);
            scene.addListener(new RichMessageSceneListener(name, entry.x, entry.y, entry.width, entry.height));
        }

        return richMessage;
    }

    private static class ActionEntry {

        private final int x;
        private final int y;
        private final int width;
        private final int height;
        private final AbstractRichMessageAction action;

        private ActionEntry(int x, int y, int width, int height, @NonNull AbstractRichMessageAction action) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.action = action;
        }
    }
}
