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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Value;

@Value
@JsonTypeName("message")
public class MessageImageMapAction implements ImageMapAction {
    @JsonProperty("text")
    private String text;

    @JsonProperty("area")
    private ImageMapArea area;

    public MessageImageMapAction(@JsonProperty("text") String text,
                                 @JsonProperty("area") ImageMapArea area) {
        this.text = text;
        this.area = area;
    }
}
