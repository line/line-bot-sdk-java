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

package com.linecorp.bot.model.manageaudience;

import java.net.URI;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.manageaudience.AudienceGroup.AudienceGroupBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = AudienceGroupBuilder.class)
public class AudienceGroup {
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
    URI clickUrl;

    /**
     * The value specified when the audience for uploading user IDs was created, determining which type of
     * accounts must be specified as recipients.
     */
    Boolean isIfaAudience;

    /**
     * Audience's update permission. Audiences linked to the same channel will be READ_WRITE.
     */
    AudienceGroupPermission permission;

    /**
     * How the audience was created. If omitted, all audiences are included.
     */
    AudienceGroupCreateRoute createRoute;

    @JsonPOJOBuilder(withPrefix = "")
    public static class AudienceGroupBuilder {
        // Filled by lombok
    }
}
