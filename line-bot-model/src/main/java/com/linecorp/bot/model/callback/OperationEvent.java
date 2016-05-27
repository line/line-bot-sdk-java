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

import com.fasterxml.jackson.annotation.JsonProperty;

import com.linecorp.bot.model.content.AbstractOperation;

public class OperationEvent extends Event {

    public OperationEvent(@JsonProperty("fromChannel") String fromChannel,
                          @JsonProperty("to") List<String> to,
                          @JsonProperty("eventType") EventType eventType,
                          @JsonProperty("id") String id,
                          @JsonProperty("content") AbstractOperation content) {
        super(fromChannel, to, eventType, id, content);
    }
}
