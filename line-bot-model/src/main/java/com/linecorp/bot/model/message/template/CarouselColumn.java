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

package com.linecorp.bot.model.message.template;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.linecorp.bot.model.action.Action;

import lombok.Value;

/**
 * Column object for carousel template
 */
@Value
public class CarouselColumn {
    /**
     * Image URL
     * <ul>
     *     <li>HTTPS</li>
     *     <li>JPEG or PNG</li>
     *     <li>Aspect ratio: 1:1.51</li>
     *     <li>Max width: 1024px</li>
     *     <li>Max: 1 MB</li>
     * </ul>
     */
    private final String thumbnailImageUrl;

    /**
     * Title (Max: 40 characters)
     */
    private final String title;

    /**
     * Message text<br>
     * Max: 120 characters (no image or title)<br>
     * Max: 60 characters (message with an image or title)
     */
    private final String text;

    /**
     * Action when tapped(Max: 3)
     */
    private final List<Action> actions;

    @JsonCreator
    public CarouselColumn(
            @JsonProperty("thumbnailImageUrl") String thumbnailImageUrl,
            @JsonProperty("title") String title,
            @JsonProperty("text") String text,
            @JsonProperty("actions") List<Action> actions) {
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.title = title;
        this.text = text;
        this.actions = actions != null ? actions : Collections.emptyList();
    }
}
