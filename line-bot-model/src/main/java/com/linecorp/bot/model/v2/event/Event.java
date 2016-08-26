package com.linecorp.bot.model.v2.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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
@EqualsAndHashCode
@ToString
@Getter
public abstract class Event {
    private final long timestamp;

    protected Event(long timestamp) {
        this.timestamp = timestamp;
    }
}
