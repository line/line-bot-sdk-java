package com.linecorp.bot.model.manageaudience.request;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.manageaudience.request.AddAudienceToAudienceGroupRequest.AddAudienceToAudienceGroupRequestBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = AddAudienceToAudienceGroupRequestBuilder.class)
public class AddAudienceToAudienceGroupRequest {
    private final Long audienceGroupId;
    private final String description;
    private final Boolean isIfaAudience;
    private final String uploadDescription;
    private final List<Audience> audiences;

    @JsonPOJOBuilder(withPrefix = "")
    public static class AddAudienceToAudienceGroupRequestBuilder {
        // Filled by lombok
    }
}
