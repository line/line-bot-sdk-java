/*
 * Copyright (c) 2016 LINE Corporation. All rights reserved.
 * LINE Corporation PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.linecorp.bot.model.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Getter;
import lombok.Value;

@Value
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("message")
public class MessageAction implements Action {
    private final String label;
    private final String text;

    @JsonCreator
    public MessageAction(
            @JsonProperty("label") String label,
            @JsonProperty("text") String text) {
        this.label = label;
        this.text = text;
    }
}
