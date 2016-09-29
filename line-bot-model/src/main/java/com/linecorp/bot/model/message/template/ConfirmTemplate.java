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

package com.linecorp.bot.model.message.template;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import com.linecorp.bot.model.action.Action;

import lombok.Value;

@Value
@JsonTypeName("confirm")
public class ConfirmTemplate implements Template {
    private final String text;
    private final List<Action> actions;

    @JsonCreator
    public ConfirmTemplate(@JsonProperty("text") String text, @JsonProperty("actions") List<Action> actions) {
        this.text = text;
        this.actions = actions;
    }

    public ConfirmTemplate(String text, Action left, Action right) {
        this(text, Arrays.asList(left, right));
    }
}
