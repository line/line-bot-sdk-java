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

import java.util.List;

import com.linecorp.bot.model.Broadcast;
import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.Narrowcast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.group.GroupMemberCountResponse;
import com.linecorp.bot.model.group.GroupSummaryResponse;
import com.linecorp.bot.model.profile.MembersIdsResponse;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.request.SetWebhookEndpointRequest;
import com.linecorp.bot.model.request.TestWebhookEndpointRequest;
import com.linecorp.bot.model.response.BotInfoResponse;
import com.linecorp.bot.model.response.GetAggregationUnitNameListResponse;
import com.linecorp.bot.model.response.GetAggregationUnitUsageResponse;
import com.linecorp.bot.model.response.GetFollowersResponse;
import com.linecorp.bot.model.response.GetMessageEventResponse;
import com.linecorp.bot.model.response.GetNumberOfFollowersResponse;
import com.linecorp.bot.model.response.GetNumberOfMessageDeliveriesResponse;
import com.linecorp.bot.model.response.GetStatisticsPerUnitResponse;
import com.linecorp.bot.model.response.GetWebhookEndpointResponse;
import com.linecorp.bot.model.response.IssueLinkTokenResponse;
import com.linecorp.bot.model.response.MessageQuotaResponse;
import com.linecorp.bot.model.response.NarrowcastProgressResponse;
import com.linecorp.bot.model.response.NumberOfMessagesResponse;
import com.linecorp.bot.model.response.QuotaConsumptionResponse;
import com.linecorp.bot.model.response.SetWebhookEndpointResponse;
import com.linecorp.bot.model.response.TestWebhookEndpointResponse;
import com.linecorp.bot.model.response.demographics.GetFriendsDemographicsResponse;
import com.linecorp.bot.model.richmenu.RichMenu;
import com.linecorp.bot.model.richmenu.RichMenuBulkLinkRequest;
import com.linecorp.bot.model.richmenu.RichMenuBulkUnlinkRequest;
import com.linecorp.bot.model.richmenu.RichMenuIdResponse;
import com.linecorp.bot.model.richmenu.RichMenuListResponse;
import com.linecorp.bot.model.richmenu.RichMenuResponse;
import com.linecorp.bot.model.richmenualias.CreateRichMenuAliasRequest;
import com.linecorp.bot.model.richmenualias.RichMenuAliasListResponse;
import com.linecorp.bot.model.richmenualias.RichMenuAliasResponse;
import com.linecorp.bot.model.richmenualias.UpdateRichMenuAliasRequest;
import com.linecorp.bot.model.room.RoomMemberCountResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Since 2018-06. This class is package private. Please use {@link LineMessagingClient} instead.
 * It's implementation free.
 */
interface LineMessagingService {
    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#replyMessage(ReplyMessage)
     */
    @POST("v2/bot/message/reply")
    Call<BotApiResponseBody> replyMessage(@Body ReplyMessage replyMessage);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#pushMessage(PushMessage)
     */
    @POST("v2/bot/message/push")
    Call<BotApiResponseBody> pushMessage(@Header("X-Line-Retry-Key") String retryKey,
                                         @Body PushMessage pushMessage);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#multicast(Multicast)
     */
    @POST("v2/bot/message/multicast")
    Call<BotApiResponseBody> multicast(@Header("X-Line-Retry-Key") String retryKey,
                                       @Body Multicast multicast);

    /**
     * Sends push messages to multiple users at any time.
     */
    @POST("v2/bot/message/broadcast")
    Call<BotApiResponseBody> broadcast(@Header("X-Line-Retry-Key") String retryKey,
                                       @Body Broadcast broadcast);

    /**
     * Sends a push message to multiple users. You can specify recipients using attributes (such as age, gender,
     * OS, and region) or by retargeting (audiences). Messages cannot be sent to groups or rooms.
     */
    @POST("v2/bot/message/narrowcast")
    Call<BotApiResponseBody> narrowcast(@Header("X-Line-Retry-Key") String retryKey,
                                        @Body Narrowcast narrowcast);

