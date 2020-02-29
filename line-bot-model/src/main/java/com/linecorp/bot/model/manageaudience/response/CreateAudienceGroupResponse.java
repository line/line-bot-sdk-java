package com.linecorp.bot.model.manageaudience.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.manageaudience.response.CreateAudienceGroupResponse.CreateAudienceGroupResponseBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = CreateAudienceGroupResponseBuilder.class)
public class CreateAudienceGroupResponse {
    Long audienceGroupId;
    AudienceGroupType type;
    String description;
    Long created;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CreateAudienceGroupResponseBuilder {
        // Filled by lombok
    }
}
