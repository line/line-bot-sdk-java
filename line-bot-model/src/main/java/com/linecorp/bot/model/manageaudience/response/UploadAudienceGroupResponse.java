package com.linecorp.bot.model.manageaudience.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.manageaudience.response.UploadAudienceGroupResponse.UploadAudienceGroupResponseBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = UploadAudienceGroupResponseBuilder.class)
public class UploadAudienceGroupResponse {
    Long audienceGroupId;
    AudienceGroupType type;
    String description;
    Long created;

    @JsonPOJOBuilder(withPrefix = "")
    public static class UploadAudienceGroupResponseBuilder {
        // Filled by lombok
    }
}
