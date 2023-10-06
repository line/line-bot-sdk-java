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

package com.linecorp.bot.module.client;

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

import com.linecorp.bot.module.model.AcquireChatControlRequest;
import com.linecorp.bot.module.model.DetachModuleRequest;
import com.linecorp.bot.module.model.GetModulesResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public interface LineModuleClient {
        /**
        * 
        * If the Standby Channel wants to take the initiative (Chat Control), it calls the Acquire Control API. The channel that was previously an Active Channel will automatically switch to a Standby Channel. 
            * @param chatId The `userId`, `roomId`, or `groupId` (required)
            * @param acquireChatControlRequest  (optional)
        * @return 
     * 
     * @see <a href="https://developers.line.biz/en/reference/partner-docs/#acquire-control-api"> Documentation</a>
     */
    @POST("/v2/bot/chat/{chatId}/control/acquire")
    CompletableFuture<Result<Void>> acquireChatControl(@Path("chatId") String chatId
, @Body AcquireChatControlRequest acquireChatControlRequest
);

        /**
        * 
        * The module channel admin calls the Detach API to detach the module channel from a LINE Official Account.
            * @param detachModuleRequest  (optional)
        * @return 
     * 
     * @see <a href="https://developers.line.biz/en/reference/partner-docs/#unlink-detach-module-channel-by-operation-mc-admin"> Documentation</a>
     */
    @POST("/v2/bot/channel/detach")
    CompletableFuture<Result<Void>> detachModule(@Body DetachModuleRequest detachModuleRequest
);

        /**
        * 
        * Gets a list of basic information about the bots of multiple LINE Official Accounts that have attached module channels.
            * @param start Value of the continuation token found in the next property of the JSON object returned in the response. If you can't get all basic information about the bots in one request, include this parameter to get the remaining array.  (optional)
            * @param limit Specify the maximum number of bots that you get basic information from. The default value is 100. Max value: 100  (optional, default to 100)
        * @return 
     * 
     * @see <a href="https://developers.line.biz/en/reference/partner-docs/#get-multiple-bot-info-api"> Documentation</a>
     */
    @GET("/v2/bot/list")
    CompletableFuture<Result<GetModulesResponse>> getModules(@Query("start") String start
, @Query("limit") Integer limit
);

        /**
        * 
        * To return the initiative (Chat Control) of Active Channel to Primary Channel, call the Release Control API. 
            * @param chatId The `userId`, `roomId`, or `groupId` (required)
        * @return 
     * 
     * @see <a href="https://developers.line.biz/en/reference/partner-docs/#release-control-api"> Documentation</a>
     */
    @POST("/v2/bot/chat/{chatId}/control/release")
    CompletableFuture<Result<Void>> releaseChatControl(@Path("chatId") String chatId
);


    public static ApiAuthenticatedClientBuilder<LineModuleClient> builder(String channelToken) {
        return new ApiAuthenticatedClientBuilder<>(URI.create("https://api.line.me"), LineModuleClient.class, new LineModuleExceptionBuilder(), channelToken);
    }
    public static ApiAuthenticatedClientBuilder<LineModuleClient> builder(ChannelTokenSupplier channelTokenSupplier) {
        return new ApiAuthenticatedClientBuilder<>(URI.create("https://api.line.me"), LineModuleClient.class, new LineModuleExceptionBuilder(), channelTokenSupplier);
    }

}
