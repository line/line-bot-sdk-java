package com.linecorp.bot.model.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import com.linecorp.bot.model.event.source.Source;

import lombok.Value;

@Value
@JsonTypeName("beacon")
public class BeaconEvent implements Event {
    private final long timestamp;
    private final String replyToken;
    private final Source source;
    private final BeaconContent beaconContent;

    @JsonCreator
    public BeaconEvent(
            @JsonProperty("replyToken") String replyToken,
            @JsonProperty("source") Source source,
            @JsonProperty("timestamp") long timestamp,
            @JsonProperty("beacon") BeaconContent beaconContent
    ) {
        this.replyToken = replyToken;
        this.source = source;
        this.timestamp = timestamp;
        this.beaconContent = beaconContent;
    }
}
