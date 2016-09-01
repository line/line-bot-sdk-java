package com.linecorp.bot.model.event.source;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Value;

@Value
@JsonTypeName("room")
public class RoomSource implements Source {
    private final String userId;
    private final String roomId;

    @JsonCreator
    public RoomSource(@JsonProperty("userId") String userId, @JsonProperty("roomId") String roomId) {
        this.userId = userId;
        this.roomId = roomId;
    }
}
