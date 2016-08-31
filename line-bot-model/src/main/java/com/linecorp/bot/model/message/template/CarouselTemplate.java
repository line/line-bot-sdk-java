/*
 * Copyright (c) 2016 LINE Corporation. All rights reserved.
 * LINE Corporation PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.linecorp.bot.model.message.template;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Value;

@Value
@JsonTypeName("carousel")
public class CarouselTemplate implements Template {
    private final List<CarouselColumn> columns;

    @JsonCreator
    public CarouselTemplate(@JsonProperty("columns") List<CarouselColumn> columns) {
        this.columns = columns;
    }
}
