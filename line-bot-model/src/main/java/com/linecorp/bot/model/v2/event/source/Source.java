package com.linecorp.bot.model.v2.event.source;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonSubTypes({
                      @JsonSubTypes.Type(UserSource.class),
                      @JsonSubTypes.Type(GroupSource.class),
              })
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
)
public interface Source {
    String getUserId();
}
