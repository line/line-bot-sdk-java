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

package com.linecorp.bot.model.action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;

/**
 * When a button associated with this action is tapped, the camera screen in the LINE app is opened.
 *
 * <p>This action can be configured only with quick reply buttons.
 *
 * @see <a href="https://developers.line.me/en/reference/messaging-api/#camera-action">//developers.line.me/en/reference/messaging-api/#camera-action</a>
 */
@Value
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("camera")
@JsonDeserialize(builder = CameraAction.CameraActionBuilder.class)
public class CameraAction implements Action {
    /**
     * Label for the action. Max: 20 characters
     */
    private final String label;

    public static CameraAction withLabel(final String label) {
        return builder().label(label).build();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class CameraActionBuilder {}
}
