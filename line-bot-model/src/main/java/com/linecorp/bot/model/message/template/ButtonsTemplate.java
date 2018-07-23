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
import com.fasterxml.jackson.annotation.JsonTypeName;

import com.linecorp.bot.model.action.Action;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/**
 * Template message with an image, title, text, and multiple action buttons.
 */
@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonTypeName("buttons")
public class ButtonsTemplate implements Template {
    /**
     * Image URL.
     *
     * <ul>
     * <li>HTTPS</li>
     * <li>HTTPS</li>
     * <li>JPEG or PNG</li>
     * <li>Aspect ratio: 1:1.51</li>
     * <li>Max width: 1024px</li>
     * <li>Max: 1 MB</li>
     * </ul>
     */
    private final String thumbnailImageUrl;

    /**
     * Aspect ratio of the image.
     *
     * <p>Specify one of the following values:</p>
     *
     * <ul>
     * <li>rectangle: 1.51:1</li>
     * <li>square: 1:1</li>
     * </ul>
     *
     * <p>The default value is {@code rectangle}.
     */
    private final String imageAspectRatio;

    /**
     * Size of the image.
     *
     * <p>Specify one of the following values:</p>
     *
     * <ul>
     * <li>cover: The image fills the entire image area.
     * Parts of the image that do not fit in the area are not displayed.</li>
     * <li>contain: The entire image is displayed in the image area.
     * A background is displayed in the unused areas to the left and right of vertical images
     * and in the areas above and below horizontal images.</li>
     * </ul>
     *
     * <p>The default value is {@code cover}.
     */
    private final String imageSize;

    /**
     * Background color of image.
     *
     * <p>Specify a RGB color value. The default value is {@code #FFFFFF} (white).
     */
    private final String imageBackgroundColor;

    /**
     * Title.
     *
     * <p>Max 40 characters.
     */
    private final String title;

    /**
     * Message text.
     *
     * <ul>
     * <li>Max: 160 characters(no image or title)</li>
     * <li>Max: 60 characters (message with an image or title)</li>
     * </ul>
     */
    private final String text;

    /**
     * Optional: Action when image is tapped; set for the entire image, title, and text area.
     */
    private final Action defaultAction;

    /**
     * Action when tapped.
     *
     * <p>Max: 4
     */
    private final List<Action> actions;

    /**
     * Constructor for basic use. Use {@link #builder()} to use full attributes.
     *
     * @see #builder()
     */
    @JsonCreator
    public ButtonsTemplate(
            @JsonProperty("thumbnailImageUrl") String thumbnailImageUrl,
            @JsonProperty("title") String title,
            @JsonProperty("text") String text,
            @JsonProperty("actions") List<Action> actions) {
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.title = title;
        this.text = text;
        this.defaultAction = null;
        this.actions = actions != null ? actions : Collections.emptyList();
        this.imageAspectRatio = null;
        this.imageSize = null;
        this.imageBackgroundColor = null;
    }
}
