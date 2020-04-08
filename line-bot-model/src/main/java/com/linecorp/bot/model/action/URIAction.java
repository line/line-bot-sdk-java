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

package com.linecorp.bot.model.action;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Value;

/**
 * When this action is tapped, the URI specified in the uri field is opened.
 *
 * <p>This action can NOT be configured with quick reply buttons.
 *
 * @see <a href="https://developers.line.me/en/reference/messaging-api/#uri-action">//developers.line.me/en/reference/messaging-api/#uri-action</a>
 */
@Value
@JsonTypeName("uri")
@JsonInclude(Include.NON_NULL)
public class URIAction implements Action {
    /**
     * Label for the action.
     *
     * <p>Max: 20 characters
     */
    String label;

    /**
     * URI opened when the action is performed.
     *
     * <p>Available values are: http, https, tel
     */
    URI uri;

    /**
     * URI that opened on LINE desktop clients when the action is performed. If this property is set,
     * {@link #uri} is ignored on LINE for macOS and Windows.
     */
    AltUri altUri;

    @JsonCreator
    public URIAction(
            @JsonProperty("label") String label,
            @JsonProperty("uri") URI uri,
            @JsonProperty("altUri") AltUri altUri) {
        this.label = label;
        this.uri = uri;
        this.altUri = altUri;
    }

    /**
     * An optional uris that will be opened on LINE desktop clients.
     */
    @Value
    public static class AltUri {
        /**
         * URI opened on LINE for macOS and Windows when the action is performed.
         * The available schemes are http, https, line, and tel.
         * (Max: 1000 characters).
         */
        URI desktop;

        @JsonCreator
        public AltUri(@JsonProperty("desktop") URI desktop) {
            this.desktop = desktop;
        }
    }
}
