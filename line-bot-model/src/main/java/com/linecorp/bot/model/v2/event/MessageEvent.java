package com.linecorp.bot.model.v2.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import com.linecorp.bot.model.v2.event.message.MessageContent;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("message")
public class MessageEvent extends Event {
    private final String replyToken;
    private final Source source;
    private final MessageContent message;

    @JsonCreator
    public MessageEvent(
            @JsonProperty("replyToken") String replyToken,
            @JsonProperty("source") Source source,
            @JsonProperty("timestamp") long timestamp,
            @JsonProperty("message") MessageContent message) {
        super(timestamp);
        this.replyToken = replyToken;
        this.source = source;
        this.message = message;
    }
}
