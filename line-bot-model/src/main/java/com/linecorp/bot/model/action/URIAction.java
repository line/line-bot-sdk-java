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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Value;

/**
 * When this action is tapped, the URI specified in the uri field is opened.
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("uri")
public class URIAction implements Action {
    /**
     * Label for the action<br>
     * Max: 20 characters
     */
    private final String label;

    /**
     * URI opened when the action is performed<br>
     * http, https, tel
     */
    private final String uri;

    @JsonCreator
    public URIAction(
            @JsonProperty("label") String label,
            @JsonProperty("uri") String uri) {
        this.label = label;
        this.uri = uri;
    }
}
