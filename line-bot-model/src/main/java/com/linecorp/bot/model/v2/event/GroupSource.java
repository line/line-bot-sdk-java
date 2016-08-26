package com.linecorp.bot.model.v2.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode
public class GroupSource implements Source {
    private final String groupId;
    private final String userId;

    @JsonCreator
    public GroupSource(
            @JsonProperty("groupId") String groupId,
            @JsonProperty("userId") String userId
    ) {
        this.groupId = groupId;
        this.userId = userId;
    }
}
