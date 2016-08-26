package com.linecorp.bot.model.v2.event.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Value;

@Value
@JsonTypeName("contact")
public class ContactMessageContent implements MessageContent {
    private final String id;
    private final String userId;
    private final String displayName;

    @JsonCreator
    public ContactMessageContent(
            @JsonProperty("id") String id,
            @JsonProperty("userId") String userId,
            @JsonProperty("displayName") String displayName) {
        this.id = id;
        this.userId = userId;
        this.displayName = displayName;
    }
}
