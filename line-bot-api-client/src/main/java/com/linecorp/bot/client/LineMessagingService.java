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
import com.linecorp.bot.model.profile.MembersIdsResponse;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.GetNumberOfFollowersResponse;
import com.linecorp.bot.model.response.GetNumberOfMessageDeliveriesResponse;
import com.linecorp.bot.model.response.IssueLinkTokenResponse;
import com.linecorp.bot.model.response.MessageQuotaResponse;
import com.linecorp.bot.model.response.NarrowcastProgressResponse;
import com.linecorp.bot.model.response.NumberOfMessagesResponse;
import com.linecorp.bot.model.response.QuotaConsumptionResponse;
import com.linecorp.bot.model.response.demographics.GetFriendsDemographicsResponse;
import com.linecorp.bot.model.richmenu.RichMenu;
import com.linecorp.bot.model.richmenu.RichMenuBulkLinkRequest;
import com.linecorp.bot.model.richmenu.RichMenuBulkUnlinkRequest;
import com.linecorp.bot.model.richmenu.RichMenuIdResponse;
import com.linecorp.bot.model.richmenu.RichMenuListResponse;
import com.linecorp.bot.model.richmenu.RichMenuResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
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
    Call<BotApiResponseBody> pushMessage(@Body PushMessage pushMessage);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#multicast(Multicast)
     */
    @POST("v2/bot/message/multicast")
    Call<BotApiResponseBody> multicast(@Body Multicast multicast);

    /**
     * Sends push messages to multiple users at any time.
     */
    @POST("v2/bot/message/broadcast")
    Call<BotApiResponseBody> broadcast(@Body Broadcast broadcast);

    /**
     * Sends a push message to multiple users. You can specify recipients using attributes (such as age, gender,
     * OS, and region) or by retargeting (audiences). Messages cannot be sent to groups or rooms.
     */
    @POST("v2/bot/message/narrowcast")
    Call<BotApiResponseBody> narrowcast(@Body Narrowcast narrowcast);

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
     *             {@literal "20191231"}) and the timezone should be UTC+9.
     */
    @GET("v2/bot/message/delivery/reply")
    Call<NumberOfMessagesResponse> getNumberOfSentReplyMessages(@Query("date") String date);

    /**
     * Gets the number of messages sent with the /bot/message/push endpoint. Note that the number of messages
     * retrieved by this operation does not include the number of messages sent from LINE@ Manager.
     *
     * @param date Date the messages were sent. The format should be {@code yyyyMMdd} (for Example:
     *             {@literal "20191231"}) and the timezone should be UTC+9.
     */
    @GET("v2/bot/message/delivery/push")
    Call<NumberOfMessagesResponse> getNumberOfSentPushMessages(@Query("date") String date);

    /**
     * Gets the number of messages sent with the /bot/message/multicast endpoint. The number of messages
     * retrieved by this operation does not include the number of messages sent from LINE@ Manager.
     *
     * @param date Date the messages were sent. The format should be {@code yyyyMMdd} (for Example:
     *             {@literal "20191231"}) and the timezone should be UTC+9.
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
     *             {@literal "20191231"}) and the timezone should be UTC+9.
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
     *             For example: {@literal "20191231"}) and the timezone should be UTC+9.
     */
    @GET("v2/bot/insight/message/delivery")
    Call<GetNumberOfMessageDeliveriesResponse> getNumberOfMessageDeliveries(@Query("date") String date);

    /**
     * Returns the number of users who have added the bot on or before a specified date.
     *
     * @param date Date for which to retrieve the number of followers. The format should be {@code yyyyMMdd}.
     *             For example: {@literal "20191231"}) and the timezone should be UTC+9.
     */
    @GET("v2/bot/insight/followers")
    Call<GetNumberOfFollowersResponse> getNumberOfFollowers(@Query("date") String date);

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
