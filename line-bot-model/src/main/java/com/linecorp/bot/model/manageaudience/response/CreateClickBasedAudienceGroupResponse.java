package com.linecorp.bot.model.manageaudience.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.manageaudience.response.CreateClickBasedAudienceGroupResponse.CreateClickBasedAudienceGroupResponseBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = CreateClickBasedAudienceGroupResponseBuilder.class)
public class CreateClickBasedAudienceGroupResponse {
    private final Long audienceGroupId;
    private final AudienceGroupType type;
    private final String description;
    private final Long created;
    private final String requestId;
    private final String clickUrl;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CreateClickBasedAudienceGroupResponseBuilder {
        // Filled by lombok
    }
}
