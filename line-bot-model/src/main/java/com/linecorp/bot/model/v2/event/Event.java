package com.linecorp.bot.model.v2.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonSubTypes({
                      @JsonSubTypes.Type(MessageEvent.class),
                      @JsonSubTypes.Type(OperationEvent.class),
                      @JsonSubTypes.Type(PostbackEvent.class)
              })
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
)
public interface Event {
    long getTimestamp();
}
