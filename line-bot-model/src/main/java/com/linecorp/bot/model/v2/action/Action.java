/*
 * Copyright (c) 2016 LINE Corporation. All rights reserved.
 * LINE Corporation PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.linecorp.bot.model.v2.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import lombok.Getter;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = As.PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
                      @JsonSubTypes.Type(PostbackAction.class),
                      @JsonSubTypes.Type(URIAction.class),
                      @JsonSubTypes.Type(MessageAction.class)
              })
public abstract class Action {
    @Getter
    private final String label;

    protected Action(@JsonProperty("label") String label) {
        this.label = label;
    }
}
