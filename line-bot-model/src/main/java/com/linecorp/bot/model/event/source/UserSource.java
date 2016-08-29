package com.linecorp.bot.model.event.source;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Value;

@Value
@JsonTypeName("user")
public class UserSource implements Source {
    private final String userId;

    @JsonCreator
    public UserSource(@JsonProperty("userId") String userId) {
        this.userId = userId;
    }
}
