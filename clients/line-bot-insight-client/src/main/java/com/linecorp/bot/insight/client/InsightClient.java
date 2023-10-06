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

package com.linecorp.bot.insight.client;

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

import com.linecorp.bot.insight.model.GetFriendsDemographicsResponse;
import com.linecorp.bot.insight.model.GetMessageEventResponse;
import com.linecorp.bot.insight.model.GetNumberOfFollowersResponse;
import com.linecorp.bot.insight.model.GetNumberOfMessageDeliveriesResponse;
import com.linecorp.bot.insight.model.GetStatisticsPerUnitResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public interface InsightClient {
        /**
        * 
        * Retrieves the demographic attributes for a LINE Official Account's friends.You can only retrieve information about friends for LINE Official Accounts created by users in Japan (JP), Thailand (TH), Taiwan (TW) and Indonesia (ID). 
     * 
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#get-demographic"> Documentation</a>
     */
    @GET("/v2/bot/insight/demographic")
    CompletableFuture<Result<GetFriendsDemographicsResponse>> getFriendsDemographics();

        /**
        * Get user interaction statistics
        * Returns statistics about how users interact with narrowcast messages or broadcast messages sent from your LINE Official Account. 
            * @param requestId Request ID of a narrowcast message or broadcast message. Each Messaging API request has a request ID.  (required)
     * 
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#get-message-event">Get user interaction statistics Documentation</a>
     */
    @GET("/v2/bot/insight/message/event")
    CompletableFuture<Result<GetMessageEventResponse>> getMessageEvent(@Query("requestId") String requestId
);

        /**
        * Get number of followers
        * Returns the number of users who have added the LINE Official Account on or before a specified date. 
            * @param date Date for which to retrieve the number of followers.  Format: yyyyMMdd (e.g. 20191231) Timezone: UTC+9  (optional)
     * 
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#get-number-of-followers">Get number of followers Documentation</a>
     */
    @GET("/v2/bot/insight/followers")
    CompletableFuture<Result<GetNumberOfFollowersResponse>> getNumberOfFollowers(@Query("date") String date
);

        /**
        * Get number of message deliveries
        * Returns the number of messages sent from LINE Official Account on a specified day. 
            * @param date Date for which to retrieve number of sent messages. - Format: yyyyMMdd (e.g. 20191231) - Timezone: UTC+9  (required)
     * 
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#get-number-of-delivery-messages">Get number of message deliveries Documentation</a>
     */
    @GET("/v2/bot/insight/message/delivery")
    CompletableFuture<Result<GetNumberOfMessageDeliveriesResponse>> getNumberOfMessageDeliveries(@Query("date") String date
);

        /**
        * 
        * You can check the per-unit statistics of how users interact with push messages and multicast messages sent from your LINE Official Account. 
            * @param customAggregationUnit Name of aggregation unit specified when sending the message. Case-sensitive. For example, `Promotion_a` and `Promotion_A` are regarded as different unit names.  (required)
            * @param from Start date of aggregation period.  Format: yyyyMMdd (e.g. 20210301) Time zone: UTC+9  (required)
            * @param to End date of aggregation period. The end date can be specified for up to 30 days later. For example, if the start date is 20210301, the latest end date is 20210331.  Format: yyyyMMdd (e.g. 20210301) Time zone: UTC+9  (required)
     * 
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#get-statistics-per-unit"> Documentation</a>
     */
    @GET("/v2/bot/insight/message/event/aggregation")
    CompletableFuture<Result<GetStatisticsPerUnitResponse>> getStatisticsPerUnit(@Query("customAggregationUnit") String customAggregationUnit
, @Query("from") String from
, @Query("to") String to
);


    public static ApiAuthenticatedClientBuilder<InsightClient> builder(String channelToken) {
        return new ApiAuthenticatedClientBuilder<>(URI.create("https://api.line.me"), InsightClient.class, new InsightExceptionBuilder(), channelToken);
    }
    public static ApiAuthenticatedClientBuilder<InsightClient> builder(ChannelTokenSupplier channelTokenSupplier) {
        return new ApiAuthenticatedClientBuilder<>(URI.create("https://api.line.me"), InsightClient.class, new InsightExceptionBuilder(), channelTokenSupplier);
    }

}
