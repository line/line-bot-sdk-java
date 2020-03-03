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

import com.linecorp.bot.model.manageaudience.response.GetAudienceDataResponse.GetAudienceDataResponseBuilder;
import com.linecorp.bot.model.manageaudience.response.GetAudienceGroupsResponse.AudienceGroup;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = GetAudienceDataResponseBuilder.class)
public class GetAudienceDataResponse {
    AudienceGroup audienceGroup;

    /**
     * An array of jobs. This array is used to keep track of each attempt to add new user IDs or IFAs to an
     * audience for uploading user IDs. null is returned for any other type of audience.
     */
    List<Job> jobs;

    @Value
    public static class Job {
        /**
         * A job ID.
         */
        Long audienceGroupJobId;

        /**
         * An audience ID.
         */
        long audienceGroupId;

        /**
         * The job's description.
         */
        String description;

        /**
         * The job's type.
         */
        AudienceGroupJobType type;

        /**
         * The job's status.
         */
        AudienceGroupJobStatus jobStatus;

        /**
         * The reason why the operation failed. This is only included when jobs[].jobStatus is FAILED.
         */
        AudienceGroupJobFailedType failedType;

        /**
         * The number of accounts (recipients) that were added or removed.
         */
        Long audienceCount;

        /**
         * When the job was created (in UNIX time).
         */
        long created;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class GetAudienceDataResponseBuilder {
        // Filled by lombok
    }
}
