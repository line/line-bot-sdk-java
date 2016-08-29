package com.linecorp.bot.model.v2.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import com.linecorp.bot.model.v2.event.source.Source;

import lombok.Value;

@Value
@JsonTypeName("unfollow")
public class UnfollowEvent implements Event {
    private final long timestamp;
    private final Source source;

    @JsonCreator
    public UnfollowEvent(
            @JsonProperty("source") Source source,
            @JsonProperty("timestamp") long timestamp) {
        this.source = source;
        this.timestamp = timestamp;
    }
}
