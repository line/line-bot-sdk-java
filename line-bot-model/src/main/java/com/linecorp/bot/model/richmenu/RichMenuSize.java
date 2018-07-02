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

import lombok.Value;

@Value
public class RichMenuSize {
    public static final RichMenuSize FULL = new RichMenuSize(2500, 1686);
    public static final RichMenuSize HALF = new RichMenuSize(2500, 843);

    /** Width of the rich menu. Must be 2500. */
    int width;
    /** Height of the rich menu. Possible values: 1686 or 843. */
    int height;

    @JsonCreator
    public RichMenuSize(final int width, final int height) {
        this.width = width;
        this.height = height;
    }
}
