package com.linecorp.bot.model.v2.event.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("text")
public class TextMessageContent extends MessageContent {
    private final String text;

    @JsonCreator
    public TextMessageContent(
            @JsonProperty("id") String id,
            @JsonProperty("text") String text) {
        super(id);
        this.text = text;
    }
}
