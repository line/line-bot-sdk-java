package com.linecorp.bot.model.v2.event.postback;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class PostbackContent {
    private final String data;
    private final Object params;

    @JsonCreator
    public PostbackContent(
            @JsonProperty("data") String data,
            @JsonProperty("params") Object params) {
        this.data = data;
        this.params = params;
    }
}
