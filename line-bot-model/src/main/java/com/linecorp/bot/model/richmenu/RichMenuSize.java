/*
 * Copyright 2018 LINE Corporation
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

package com.linecorp.bot.model.richmenu;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class RichMenuSize {
    public static final RichMenuSize FULL = new RichMenuSize(2500, 1686);
    public static final RichMenuSize HALF = new RichMenuSize(2500, 843);

    /** Width of the rich menu. Possible values: 2500, 1200, or 800. */
    int width;
    /**
     * Height of the rich menu.
     * Possible values: 1686 or 843 for width = 2500, 810 or 405 for 1200, 540 or 270 for 800.
     */
    int height;

    @JsonCreator
    public RichMenuSize(@JsonProperty("width") final int width,
                        @JsonProperty("height") final int height) {
        this.width = width;
        this.height = height;
    }
}
