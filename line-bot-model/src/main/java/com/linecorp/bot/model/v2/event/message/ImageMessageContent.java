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
@JsonTypeName("image")
public class ImageMessageContent extends MessageContent {
    private final String url;

    @JsonCreator
    public ImageMessageContent(
            @JsonProperty("id") String id,
            @JsonProperty("url") String url) {
        super(id);
        this.url = url;
    }
}
