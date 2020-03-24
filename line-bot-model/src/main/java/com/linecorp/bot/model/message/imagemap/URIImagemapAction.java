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

package com.linecorp.bot.model.message.imagemap;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/**
 * URLImagemapAction - When a region is tapped, the user is redirected to the URI specified.
 */
@JsonTypeName("uri")
@Value
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__(@Deprecated))
// TODO: Remove next release. Use builder() instead.
@JsonDeserialize(builder = URIImagemapAction.URIImagemapActionBuilder.class)
public class URIImagemapAction implements ImagemapAction {
    @JsonPOJOBuilder(withPrefix = "")
    public static class URIImagemapActionBuilder {
        // Providing builder instead of public constructor. Class body is filled by lombok.
    }

    /**
     * Webpage URL.
     */
    String linkUri;

    /**
     * Defined tappable area.
     */
    ImagemapArea area;

    String label;

    /**
     * URIImagemapAction constructor.
     *
     * @deprecated Use builder method instead. This construct will remove in next major release.
     */
    @Deprecated
    public URIImagemapAction(String linkUri, ImagemapArea area) {
        this.linkUri = linkUri;
        this.area = area;
        this.label = null;
    }
}
