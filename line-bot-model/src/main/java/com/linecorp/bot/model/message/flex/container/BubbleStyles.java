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

package com.linecorp.bot.model.message.flex.container;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonInclude(Include.NON_NULL)
public class BubbleStyles {

    @Value
    @Builder
    @JsonInclude(Include.NON_NULL)
    public static class BlockStyle {
        private final String backgroundColor;

        private final Boolean separator;

        private final String separatorColor;

        @JsonCreator
        public BlockStyle(
                @JsonProperty("backgroundColor") String backgroundColor,
                @JsonProperty("separator") Boolean separator,
                @JsonProperty("separatorColor") String separatorColor) {
            this.backgroundColor = backgroundColor;
            this.separator = separator;
            this.separatorColor = separatorColor;
        }
    }

    private final BlockStyle header;

    private final BlockStyle hero;

    private final BlockStyle body;

    private final BlockStyle footer;

    @JsonCreator
    public BubbleStyles(
            @JsonProperty("header") BlockStyle header,
            @JsonProperty("hero") BlockStyle hero,
            @JsonProperty("body") BlockStyle body,
            @JsonProperty("footer") BlockStyle footer) {
        this.header = header;
        this.hero = hero;
        this.body = body;
        this.footer = footer;
    }
}
