package com.linecorp.bot.model.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import com.linecorp.bot.model.event.source.Source;

@JsonSubTypes({
                      @JsonSubTypes.Type(MessageEvent.class),
                      @JsonSubTypes.Type(UnfollowEvent.class),
                      @JsonSubTypes.Type(FollowEvent.class),
                      @JsonSubTypes.Type(JoinEvent.class),
                      @JsonSubTypes.Type(LeaveEvent.class),
                      @JsonSubTypes.Type(PostbackEvent.class),
                      @JsonSubTypes.Type(BeaconEvent.class)
              })
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
)
public interface Event {
    Source getSource();
    long getTimestamp();
}
