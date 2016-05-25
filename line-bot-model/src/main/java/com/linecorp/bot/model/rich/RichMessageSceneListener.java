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

import java.util.Arrays;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 * The listener model defines a mapping between an event tap area and the action. The listeners property can have
 * multiple listener objects. Each listener object has the following properties:
 */
@Getter
@ToString
@EqualsAndHashCode
public class RichMessageSceneListener {

    /**
     * Rectangular area where tap event is received.
     */
    private final List<Integer> params;
    /**
     * Action ID to be executed when the tap event occurs.
     */
    private final String action;

    /**
     * Creates a new Listener Object indicating the position to execute the specified action
     *
     * @param action The action name to be executed
     * @param x The vertical position of the target area
     * @param y The horizontal position of the target area
     * @param width The width of the target area
     * @param height The height of the target area
     */
    public RichMessageSceneListener(@NonNull String action, int x, int y, int width, int height) {
        this.params = Arrays.asList(x, y, width, height);
        this.action = action;
    }

    public String getType() {
        return "touch";
    }
}
