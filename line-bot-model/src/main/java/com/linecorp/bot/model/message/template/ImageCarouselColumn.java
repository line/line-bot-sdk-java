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

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.linecorp.bot.model.action.Action;

import lombok.Value;

/**
 * Column object for carousel template.
 */
@Value
public class ImageCarouselColumn {
    /**
     * Image URL.
     *
     * <ul>
     * <li>Max 1000 characters HTTPS URL</li>
     * <li>JPEG or PNG</li>
     * <li>Aspect ratio: 1:1</li>
     * <li>Max width: 1024px</li>
     * <li>Max: 1 MB</li>
     * </ul>
     */
    URI imageUrl;

    /**
     * Action when tapped.
     */
    Action action;

    @JsonCreator
    public ImageCarouselColumn(@JsonProperty("imageUrl") URI imageUrl,
                               @JsonProperty("action") Action action) {
        this.imageUrl = imageUrl;
        this.action = action;
    }
}
