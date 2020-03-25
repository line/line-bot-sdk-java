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

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface ManageAudienceService {
    @POST("v2/bot/audienceGroup/upload")
    Call<CreateAudienceGroupResponse> createAudienceGroup(@Body CreateAudienceGroupRequest request);

    @PUT("v2/bot/audienceGroup/upload")
    Call<Void> addAudienceToAudienceGroup(@Body AddAudienceToAudienceGroupRequest request);

    @POST("v2/bot/audienceGroup/click")
    Call<CreateClickBasedAudienceGroupResponse> createClickBasedAudienceGroup(
            @Body CreateClickBasedAudienceGroupRequest request);

    @POST("v2/bot/audienceGroup/imp")
    Call<CreateImpBasedAudienceGroupResponse> createImpBasedAudienceGroup(
            @Body CreateImpBasedAudienceGroupRequest request);

    @PUT("v2/bot/audienceGroup/{audienceGroupId}/updateDescription")
    Call<Void> updateAudienceGroupDescription(
            @Path("audienceGroupId") long audienceGroupId, @Body UpdateAudienceGroupDescriptionRequest request);

    @DELETE("v2/bot/audienceGroup/{audienceGroupId}")
    Call<Void> deleteAudienceGroup(@Path("audienceGroupId") long audienceGroupId);

    @GET("v2/bot/audienceGroup/{audienceGroupId}")
    Call<GetAudienceDataResponse> getAudienceData(@Path("audienceGroupId") long audienceGroupId);

    @GET("v2/bot/audienceGroup/list")
    Call<GetAudienceGroupsResponse> getAudienceGroups(
            @Query("page") long page,
            @Query("description") String description,
            @Query("status") AudienceGroupStatus status,
            @Query("size") Long size,
            @Query("includesExternalPublicGroups") Boolean includesExternalPublicGroups,
            @Query("createRoute") AudienceGroupCreateRoute createRoute);

    @GET("v2/bot/audienceGroup/authorityLevel")
    Call<GetAudienceGroupAuthorityLevelResponse> getAudienceGroupAuthorityLevel();

    @PUT("v2/bot/audienceGroup/authorityLevel")
    Call<Void> updateAudienceGroupAuthorityLevel(@Body UpdateAudienceGroupAuthorityLevelRequest request);
}
