package com.linecorp.bot.model.event.source;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonSubTypes({
                      @JsonSubTypes.Type(UserSource.class),
                      @JsonSubTypes.Type(GroupSource.class),
                      @JsonSubTypes.Type(RoomSource.class),
              })
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
)
public interface Source {
    String getUserId();

    /**
     * message/event sender id. It might be userId/roomId/roomId.
     * @return message/event sender id
     */
    String getSenderId();
}
