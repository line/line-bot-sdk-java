package com.linecorp.bot.model.manageaudience.response;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.manageaudience.response.GetAudienceGroupsResponse.GetAudienceGroupsResponseBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = GetAudienceGroupsResponseBuilder.class)
public class GetAudienceGroupsResponse {
    private final List<AudienceGroup> audienceGroups;

    @Value
    public static class AudienceGroup {
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
        Boolean hasNextPage;
        Long totalCount;
        Long page;
        Long size;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class GetAudienceGroupsResponseBuilder {
        // Filled by lombok
    }
}
