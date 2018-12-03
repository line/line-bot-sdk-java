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

package com.linecorp.bot.model.message.imagemap;

import java.net.URI;

import lombok.Value;

/**
 * labeled hyperlink displayed after the video is finished.
 */
@Value
public class ImagemapExternalLink {
    /**
     * Webpage URL. Called when the label displayed after the video is tapped.
     *
     * <ul>
     * <li>Max: 1000 characters</li>
     * </ul>
     */
    private final URI linkUri;

    /**
     * Label. Displayed after the video is finished.
     *
     * <ul>
     * <li>Max: 30 characters</li>
     * </ul>
     */
    private final String label;
}
