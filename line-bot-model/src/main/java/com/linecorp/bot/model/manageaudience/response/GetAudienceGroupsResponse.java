/*
 * Copyright 2020 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

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
    /**
     * An array of audience data.
     */
    private final List<AudienceGroup> audienceGroups;

    /**
     * true when this is not the last page.
     */
    Boolean hasNextPage;

    /**
     * The total number of audiences that can be fetched with the specified filter.
     */
    Long totalCount;

    /**
     * The current page number.
     */
    Long page;

    /**
     * The number of audiences on the current page.
     */
    Long size;

    @Value
    public static class AudienceGroup {
        /**
         * The audience ID.
         */
        long audienceGroupId;

        /**
         * The audience type.
         */
        AudienceGroupType type;

        /**
         * The audience's name.
         */
        String description;

        /**
         * The audience's status.
         */
        AudienceGroupStatus status;

        /**
         * The reason why the operation failed. This is only included when audienceGroups[].status is FAILED
         * or EXPIRED.
         */
        AudienceGroupFailedType failedType;

        /**
         * The number of valid recipients.
         */
        Long audienceCount;

        /**
         * When the audience was created (in UNIX time).
         */
        long created;

        /**
         * The request ID that was specified when the audience was created. This is only included when
         * audienceGroups[].type is CLICK or IMP.
         */
        String requestId;

        /**
         * The URL that was specified when the audience was created. This is only included when
         * audienceGroups[].type is CLICK.
         */
        String clickUrl;

        /**
         * The value specified when the audience for uploading user IDs was created, determining which type of
         * accounts must be specified as recipients.
         */
        Boolean isIfaAudience;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class GetAudienceGroupsResponseBuilder {
        // Filled by lombok
    }
}
