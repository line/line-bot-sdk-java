/*
 * Copyright 2016 LINE Corporation
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

import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.profile.MembersIdsResponse;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.model.response.IssueLinkTokenResponse;
import com.linecorp.bot.model.response.NumberOfMessagesResponse;
import com.linecorp.bot.model.richmenu.RichMenu;
import com.linecorp.bot.model.richmenu.RichMenuIdResponse;
import com.linecorp.bot.model.richmenu.RichMenuListResponse;
import com.linecorp.bot.model.richmenu.RichMenuResponse;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

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
    Call<BotApiResponse> replyMessage(@Body ReplyMessage replyMessage);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#pushMessage(PushMessage)
     */
    @POST("v2/bot/message/push")
    Call<BotApiResponse> pushMessage(@Body PushMessage pushMessage);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#multicast(Multicast)
     */
    @POST("v2/bot/message/multicast")
    Call<BotApiResponse> multicast(@Body Multicast multicast);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#getMessageContent(String)
     */
    @Streaming
    @GET("v2/bot/message/{messageId}/content")
    Call<ResponseBody> getMessageContent(@Path("messageId") String messageId);

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
    Call<BotApiResponse> leaveGroup(@Path("groupId") String groupId);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#leaveRoom(String)
     */
    @POST("v2/bot/room/{roomId}/leave")
    Call<BotApiResponse> leaveRoom(@Path("roomId") String roomId);

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
     * @see LineMessagingClient#unlinkRichMenuIdFromUser(String)
     */
    @DELETE("v2/bot/user/{userId}/richmenu")
    Call<Void> unlinkRichMenuIdFromUser(@Path("userId") String userId);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#getRichMenuImage(String)
     */
    @GET("v2/bot/richmenu/{richMenuId}/content")
    Call<ResponseBody> getRichMenuImage(@Path("richMenuId") String richMenuId);

    /**
     * Method for Retrofit.
     *
     * @see LineMessagingClient#setRichMenuImage(String, String, byte[])
     */
    @POST("v2/bot/richmenu/{richMenuId}/content")
    Call<Void> uploadRichMenuImage(
            @Path("richMenuId") String richMenuId,
            @Body RequestBody requestBody);

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
}
