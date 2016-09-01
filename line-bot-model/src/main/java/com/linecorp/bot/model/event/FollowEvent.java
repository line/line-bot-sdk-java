package com.linecorp.bot.model.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import com.linecorp.bot.model.event.source.Source;

import lombok.Value;

@Value
@JsonTypeName("follow")
public class FollowEvent implements Event {
    private final String replyToken;
    private final Source source;
    private final long timestamp;

    @JsonCreator
    public FollowEvent(
            @JsonProperty("replyToken") String replyToken,
            @JsonProperty("source") Source source,
            @JsonProperty("timestamp") long timestamp) {
        this.replyToken = replyToken;
        this.source = source;
        this.timestamp = timestamp;
    }
}
