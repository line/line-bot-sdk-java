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

package com.linecorp.bot.model.deprecated.content;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;

/**
 * To send location information, the required values are as follows.
 */

@ToString
@Getter
@Deprecated
public class LocationContent extends AbstractContent {
    /**
     * String used to explain the location information (example: name of restaurant, address).
     */
    private final String text;
    private final LocationContentLocation location;

    @JsonCreator
    public LocationContent(
            @JsonProperty("id") String id,
            @JsonProperty("from") String from,
            @JsonProperty("contentType") ContentType contentType,
            @JsonProperty("toType") RecipientType toType,
            @JsonProperty("text") String text,
            @JsonProperty("location") LocationContentLocation location) {
        super(id, from, contentType, toType);
        this.text = text;
        this.location = location;
    }

    public LocationContent(RecipientType toType, String text, String title, String address, double latitude,
                           double longitude) {
        this(
                null,
                null,
                ContentType.LOCATION,
                toType,
                text,
                new LocationContentLocation(title, address, latitude, longitude)
        );
    }
}
