package com.linecorp.bot.model.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import com.linecorp.bot.model.event.source.GroupSource;

import lombok.Value;

@Value
@JsonTypeName("join")
public class JoinEvent implements Event {
    private final String replyToken;
    private final GroupSource source;
    private final long timestamp;

    @JsonCreator
    public JoinEvent(
            @JsonProperty("replyToken") String replyToken,
            @JsonProperty("source") GroupSource source,
            @JsonProperty("timestamp") long timestamp) {
        this.replyToken = replyToken;
        this.source = source;
        this.timestamp = timestamp;
    }
}
