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

package com.linecorp.bot.model.event.message;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = ContentProvider.ContentProviderBuilder.class)
public class ContentProvider {
    @JsonPOJOBuilder(withPrefix = "")
    public static class ContentProviderBuilder {
        // Providing builder instead of public constructor. Class body is filled by lombok.
    }

    private static final String LINE = "line";
    private static final String EXTERNAL = "external";

    /**
     * Provider of the resource. Only {@link #LINE} or {@link #EXTERNAL} can be set.
     * <ul>
     * <li> {@code line}: LINE. The binary data can be retrieved from the content endpoint.</li>
     * <li> {@code external}: Provider other than LINE</li>
     * </ul>
     */
    String type;

    /**
     * URL of the resource. Only included when {@link #type} is {@link #EXTERNAL}.
     */
    URI originalContentUrl;

    /**
     * URL of the preview resource. Only included when {@link #type} is {@link #EXTERNAL}.
     */
    URI previewImageUrl;

    @JsonIgnore
    public boolean isExternal() {
        return EXTERNAL.equals(type);
    }
}
