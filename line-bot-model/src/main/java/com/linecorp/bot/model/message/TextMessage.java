package com.linecorp.bot.model.message;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Value;

@Value
@JsonTypeName("text")
public class TextMessage implements Message {
    private String text;
}
