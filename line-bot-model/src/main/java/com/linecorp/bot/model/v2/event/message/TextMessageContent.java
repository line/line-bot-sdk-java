package com.linecorp.bot.model.v2.event.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Value;

@Value
@JsonTypeName("text")
public class TextMessageContent implements MessageContent {
    private final String id;
    private final String text;

    @JsonCreator
    public TextMessageContent(
            @JsonProperty("id") String id,
            @JsonProperty("text") String text) {
        this.id = id;
        this.text = text;
    }
}