    /**
     * Gets the status of a narrowcast message.
     */
    @GET("v2/bot/message/progress/narrowcast")
    Call<NarrowcastProgressResponse> getNarrowcastProgress(@Query("requestId") String requestId);

    /**
     * Gets the target limit for additional messages in the current month. The number of messages retrieved by
     * this operation includes the number of messages sent from LINE Official Account Manager.
     * Set a target limit with LINE Official Account Manager. For the procedures, refer to the LINE Official
     * Account Manager manual.
     * Note: LINE@ accounts cannot call this API endpoint.
     */
    @GET("v2/bot/message/quota")
    Call<MessageQuotaResponse> getMessageQuota();

    /**
     * Gets the number of messages sent in the current month. The number of messages retrieved by this
     * operation includes the number of messages sent from LINE Official Account Manager. The number of
     * messages retrieved by this operation is approximate. To get the correct number of sent messages,
     * use LINE Official Account Manager or execute API operations for getting the number of sent messages.
     * Note: LINE@ accounts cannot call this API endpoint.
     */
    @GET("v2/bot/message/quota/consumption")
    Call<QuotaConsumptionResponse> getMessageQuotaConsumption();

    /**
     * Gets the number of messages sent with the /bot/message/reply endpoint. Note that the number of messages
     * retrieved by this operation does not include the number of messages sent from LINE@ Manager.
     *
     * @param date Date the messages were sent. The format should be {@code yyyyMMdd} (for Example:
     *         {@literal "20191231"}) and the timezone should be UTC+9.
     */
    @GET("v2/bot/message/delivery/reply")
    Call<NumberOfMessagesResponse> getNumberOfSentReplyMessages(@Query("date") String date);

    /**
     * Gets the number of messages sent with the /bot/message/push endpoint. Note that the number of messages
     * retrieved by this operation does not include the number of messages sent from LINE@ Manager.
     *
     * @param date Date the messages were sent. The format should be {@code yyyyMMdd} (for Example:
     *         {@literal "20191231"}) and the timezone should be UTC+9.
     */
    @GET("v2/bot/message/delivery/push")
    Call<NumberOfMessagesResponse> getNumberOfSentPushMessages(@Query("date") String date);

    /**
     * Gets the number of messages sent with the /bot/message/multicast endpoint. The number of messages
     * retrieved by this operation does not include the number of messages sent from LINE@ Manager.
     *
     * @param date Date the messages were sent. The format should be {@code yyyyMMdd} (for Example:
     *         {@literal "20191231"}) and the timezone should be UTC+9.
     */
    @GET("v2/bot/message/delivery/multicast")
    Call<NumberOfMessagesResponse> getNumberOfSentMulticastMessages(@Query("date") String date);

    /**
     * Gets the number of messages sent with the {@code "/bot/message/broadcast"} endpoint. The number of
     * messages retrieved by this operation does not include the number of messages sent from LINE Official
     * Account Manager.
     * Note: LINE@ accounts cannot call this API endpoint. Please migrate it to a LINE official account.
     * For more information, see <a href="https://developers.line.biz/en/docs/messaging-api/migrating-line-at/">
     * Migration of LINE@ accounts</a>.
     *
     * @param date Date the messages were sent. The format should be {@code yyyyMMdd} (for Example:
     *         {@literal "20191231"}) and the timezone should be UTC+9.
     */
    @GET("v2/bot/message/delivery/broadcast")
    Call<NumberOfMessagesResponse> getNumberOfSentBroadcastMessages(@Query("date") String date);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#getProfile(String)
     */
    @GET("v2/bot/profile/{userId}")
    Call<UserProfileResponse> getProfile(@Path("userId") String userId);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#getGroupMemberProfile(String, String)
     * @see LineMessagingClient#getRoomMemberProfile(String, String)
     */
    @GET("v2/bot/{sourceType}/{senderId}/member/{userId}")
    Call<UserProfileResponse> getMemberProfile(
            @Path("sourceType") String sourceType,
            @Path("senderId") String senderId,
            @Path("userId") String userId);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#getGroupMembersIds(String, String)
     * @see LineMessagingClient#getRoomMembersIds(String, String)
     */
    @GET("v2/bot/{sourceType}/{senderId}/members/ids")
    Call<MembersIdsResponse> getMembersIds(
            @Path("sourceType") String sourceType,
            @Path("senderId") String senderId,
            @Query("start") String start);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#leaveGroup(String)
     */
    @POST("v2/bot/group/{groupId}/leave")
    Call<BotApiResponseBody> leaveGroup(@Path("groupId") String groupId);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#leaveRoom(String)
     */
    @POST("v2/bot/room/{roomId}/leave")
    Call<BotApiResponseBody> leaveRoom(@Path("roomId") String roomId);

