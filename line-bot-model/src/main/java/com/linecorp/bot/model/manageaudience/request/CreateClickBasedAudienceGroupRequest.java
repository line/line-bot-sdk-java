package com.linecorp.bot.model.manageaudience.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.manageaudience.request.CreateClickBasedAudienceGroupRequest.CreateClickAudienceGroupRequestBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = CreateClickAudienceGroupRequestBuilder.class)
public class CreateClickBasedAudienceGroupRequest {
    private final String description;
    private final String requestId;
    private final String clickUrl;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CreateClickAudienceGroupRequestBuilder {
        // Filled by lombok
    }
}
