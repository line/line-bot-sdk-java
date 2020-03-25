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
package com.linecorp.bot.client;

import java.util.concurrent.CompletableFuture;

import com.linecorp.bot.model.manageaudience.AudienceGroupCreateRoute;
import com.linecorp.bot.model.manageaudience.AudienceGroupStatus;
import com.linecorp.bot.model.manageaudience.request.AddAudienceToAudienceGroupRequest;
import com.linecorp.bot.model.manageaudience.request.CreateAudienceGroupRequest;
import com.linecorp.bot.model.manageaudience.request.CreateClickBasedAudienceGroupRequest;
import com.linecorp.bot.model.manageaudience.request.CreateImpBasedAudienceGroupRequest;
import com.linecorp.bot.model.manageaudience.request.UpdateAudienceGroupAuthorityLevelRequest;
import com.linecorp.bot.model.manageaudience.request.UpdateAudienceGroupDescriptionRequest;
import com.linecorp.bot.model.manageaudience.response.CreateAudienceGroupResponse;
import com.linecorp.bot.model.manageaudience.response.CreateClickBasedAudienceGroupResponse;
import com.linecorp.bot.model.manageaudience.response.CreateImpBasedAudienceGroupResponse;
import com.linecorp.bot.model.manageaudience.response.GetAudienceDataResponse;
import com.linecorp.bot.model.manageaudience.response.GetAudienceGroupAuthorityLevelResponse;
import com.linecorp.bot.model.manageaudience.response.GetAudienceGroupsResponse;
import com.linecorp.bot.model.response.BotApiResponse;

import retrofit2.http.Body;

public interface ManageAudienceClient {

    /**
     * Creates an audience for uploading user IDs. You can create up to 1,000 audiences.
     *
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#create-upload-audience-group">
     *         Create audience for uploading user IDs</a>
     */
    CompletableFuture<CreateAudienceGroupResponse> createAudienceGroup(CreateAudienceGroupRequest request);

    /**
     * Adds new user IDs or IFAs to an audience for uploading user IDs.
     *
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#update-upload-audience-group">
     *         Add user IDs or Identifiers for Advertisers (IFAs) to an audience for uploading user IDs</a>
     */
    CompletableFuture<BotApiResponse> addAudienceToAudienceGroup(
            AddAudienceToAudienceGroupRequest request);

    /**
     * Creates an audience for click-based retargeting.
     *
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#create-click-audience-group">
     *         Create audience for click-based retargeting</a>
     */
    CompletableFuture<CreateClickBasedAudienceGroupResponse> createClickBasedAudienceGroup(
            CreateClickBasedAudienceGroupRequest request);

    /**
     * Creates an audience for impression-based retargeting.
     *
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#create-imp-audience-group">
     *         Create audience for impression-based retargeting</a>
     */
    CompletableFuture<CreateImpBasedAudienceGroupResponse> createImpBasedAudienceGroup(
            CreateImpBasedAudienceGroupRequest request);

    /**
     * Renames an existing audience.
     *
     * @param audienceGroupId The audience ID.
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#set-description-audience-group">
     *         Rename an audience</a>
     */
    CompletableFuture<BotApiResponse> updateAudienceGroupDescription(
            long audienceGroupId, UpdateAudienceGroupDescriptionRequest request);

    /**
     * Deletes an audience.
     *
     * @param audienceGroupId The audience ID.
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#delete-audience-group">
     *         Delete audience</a>
     */
    CompletableFuture<BotApiResponse> deleteAudienceGroup(long audienceGroupId);

    /**
     * Gets audience data.
     *
     * @param audienceGroupId The audience ID.
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#get-audience-group">
     *         Get audience data</a>
     */
    CompletableFuture<GetAudienceDataResponse> getAudienceData(long audienceGroupId);

    /**
     * Gets data for more than one audience.
     *
     * @param page The page to return when getting (paginated) results. Specify a value of 1 or more.
     * @param description The name of the audience(s) to return. You can search for partial matches.
     *         Comparisons are case-insensitive, so the names AUDIENCE and audience are considered
     *         identical.
     * @param status The audience's status.
     * @param size The number of audiences per page. This is 20 by default.
     *         Max: 40
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#get-audience-groups">
     *         Get data for multiple audiences</a>
     */
    CompletableFuture<GetAudienceGroupsResponse> getAudienceGroups(
            long page, String description, AudienceGroupStatus status, Long size,
            Boolean includesExternalPublicGroups, AudienceGroupCreateRoute createRoute);

    /**
     * Get audience group authority level.
     *
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#get-authority-level">
     *         Get authority level</a>
     */
    CompletableFuture<GetAudienceGroupAuthorityLevelResponse> getAudienceGroupAuthorityLevel();

    /**
     * Update audience group authority level.
     *
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#change-authority-level">
     *         Change authority level</a>
     */
    CompletableFuture<BotApiResponse> updateAudienceGroupAuthorityLevel(
            @Body UpdateAudienceGroupAuthorityLevelRequest request);

    static ManageAudienceClientBuilder builder() {
        return new ManageAudienceClientBuilder();
    }
}
