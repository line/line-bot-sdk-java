package com.linecorp.bot.client;

import java.util.List;

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
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface LineMessagingService {
    @POST("/v2/bot/message/reply")
    Call<BotApiResponse> reply(@Body ReplyMessage replyMessage);

    @POST("/v2/bot/message/push")
    Call<BotApiResponse> push(@Body PushMessage pushMessage);

    @Streaming
    @GET("/v2/bot/message/{messageId}/content")
    Call<ResponseBody> getContent(@Path("messageId") String messageId);

    /**
     * The profile information of any specified user can be obtained.
     */
    @GET("/v2/bot/profile")
    Call<UserProfileResponse> getProfile(@Query("userId") List<String> userId);

    @POST("/v2/bot/group/{groupId}/leave")
    Call<BotApiResponse> leaveGroup(@Path("groupId") String groupId);

    @POST("/v2/bot/room/{roomId}/leave")
    Call<BotApiResponse> leaveRoom(@Path("roomId") String roomId);
}
