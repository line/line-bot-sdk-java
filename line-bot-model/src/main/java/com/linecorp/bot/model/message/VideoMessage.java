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

package com.linecorp.bot.model.message;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Value;

@Value
@JsonTypeName("video")
public class VideoMessage implements Message {
    /**
     * URL of video file
     * <ul>
     * <li>HTTPS</li>
     * <li>mp4</li>
     * <li>Less than 1 minute</li>
     * <li>Max: 10 MB</li>
     *  </ul>
     */
    private final String originalContentUrl;

    /**
     * URL of preview image
     * <ul>
     *  <li>HTTPS</li>
     *  <li>JPEG</li>
     *  <li>Max: 240 x 240</li>
     *  <li>Max: 1 MB</li>
     * </ul>
     */
    private final String previewImageUrl;
}
