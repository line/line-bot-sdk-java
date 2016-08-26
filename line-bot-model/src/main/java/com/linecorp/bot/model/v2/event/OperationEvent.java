package com.linecorp.bot.model.v2.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import com.linecorp.bot.model.v2.event.operation.OperationContent;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("operation")
public class OperationEvent extends Event {
    private final String replyToken;
    private final Source source;
    private final OperationContent operation;

    @JsonCreator
    public OperationEvent(
            @JsonProperty("replyToken") String replyToken,
            @JsonProperty("source") Source source,
            @JsonProperty("timestamp") long timestamp,
            @JsonProperty("operation") OperationContent operation) {
        super(timestamp);
        this.replyToken = replyToken;
        this.source = source;
        this.operation = operation;
    }
}