    /**
     * Get group summary.
     */
    @GET("v2/bot/group/{groupId}/summary")
    Call<GroupSummaryResponse> getGroupSummary(@Path("groupId") String groupId);

    /**
     * Get members in group count.
     */
    @GET("v2/bot/group/{groupId}/members/count")
    Call<GroupMemberCountResponse> getGroupMemberCount(@Path("groupId") String groupId);

    /**
     * Get members in room count.
     */
    @GET("v2/bot/room/{roomId}/members/count")
    Call<RoomMemberCountResponse> getRoomMemberCount(@Path("roomId") String roomId);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#getRichMenu(String)
     */
    @GET("v2/bot/richmenu/{richMenuId}")
    Call<RichMenuResponse> getRichMenu(@Path("richMenuId") String richMenuId);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#createRichMenu(RichMenu)
     */
    @POST("v2/bot/richmenu")
    Call<RichMenuIdResponse> createRichMenu(@Body RichMenu richMenu);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#deleteRichMenu(String)
     */
    @DELETE("v2/bot/richmenu/{richMenuId}")
    Call<Void> deleteRichMenu(@Path("richMenuId") String richMenuId);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#getRichMenuIdOfUser(String)
     */
    @GET("v2/bot/user/{userId}/richmenu")
    Call<RichMenuIdResponse> getRichMenuIdOfUser(@Path("userId") String userId);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#linkRichMenuIdToUser(String, String)
     */
    @POST("v2/bot/user/{userId}/richmenu/{richMenuId}")
    Call<Void> linkRichMenuToUser(
            @Path("userId") String userId,
            @Path("richMenuId") String richMenuId);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#linkRichMenuIdToUsers(List, String)
     */
    @POST("v2/bot/richmenu/bulk/link")
    Call<Void> linkRichMenuToUsers(@Body RichMenuBulkLinkRequest request);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#unlinkRichMenuIdFromUser(String)
     */
    @DELETE("v2/bot/user/{userId}/richmenu")
    Call<Void> unlinkRichMenuIdFromUser(@Path("userId") String userId);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#unlinkRichMenuIdFromUser(String)
     */
    @POST("v2/bot/richmenu/bulk/unlink")
    Call<Void> unlinkRichMenuIdFromUsers(@Body RichMenuBulkUnlinkRequest request);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#getRichMenuList()
     */
    @GET("v2/bot/richmenu/list")
    Call<RichMenuListResponse> getRichMenuList();

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#setDefaultRichMenu(String)
     */
    @POST("/v2/bot/user/all/richmenu/{richMenuId}")
    Call<Void> setDefaultRichMenu(@Path("richMenuId") String richMenuId);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#getDefaultRichMenuId()
     */
    @GET("/v2/bot/user/all/richmenu")
    Call<RichMenuIdResponse> getDefaultRichMenuId();

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#cancelDefaultRichMenu()
     */
    @DELETE("/v2/bot/user/all/richmenu")
    Call<Void> cancelDefaultRichMenu();

    /**
     * Issues a link token used for the account link feature.
     *
     * @see LineMessagingClient#issueLinkToken(String)
     */
    @POST("v2/bot/user/{userId}/linkToken")
    Call<IssueLinkTokenResponse> issueLinkToken(@Path("userId") String userId);

    /**
     * Retrieves the demographic attributes for a bot's friends.
     */
    @GET("v2/bot/insight/demographic")
    Call<GetFriendsDemographicsResponse> getFriendsDemographics();

