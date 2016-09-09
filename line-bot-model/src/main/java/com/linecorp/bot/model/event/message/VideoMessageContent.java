package com.linecorp.bot.model.event.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Value;

@Value
@JsonTypeName("video")
public class VideoMessageContent implements MessageContent {
    private final String id;
    private final String url;

    @JsonCreator
    public VideoMessageContent(
            @JsonProperty("id") String id,
            @JsonProperty("url") String url) {
        this.id = id;
        this.url = url;
    }
}
