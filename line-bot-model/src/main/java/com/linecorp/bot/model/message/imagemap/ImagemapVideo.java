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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ImagemapVideo {

    /**
     * URL of the video file (Max: 1000 characters).
     *
     * <ul>
     * <li>HTTPS</li>
     * <li>mp4</li>
     * <li>Max: 1 minute</li>
     * <li>Max: 10 MB</li>
     * </ul>
     *
     * <p>
     * Note: A very wide or tall video may be cropped when played in some environments.
     * </p>
     */
    private final URI originalContentUrl;

    /**
     * URL of the preview image (Max: 1000 characters).
     *
     * <ul>
     * <li>HTTPS</li>
     * <li>JPEG</li>
     * <li>Max: 240 x 240 pixels</li>
     * <li>Max: 1 MB</li>
     * </ul>
     */
    private final URI previewImageUrl;

    /**
     * please see {@link ImagemapArea}.
     */
    private final ImagemapArea area;

    /**
     * please see {@link ImagemapExternalLink}.
     *
     * <p>
     * This property is required if you set a video to play
     * and a label to display after the video on the imagemap.
     * </p>
     */
    private final ImagemapExternalLink externalLink;
}
