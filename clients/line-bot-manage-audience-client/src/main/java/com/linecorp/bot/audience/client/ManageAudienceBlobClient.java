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

import com.linecorp.bot.audience.model.CreateAudienceGroupResponse;
import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public interface ManageAudienceBlobClient {
        /**
        * 
        * Add user IDs or Identifiers for Advertisers (IFAs) to an audience for uploading user IDs (by file).
            * @param audienceGroupId The audience ID. (optional)
            * @param uploadDescription The description to register with the job (optional)
            * @param _file A text file with one user ID or IFA entered per line. Specify text/plain as Content-Type. Max file number: 1 Max number: 1,500,000  (required)
     * 
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#update-upload-audience-group-by-file"> Documentation</a>
     */
    @PUT("/v2/bot/audienceGroup/upload/byFile")
    @Multipart
    CompletableFuture<Result<Void>> addUserIdsToAudience(
    @Part("audienceGroupId") Long audienceGroupId
    
, 
    @Part("uploadDescription") String uploadDescription
    
, 
    
    @Part("file\"; filename=\"file") UploadFile _file
);

        /**
        * 
        * Create audience for uploading user IDs (by file).
            * @param description The audience&#39;s name. This is case-insensitive, meaning AUDIENCE and audience are considered identical. Max character limit: 120  (optional)
            * @param isIfaAudience To specify recipients by IFAs: set &#x60;true&#x60;. To specify recipients by user IDs: set &#x60;false&#x60; or omit isIfaAudience property.  (optional)
            * @param uploadDescription The description to register for the job (in &#x60;jobs[].description&#x60;).  (optional)
            * @param _file A text file with one user ID or IFA entered per line. Specify text/plain as Content-Type. Max file number: 1 Max number: 1,500,000  (required)
     * 
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#create-upload-audience-group-by-file"> Documentation</a>
     */
    @POST("/v2/bot/audienceGroup/upload/byFile")
    @Multipart
    CompletableFuture<Result<CreateAudienceGroupResponse>> createAudienceForUploadingUserIds(
    @Part("description") String description
    
, 
    @Part("isIfaAudience") Boolean isIfaAudience
    
, 
    @Part("uploadDescription") String uploadDescription
    
, 
    
    @Part("file\"; filename=\"file") UploadFile _file
);


    public static ApiAuthenticatedClientBuilder<ManageAudienceBlobClient> builder(String channelToken) {
        return new ApiAuthenticatedClientBuilder<>(URI.create("https://api-data.line.me"), ManageAudienceBlobClient.class, new ManageAudienceExceptionBuilder(), channelToken);
    }
    public static ApiAuthenticatedClientBuilder<ManageAudienceBlobClient> builder(ChannelTokenSupplier channelTokenSupplier) {
        return new ApiAuthenticatedClientBuilder<>(URI.create("https://api-data.line.me"), ManageAudienceBlobClient.class, new ManageAudienceExceptionBuilder(), channelTokenSupplier);
    }

}
