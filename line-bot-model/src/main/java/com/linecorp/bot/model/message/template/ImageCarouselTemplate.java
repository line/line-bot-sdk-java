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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Value;

import java.util.List;

/**
 * Template message with multiple columns which can be cycled like a carousel.
 */
@Value
@JsonTypeName("image_carousel")
public class ImageCarouselTemplate implements Template {
    /**
     * List of columns(Max: 5)
     */
    private final List<ImageCarouselColumn> columns;

    @JsonCreator
    public ImageCarouselTemplate(@JsonProperty("columns") List<ImageCarouselColumn> columns) {
        this.columns = columns;
    }
}
