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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/**
 * Column object for carousel template.
 *
 * @see CarouselColumnBuilder
 */
@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CarouselColumn {
    /**
     * Image URL.
     *
     * <ul>
     * <li>HTTPS</li>
     * <li>JPEG or PNG</li>
     * <li>Aspect ratio: 1:1.51</li>
     * <li>Max width: 1024px</li>
     * <li>Max: 1 MB</li>
     * </ul>
     */
    private final String thumbnailImageUrl;

    /**
     * Background color of image.
     *
     * <p>Specify a RGB color value. The default value is <code>#FFFFFF</code> (white).
     */
    private final String imageBackgroundColor;

    /**
     * Title.
     *
     * <p>Max: 40 characters
     */
    private final String title;

    /**
     * Message text.
     *
     * <p>Max: 120 characters (no image or title)<br>
     * Max: 60 characters (message with an image or title)
     */
    private final String text;

    /**
     * Optional: Action when image is tapped; set for the entire image, title, and text area.
     */
    private final Action defaultAction;

    /**
     * Action when tapped.
     *
     * <p>Max: 3</p>
     */
    private final List<Action> actions;

    /**
     * Constructor for standard properties. Use {@link #builder()} for fully-customized instance.
     *
     * @see #builder()
     */
    @JsonCreator
    public CarouselColumn(
            @JsonProperty("thumbnailImageUrl") String thumbnailImageUrl,
            @JsonProperty("title") String title,
            @JsonProperty("text") String text,
            @JsonProperty("actions") List<Action> actions) {
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.title = title;
        this.text = text;
        this.defaultAction = null;
        this.actions = actions != null ? actions : Collections.emptyList();
        this.imageBackgroundColor = null;
    }
}
