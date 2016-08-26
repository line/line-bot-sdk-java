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

package com.linecorp.bot.model.deprecated.callback;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import com.linecorp.bot.model.deprecated.content.Content;

import lombok.Getter;
import lombok.ToString;

@ToString
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = As.PROPERTY,
        property = "eventType",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MessageEvent.class, name = EventType.Constants.MESSAGE),
        @JsonSubTypes.Type(value = OperationEvent.class, name = EventType.Constants.OPERATION)
})
public abstract class Event {

    @Getter
    private final String fromChannel;
    @Getter
    private final List<String> to;
    @Getter
    private final EventType eventType;
    @Getter
    private final String id;
    @Getter
    private final Content content;

    public Event(
            String fromChannel,
            List<String> to,
            EventType eventType,
            String id,
            Content content) {
        this.fromChannel = fromChannel;
        this.to = to;
        this.eventType = eventType;
        this.id = id;
        this.content = content;
    }
}
