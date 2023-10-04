/*
 * Copyright 2023 LINE Corporation
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

package com.linecorp.bot.audience.client;

import java.net.URI;

import java.util.concurrent.CompletableFuture;

import com.linecorp.bot.client.base.ApiAuthenticatedClientBuilder;
import com.linecorp.bot.client.base.ApiClientBuilder;
import com.linecorp.bot.client.base.Result;
import com.linecorp.bot.client.base.BlobContent;
import com.linecorp.bot.client.base.UploadFile;
import com.linecorp.bot.client.base.channel.ChannelTokenSupplier;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.MultipartBody;

import com.linecorp.bot.audience.model.AddAudienceToAudienceGroupRequest;
import com.linecorp.bot.audience.model.AudienceGroupCreateRoute;
import com.linecorp.bot.audience.model.AudienceGroupStatus;
import com.linecorp.bot.audience.model.CreateAudienceGroupRequest;
import com.linecorp.bot.audience.model.CreateAudienceGroupResponse;
import com.linecorp.bot.audience.model.CreateClickBasedAudienceGroupRequest;
import com.linecorp.bot.audience.model.CreateClickBasedAudienceGroupResponse;
import com.linecorp.bot.audience.model.CreateImpBasedAudienceGroupRequest;
import com.linecorp.bot.audience.model.CreateImpBasedAudienceGroupResponse;
import com.linecorp.bot.audience.model.ErrorResponse;
import com.linecorp.bot.audience.model.GetAudienceDataResponse;
import com.linecorp.bot.audience.model.GetAudienceGroupAuthorityLevelResponse;
import com.linecorp.bot.audience.model.GetAudienceGroupsResponse;
import com.linecorp.bot.audience.model.UpdateAudienceGroupAuthorityLevelRequest;
import com.linecorp.bot.audience.model.UpdateAudienceGroupDescriptionRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public interface ManageAudienceClient {
        /**
        * 
        * Activate audience
            * @param audienceGroupId The audience ID. (required)
        * @return 
     * 
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#activate-audience-group"> Documentation</a>
     */
    @PUT("/v2/bot/audienceGroup/{audienceGroupId}/activate")
    CompletableFuture<Result<Void>> activateAudienceGroup(@Path("audienceGroupId") Long audienceGroupId
);

        /**
        * 
        * Add user IDs or Identifiers for Advertisers (IFAs) to an audience for uploading user IDs (by JSON)
            * @param addAudienceToAudienceGroupRequest  (required)
        * @return 
     * 
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#update-upload-audience-group"> Documentation</a>
     */
    @PUT("/v2/bot/audienceGroup/upload")
    CompletableFuture<Result<Void>> addAudienceToAudienceGroup(@Body AddAudienceToAudienceGroupRequest addAudienceToAudienceGroupRequest
);

        /**
        * 
        * Create audience for uploading user IDs (by JSON)
            * @param createAudienceGroupRequest  (required)
        * @return 
     * 
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#create-upload-audience-group"> Documentation</a>
     */
    @POST("/v2/bot/audienceGroup/upload")
    CompletableFuture<Result<CreateAudienceGroupResponse>> createAudienceGroup(@Body CreateAudienceGroupRequest createAudienceGroupRequest
);

        /**
        * 
        * Create audience for click-based retargeting
            * @param createClickBasedAudienceGroupRequest  (required)
        * @return 
     * 
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#create-click-audience-group"> Documentation</a>
     */
    @POST("/v2/bot/audienceGroup/click")
    CompletableFuture<Result<CreateClickBasedAudienceGroupResponse>> createClickBasedAudienceGroup(@Body CreateClickBasedAudienceGroupRequest createClickBasedAudienceGroupRequest
);

        /**
        * 
        * Create audience for impression-based retargeting
            * @param createImpBasedAudienceGroupRequest  (required)
        * @return 
     * 
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#create-imp-audience-group"> Documentation</a>
     */
    @POST("/v2/bot/audienceGroup/imp")
    CompletableFuture<Result<CreateImpBasedAudienceGroupResponse>> createImpBasedAudienceGroup(@Body CreateImpBasedAudienceGroupRequest createImpBasedAudienceGroupRequest
);

        /**
        * 
        * Delete audience
            * @param audienceGroupId The audience ID. (required)
        * @return 
     * 
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#delete-audience-group"> Documentation</a>
     */
    @DELETE("/v2/bot/audienceGroup/{audienceGroupId}")
    CompletableFuture<Result<Void>> deleteAudienceGroup(@Path("audienceGroupId") Long audienceGroupId
);

        /**
        * 
        * Gets audience data.
            * @param audienceGroupId The audience ID. (required)
        * @return 
     * 
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#get-audience-group"> Documentation</a>
     */
    @GET("/v2/bot/audienceGroup/{audienceGroupId}")
    CompletableFuture<Result<GetAudienceDataResponse>> getAudienceData(@Path("audienceGroupId") Long audienceGroupId
);

        /**
        * 
        * Get the authority level of the audience
        * @return 
     * 
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#get-authority-level"> Documentation</a>
     */
    @GET("/v2/bot/audienceGroup/authorityLevel")
    CompletableFuture<Result<GetAudienceGroupAuthorityLevelResponse>> getAudienceGroupAuthorityLevel();

        /**
        * 
        * Gets data for more than one audience.
            * @param page The page to return when getting (paginated) results. Must be 1 or higher. (required)
            * @param description The name of the audience(s) to return. You can search for partial matches. This is case-insensitive, meaning AUDIENCE and audience are considered identical. If omitted, the name of the audience(s) will not be used as a search criterion.  (optional)
            * @param status The status of the audience(s) to return. If omitted, the status of the audience(s) will not be used as a search criterion.  (optional)
            * @param size The number of audiences per page. Default: 20 Max: 40  (optional)
            * @param includesExternalPublicGroups true (default): Get public audiences created in all channels linked to the same bot. false: Get audiences created in the same channel.  (optional)
            * @param createRoute How the audience was created. If omitted, all audiences are included.  &#x60;OA_MANAGER&#x60;: Return only audiences created with LINE Official Account Manager (opens new window). &#x60;MESSAGING_API&#x60;: Return only audiences created with Messaging API.  (optional)
        * @return 
     * 
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#get-audience-groups"> Documentation</a>
     */
    @GET("/v2/bot/audienceGroup/list")
    CompletableFuture<Result<GetAudienceGroupsResponse>> getAudienceGroups(@Query("page") Long page
, @Query("description") String description
, @Query("status") AudienceGroupStatus status
, @Query("size") Long size
, @Query("includesExternalPublicGroups") Boolean includesExternalPublicGroups
, @Query("createRoute") AudienceGroupCreateRoute createRoute
);

        /**
        * 
        * Change the authority level of the audience
            * @param updateAudienceGroupAuthorityLevelRequest  (required)
        * @return 
     * 
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#change-authority-level"> Documentation</a>
     */
    @PUT("/v2/bot/audienceGroup/authorityLevel")
    CompletableFuture<Result<Void>> updateAudienceGroupAuthorityLevel(@Body UpdateAudienceGroupAuthorityLevelRequest updateAudienceGroupAuthorityLevelRequest
);

        /**
        * 
        * Renames an existing audience.
            * @param audienceGroupId The audience ID. (required)
            * @param updateAudienceGroupDescriptionRequest  (required)
        * @return 
     * 
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#set-description-audience-group"> Documentation</a>
     */
    @PUT("/v2/bot/audienceGroup/{audienceGroupId}/updateDescription")
    CompletableFuture<Result<Void>> updateAudienceGroupDescription(@Path("audienceGroupId") Long audienceGroupId
, @Body UpdateAudienceGroupDescriptionRequest updateAudienceGroupDescriptionRequest
);


    public static ApiAuthenticatedClientBuilder<ManageAudienceClient> builder(String channelToken) {
        return new ApiAuthenticatedClientBuilder<>(URI.create("https://api.line.me"), ManageAudienceClient.class, new ManageAudienceExceptionBuilder(), channelToken);
    }
    public static ApiAuthenticatedClientBuilder<ManageAudienceClient> builder(ChannelTokenSupplier channelTokenSupplier) {
        return new ApiAuthenticatedClientBuilder<>(URI.create("https://api.line.me"), ManageAudienceClient.class, new ManageAudienceExceptionBuilder(), channelTokenSupplier);
    }

}
