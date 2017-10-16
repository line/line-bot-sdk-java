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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

/**
 * @deprecated Please use {@link LineMessagingClient} instead.
 */
@Deprecated
public interface LineMessagingService {
    /**
     * Reply to messages from users.
     *
     * <p>Webhooks are used to notify you when an event occurs. For events that you can respond to,
     * a replyToken is issued for replying to messages.
     * <p>Because the replyToken becomes invalid after a certain period of time,
     * responses should be sent as soon as a message is received. Reply tokens can only be used once.
     *
     * @see #pushMessage(PushMessage)
     * @see <a href="https://devdocs.line.me?java#reply-message">//devdocs.line.me#reply-message</a>
     */
    @POST("v2/bot/message/reply")
    Call<BotApiResponse> replyMessage(@Body ReplyMessage replyMessage);

    /**
     * Send messages to users when you want to.
     *
     * <p>INFO: Use of the Push Message API is limited to certain plans.
     *
     * @see #replyMessage(ReplyMessage)
     * @see <a href="https://devdocs.line.me?java#push-message">//devdocs.line.me#push-message</a>
     */
    @POST("v2/bot/message/push")
    Call<BotApiResponse> pushMessage(@Body PushMessage pushMessage);

    /**
     * Send messages to multiple users at any time. <strong>IDs of groups or rooms cannot be used.</strong>
     *
     * <p>INFO: Only available for plans which support push messages. Messages cannot be sent to groups or rooms.
     * <p>INFO: Use IDs returned via the webhook event of source users. IDs of groups or rooms cannot be used.
     * Do not use the LINE ID found on the LINE app.</p>
     * @see #pushMessage(PushMessage)
     * @see <a href="https://devdocs.line.me?java#multicast">//devdocs.line.me#multicast</a>
     */
    @POST("v2/bot/message/multicast")
    Call<BotApiResponse> multicast(@Body Multicast multicast);

    /**
     * Download image, video, and audio data sent from users.
     *
     * @see <a href="https://devdocs.line.me?java#get-content">//devdocs.line.me#get-content</a>
     */
    @Streaming
    @GET("v2/bot/message/{messageId}/content")
    Call<ResponseBody> getMessageContent(@Path("messageId") String messageId);

    /**
     * Get user profile information.
     *
     * @see <a href="https://devdocs.line.me?java#bot-api-get-profile">//devdocs.line.me#bot-api-get-profile</a>
     */
    @GET("v2/bot/profile/{userId}")
    Call<UserProfileResponse> getProfile(@Path("userId") String userId);

    /**
     * Get Group/Room member profile.
     *
     * @param sourceType "room" or "group".
     * @param senderId Identifier of the group/room.
     * @param userId Identifier of the user.
     *
     * @see <a href="https://devdocs.line.me?java#get-group-room-member-profile">//devdocs.line.me#get-group-room-member-profile</a>
     */
    @GET("v2/bot/{sourceType}/{senderId}/member/{userId}")
    Call<UserProfileResponse> getMemberProfile(
            @Path("sourceType") String sourceType,
            @Path("senderId") String senderId,
            @Path("userId") String userId);

    /**
     * Get group/room member IDs.
     *
     * <p>Get group and room member IDs request example
     * <p>Gets the user IDs of the members of a group or a room that the bot is in. This includes the user IDs of users who have not added the bot as a friend or has blocked the bot.
     *
     * <p><strong>INFO</strong> This feature is only available for LINE@ Approved accounts or official accounts.
     *
     * @param sourceType "room" or "group".
     * @param senderId Identifier of the group/room.
     * @param start continuationToken to next page.
     *
     * @see <a href="https://devdocs.line.me?java#get-group-room-member-profile">//devdocs.line.me#get-group-room-member-profile</a>
     */
    @GET("v2/bot/{sourceType}/{senderId}/members/ids")
    Call<MembersIdsResponse> getMembersIds(
            @Path("sourceType") String sourceType,
            @Path("senderId") String senderId,
            @Query("start") String start);

    /**
     * Leave a group.
     *
     * @see <a href="https://devdocs.line.me?java#leave">//devdocs.line.me#leave</a>
     */
    @POST("v2/bot/group/{groupId}/leave")
    Call<BotApiResponse> leaveGroup(@Path("groupId") String groupId);

    /**
     * Leave a room.
     *
     * @see <a href="https://devdocs.line.me?java#leave">//devdocs.line.me#leave</a>
     */
    @POST("v2/bot/room/{roomId}/leave")
    Call<BotApiResponse> leaveRoom(@Path("roomId") String roomId);
}
