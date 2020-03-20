/*
 * Copyright 2020 LINE Corporation
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
 *
 */

package com.linecorp.bot.model.message.sender;

import java.net.URI;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;

/**
 * Change icon and display name.
 */
@Value
@Builder
@JsonDeserialize(builder = Sender.SenderBuilder.class)
public class Sender {
    /**
     * Display name. Certain words such as LINE may not be used.
     * Max character limit: 20
     */
    String name;

    /**
     * URL of the image to display as an icon when sending a message.
     * Max character limit: 1000
     * URL scheme: https
     */
    URI iconUrl;

    @JsonPOJOBuilder(withPrefix = "")
    public static class SenderBuilder {
    }
}
