package com.linecorp.bot.model.v2.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import com.linecorp.bot.model.v2.event.operation.OperationContent;
import com.linecorp.bot.model.v2.event.source.Source;

import lombok.Value;

@Value
@JsonTypeName("operation")
public class OperationEvent implements Event {
    private final long timestamp;
    private final String replyToken;
    private final Source source;
    private final OperationContent operation;

    @JsonCreator
    public OperationEvent(
            @JsonProperty("replyToken") String replyToken,
            @JsonProperty("source") Source source,
            @JsonProperty("timestamp") long timestamp,
            @JsonProperty("operation") OperationContent operation) {
        this.replyToken = replyToken;
        this.source = source;
        this.timestamp = timestamp;
        this.operation = operation;
    }
}
