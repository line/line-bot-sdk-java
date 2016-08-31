/*
 * Copyright (c) 2016 LINE Corporation. All rights reserved.
 * LINE Corporation PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.linecorp.bot.model.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Value;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("postback")
@JsonInclude(Include.NON_NULL)
public class PostbackAction implements Action {
    private final String label;
    private final String data;
    private final String text;

    /**
     * @param text text message (optional)
     */
    @JsonCreator
    public PostbackAction(
            @JsonProperty("label") String label,
            @JsonProperty("data") String data,
            @JsonProperty("text") String text) {
        this.label = label;
        this.data = data;
        this.text = text;
    }

    public PostbackAction(String label, String data) {
        this(label, data, null);
    }
}
