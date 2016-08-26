package com.linecorp.bot.model.v2.event;

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
public abstract class Source {
    public abstract String getUserId();
}
