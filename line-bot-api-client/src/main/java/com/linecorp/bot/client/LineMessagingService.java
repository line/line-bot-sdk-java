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

import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface LineMessagingService {
    @POST("/v2/bot/message/reply")
    Call<BotApiResponse> replyMessage(@Body ReplyMessage replyMessage);

    @POST("/v2/bot/message/push")
    Call<BotApiResponse> pushMessage(@Body PushMessage pushMessage);

    @Streaming
    @GET("/v2/bot/message/{messageId}/content")
    Call<ResponseBody> getContent(@Path("messageId") String messageId);

    /**
     * The profile information of any specified user can be obtained.
     */
    @GET("/v2/bot/profile/{userId}")
    Call<UserProfileResponse> getProfile(@Path("userId") String userId);

    @POST("/v2/bot/group/{groupId}/leave")
    Call<BotApiResponse> leaveGroup(@Path("groupId") String groupId);

    @POST("/v2/bot/room/{roomId}/leave")
    Call<BotApiResponse> leaveRoom(@Path("roomId") String roomId);
}
