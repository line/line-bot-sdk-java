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

import java.io.File;
import java.util.concurrent.CompletableFuture;

import com.linecorp.bot.model.manageaudience.response.CreateAudienceForUploadingResponse;
import com.linecorp.bot.model.response.BotApiResponse;

public interface ManageAudienceBlobClient {

    /**
     * Create audience for uploading user IDs (by file).
     */
    CompletableFuture<CreateAudienceForUploadingResponse> createAudienceForUploadingUserIds(
            String description,
            boolean isIfaAudience,
            String uploadDescription,
            File file
    );

    /**
     * Add user IDs or Identifiers for Advertisers (IFAs) to an audience for uploading user IDs (by file).
     */
    CompletableFuture<BotApiResponse> addUserIdsToAudience(
            long audienceGroupId,
            String uploadDescription,
            File file
    );

    static ManageAudienceBlobClientBuilder builder() {
        return new ManageAudienceBlobClientBuilder();
    }
}
