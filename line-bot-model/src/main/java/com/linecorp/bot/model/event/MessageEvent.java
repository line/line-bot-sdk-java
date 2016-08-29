package com.linecorp.bot.model.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.source.Source;

import lombok.Value;

@Value
@JsonTypeName("message")
public class MessageEvent implements Event {
    private final long timestamp;
    private final String replyToken;
    private final Source source;
    private final MessageContent message;

    @JsonCreator
    public MessageEvent(
            @JsonProperty("replyToken") String replyToken,
            @JsonProperty("source") Source source,
            @JsonProperty("timestamp") long timestamp,
            @JsonProperty("message") MessageContent message) {
        this.replyToken = replyToken;
        this.source = source;
        this.timestamp = timestamp;
        this.message = message;
    }
}
