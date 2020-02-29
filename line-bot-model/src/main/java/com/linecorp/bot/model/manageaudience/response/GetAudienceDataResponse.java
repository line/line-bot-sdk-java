package com.linecorp.bot.model.manageaudience.response;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.manageaudience.response.GetAudienceDataResponse.GetAudienceDataResponseBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = GetAudienceDataResponseBuilder.class)
public class GetAudienceDataResponse {
    Long audienceGroupId;
    AudienceGroupType type;
    String description;
    AudienceGroupStatus status;
    AudienceGroupFailedType failedType;
    Long audienceCount;
    Long created;
    String requestId;
    String clickUrl;
    Boolean isIfaAudience;
    List<Job> jobs;

    @Value
    public static class Job {
        Long audienceGroupJobId;
        Long audienceGroupId;
        String description;
        String type;
        String jobStatus;
        String failedType;
        Long audienceCount;
        Long created;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class GetAudienceDataResponseBuilder {
        // Filled by lombok
    }
}