    /**
     * Gets the number of messages sent on a specified day.
     *
     * @param date Date for which to retrieve number of sent messages. The format should be {@code yyyyMMdd}.
     *         For example: {@literal "20191231"}) and the timezone should be UTC+9.
     */
    @GET("v2/bot/insight/message/delivery")
    Call<GetNumberOfMessageDeliveriesResponse> getNumberOfMessageDeliveries(@Query("date") String date);

    /**
     * Returns the number of users who have added the bot on or before a specified date.
     *
     * @param date Date for which to retrieve the number of followers. The format should be {@code yyyyMMdd}.
     *         For example: {@literal "20191231"}) and the timezone should be UTC+9.
     */
    @GET("v2/bot/insight/followers")
    Call<GetNumberOfFollowersResponse> getNumberOfFollowers(@Query("date") String date);

    /**
     * Get a list of users who added your LINE Official Account as a friend.
     */
    @GET("v2/bot/followers/ids")
    Call<GetFollowersResponse> getFollowers(@Query("start") String next, @Query("limit") Integer limit);

    /**
     * Returns statistics about how users interact with narrowcast messages or broadcast messages sent from your
     * LINE Official Account.
     *
     * <p>You can get statistics per message or per bubble.</p>
     * @param requestId Request ID of a narrowcast message or broadcast message. Each Messaging API request has
     *                  a request ID. Find it in the response headers.
     */
    @GET("v2/bot/insight/message/event")
    Call<GetMessageEventResponse> getMessageEvent(@Query("requestId") String requestId);

    /**
     * Gets a bot's basic information.
     */
    @GET("v2/bot/info")
    Call<BotInfoResponse> getBotInfo();

    @GET("v2/bot/channel/webhook/endpoint")
    Call<GetWebhookEndpointResponse> getWebhookEndpoint();

    @PUT("v2/bot/channel/webhook/endpoint")
    Call<SetWebhookEndpointResponse> setWebhookEndpoint(@Body SetWebhookEndpointRequest request);

    @POST("v2/bot/channel/webhook/test")
    Call<TestWebhookEndpointResponse> testWebhookEndpoint(@Body TestWebhookEndpointRequest request);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#createRichMenuAlias(CreateRichMenuAliasRequest)
     */
    @POST("v2/bot/richmenu/alias")
    Call<BotApiResponseBody> createRichMenuAlias(@Body CreateRichMenuAliasRequest request);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#updateRichMenuAlias(String, UpdateRichMenuAliasRequest)
     */
    @POST("v2/bot/richmenu/alias/{richMenuAliasId}")
    Call<BotApiResponseBody> updateRichMenuAlias(
            @Path("richMenuAliasId") String richMenuAliasId,
            @Body UpdateRichMenuAliasRequest request);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#getRichMenuAliasList()
     */
    @GET("v2/bot/richmenu/alias/list")
    Call<RichMenuAliasListResponse> getRichMenuAliasList();

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#getRichMenuAlias(String)
     */
    @GET("v2/bot/richmenu/alias/{richMenuAliasId}")
    Call<RichMenuAliasResponse> getRichMenuAlias(@Path("richMenuAliasId") String richMenuAliasId);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#deleteRichMenuAlias(String)
     */
    @DELETE("v2/bot/richmenu/alias/{richMenuAliasId}")
    Call<BotApiResponseBody> deleteRichMenuAlias(@Path("richMenuAliasId") String richMenuAliasId);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#getStatisticsPerUnit(String, String, String)
     */
    @GET("v2/bot/insight/message/event/aggregation")
    Call<GetStatisticsPerUnitResponse> getStatisticsPerUnit(
            @Query("customAggregationUnit") String customAggregationUnit,
            @Query("from") String from,
            @Query("to") String to);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#getAggregationUnitUsage()
     */
    @GET("v2/bot/message/aggregation/info")
    Call<GetAggregationUnitUsageResponse> getAggregationUnitUsage();

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#getAggregationUnitNameList(String, String)
     */
    @GET("v2/bot/message/aggregation/list")
    Call<GetAggregationUnitNameListResponse> getAggregationUnitNameList(@Query("limit") String limit,
                                                                        @Query("start") String start);
}
