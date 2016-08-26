package com.linecorp.bot.model.v2.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import com.linecorp.bot.model.v2.event.postback.PostbackContent;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("postback")
public class PostbackEvent extends Event {
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
        super(timestamp);
        this.replyToken = replyToken;
        this.source = source;
        this.postbackContent = postbackContent;
    }
}
