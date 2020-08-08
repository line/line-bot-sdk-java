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

package com.linecorp.bot.model.event.link;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;

/**
 * Content of the link account event.
 */
@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = LinkContent.LinkContentBuilder.class)
public class LinkContent {
    @JsonPOJOBuilder(withPrefix = "")
    public static class LinkContentBuilder {
        // Providing builder instead of public constructor. Class body is filled by lombok.
    }

    /**
     * One of the following values to indicate whether the link was successful or not.
     *
     * @see Result
     */
    Result result;

    /**
     * Specified nonce when verifying the user ID.
     */
    String nonce;

    public enum Result {
        /** Indicates the link was successful. */
        @JsonProperty("ok")
        OK,
        /** Indicates the link failed for any reason, such as due to a user impersonation. */
        @JsonProperty("failed")
        FAILED,
    }
}
