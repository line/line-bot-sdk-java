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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/**
 * Template message with multiple columns which can be cycled like a carousel.
 *
 * @see CarouselTemplateBuilder
 */
@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonTypeName("carousel")
public class CarouselTemplate implements Template {
    /**
     * List of columns.
     *
     * <p>Max: 5
     */
    private final List<CarouselColumn> columns;

    /**
     * Aspect ratio of the image.
     *
     * <p>Specify one of the following values:
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

    @JsonCreator
    public CarouselTemplate(@JsonProperty("columns") List<CarouselColumn> columns) {
        this.columns = columns;
        this.imageAspectRatio = null;
        this.imageSize = null;
    }
}
