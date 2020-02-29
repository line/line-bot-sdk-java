package com.linecorp.bot.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.response.GetAudienceGroupsResponse.GetAudienceGroupsResponseBuilder;

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

    public enum AudienceGroupType {
        UPLOAD,
        CLICK,
        IMP,
        @JsonEnumDefaultValue
        UNKNOWN // Messaging API may implement new audience group type in the future!
    }

    public enum AudienceGroupStatus {
        IN_PROGRESS,
        READY,
        FAILED,
        EXPIRED,
        @JsonEnumDefaultValue
        UNKNOWN
    }

    public enum AudienceGroupFailedType {
        AUDIENCE_GROUP_AUDIENCE_INSUFFICIENT,
        INTERNAL_ERROR,
        @JsonEnumDefaultValue
        UNKNOWN
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class GetAudienceGroupsResponseBuilder {
        // Filled by lombok
    }
}
