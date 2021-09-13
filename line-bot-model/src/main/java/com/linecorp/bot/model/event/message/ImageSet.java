/*
 * Copyright 2021 LINE Corporation
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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = ImageSet.ImageSetBuilder.class)
public class ImageSet {
    @JsonPOJOBuilder(withPrefix = "")
    public static class ImageSetBuilder {
        // Providing builder instead of public constructor. Class body is filled by lombok.
    }

    /**
     * Image set ID. Only included when multiple images are sent simultaneously.
     */
    String id;

    /**
     * An index starting from 1, indicating the image number in a set of images sent simultaneously.
     * Only included when multiple images are sent simultaneously.
     */
    Integer index;

    /**
     * The total number of images sent simultaneously.
     * Only included when multiple images are sent simultaneously.
     */
    Integer total;
}
