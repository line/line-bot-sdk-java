package com.linecorp.bot.model.message;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Value;

@Value
@JsonTypeName("audio")
public class AudioMessage implements Message {
    private String originalContentUrl;
    private Integer duration;
}
