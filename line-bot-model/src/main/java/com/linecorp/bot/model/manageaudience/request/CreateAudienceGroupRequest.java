package com.linecorp.bot.model.manageaudience.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.manageaudience.request.CreateAudienceGroupRequest.CreateAudienceGroupRequestBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = CreateAudienceGroupRequestBuilder.class)
public final class CreateAudienceGroupRequest {
    private final String description;
    private final Boolean isIfaAudience;
    private final String uploadDescription;
    private final List<Audience> audiences;

    @JsonCreator
    public CreateAudienceGroupRequest(String description, Boolean isIfaAudience, String uploadDescription,
                                      List<Audience> audiences) {
        this.description = description;
        this.isIfaAudience = isIfaAudience;
        this.uploadDescription = uploadDescription;
        this.audiences = audiences;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class CreateAudienceGroupRequestBuilder {
        // Filled by lombok
    }
}
