package com.linecorp.bot.model.v2.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import com.linecorp.bot.model.v2.event.postback.PostbackContent;
import com.linecorp.bot.model.v2.event.source.Source;

import lombok.Value;

@Value
@JsonTypeName("postback")
public class PostbackEvent implements Event {
    private final long timestamp;
    private final String replyToken;
    private final Source source;
    private final PostbackContent postbackContent;

    @JsonCreator
    public PostbackEvent(
            @JsonProperty("replyToken") String replyToken,
            @JsonProperty("source") Source source,
            @JsonProperty("timestamp") long timestamp,
            @JsonProperty("postback") PostbackContent postbackContent
    ) {
        this.replyToken = replyToken;
        this.source = source;
        this.timestamp = timestamp;
        this.postbackContent = postbackContent;
    }
}
