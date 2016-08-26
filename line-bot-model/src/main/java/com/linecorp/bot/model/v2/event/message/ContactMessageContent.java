package com.linecorp.bot.model.v2.event.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("contact")
public class ContactMessageContent extends MessageContent {
    private final String userId;
    private final String displayName;

    @JsonCreator
    public ContactMessageContent(
            @JsonProperty("id") String id,
            @JsonProperty("userId") String userId,
            @JsonProperty("displayName") String displayName) {
        super(id);
        this.userId = userId;
        this.displayName = displayName;
    }
}
