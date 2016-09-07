/*
 * Copyright (c) 2016 LINE Corporation. All rights reserved.
 * LINE Corporation PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.linecorp.bot.model.message.template;

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
}
