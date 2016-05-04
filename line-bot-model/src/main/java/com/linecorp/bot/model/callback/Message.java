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

package com.linecorp.bot.model.callback;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.content.AbstractContent;
import com.linecorp.bot.model.content.AbstractOperation;
import com.linecorp.bot.model.content.Content;

import lombok.Getter;
import lombok.ToString;

@ToString
public class Message {
    @Getter
    private Content content;
    @Getter
    private final String fromChannel;
    @Getter
    private final List<String> to;
    @Getter
    private final EventType eventType;
    @Getter
    private final String id;
    @Getter
    private final JsonNode contentNode;

    @JsonCreator
    public Message(
            @JsonProperty("fromChannel") String fromChannel,
            @JsonProperty("to") List<String> to,
            @JsonProperty("eventType") EventType eventType,
            @JsonProperty("id") String id,
            @JsonProperty("content") JsonNode content
    ) {
        this.fromChannel = fromChannel;
        this.to = to;
        this.eventType = eventType;
        this.id = id;
        this.contentNode = content;
    }

    // TODO remove this.
    public void parseContent(ObjectMapper objectMapper) throws JsonProcessingException {
        switch (eventType) {
            case Operation:
                this.content = objectMapper.treeToValue(contentNode, AbstractOperation.class);
                break;
            default:
                this.content = objectMapper.treeToValue(contentNode, AbstractContent.class);
                break;
        }
    }
}
