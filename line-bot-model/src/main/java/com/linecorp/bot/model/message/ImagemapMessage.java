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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.message.imagemap.ImagemapAction;
import com.linecorp.bot.model.message.imagemap.ImagemapBaseSize;
import com.linecorp.bot.model.message.imagemap.ImagemapVideo;
import com.linecorp.bot.model.message.quickreply.QuickReply;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/**
 * Imagemaps are images with one or more links. You can assign one link for the entire image or multiple
 * links which correspond to different regions of the image.
 */
@Value
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonTypeName("imagemap")
@JsonDeserialize(builder = ImagemapMessage.ImagemapMessageBuilder.class)
public class ImagemapMessage implements Message {
    /**
     * Base URL.
     *
     * <ul>
     * <li>HTTPS required</li>
     * </ul>
     */
    private final String baseUrl;

    /**
     * Alternative text.
     */
    private final String altText;

    /**
     * Size of base image.
     */
    private final ImagemapBaseSize baseSize;

    /**
     * Action when tapped.
     */
    private final List<ImagemapAction> actions;

    private final ImagemapVideo video;

    private final QuickReply quickReply;

    /**
     * Constructor without {@link #quickReply} parameter.
     *
     * <p>If you want use {@link QuickReply}, please use {@link #builder()} instead.
     */
    public ImagemapMessage(
            final String baseUrl,
            final String altText,
            final ImagemapBaseSize imagemapBaseSize,
            final List<ImagemapAction> actions,
            final ImagemapVideo imagemapVideo) {
        this(baseUrl, altText, imagemapBaseSize, actions, imagemapVideo, null);
    }

    public ImagemapMessage(
            final String baseUrl,
            final String altText,
            final ImagemapBaseSize imagemapBaseSize,
            final List<ImagemapAction> actions) {
        this(baseUrl, altText, imagemapBaseSize, actions, null);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class ImagemapMessageBuilder {}
}
