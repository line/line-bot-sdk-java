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

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.OngoingStubbing;

import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.profile.MembersIdsResponse;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.model.response.IssueLinkTokenResponse;
import com.linecorp.bot.model.response.NumberOfMessagesResponse;
import com.linecorp.bot.model.response.NumberOfMessagesResponse.Status;
import com.linecorp.bot.model.richmenu.RichMenu;
import com.linecorp.bot.model.richmenu.RichMenuIdResponse;
import com.linecorp.bot.model.richmenu.RichMenuListResponse;
import com.linecorp.bot.model.richmenu.RichMenuResponse;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LineMessagingClientImplTest {
    private static final byte[] ZERO_BYTES = {};
    private static final BotApiResponse BOT_API_SUCCESS_RESPONSE = new BotApiResponse("", emptyList());
    private static final RichMenuIdResponse RICH_MENU_ID_RESPONSE = new RichMenuIdResponse("ID");

    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public final Timeout timeoutRule = Timeout.seconds(5);

    @Mock
    private LineMessagingService retrofitMock;

    @InjectMocks
    private LineMessagingClientImpl target;

    @Test
    public void replyMessageTest() throws Exception {
        whenCall(retrofitMock.replyMessage(any()),
                 BOT_API_SUCCESS_RESPONSE);
        final ReplyMessage replyMessage = new ReplyMessage("token", new TextMessage("Message"));

        // Do
        final BotApiResponse botApiResponse =
                target.replyMessage(replyMessage).get();

        // Verify
        verify(retrofitMock, only()).replyMessage(replyMessage);
        assertThat(botApiResponse).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    @Test
    public void pushMessageTest() throws Exception {
        whenCall(retrofitMock.pushMessage(any()),
                 BOT_API_SUCCESS_RESPONSE);
        final PushMessage pushMessage = new PushMessage("TO", new TextMessage("text"));

        // Do
        final BotApiResponse botApiResponse =
                target.pushMessage(pushMessage).get();

        // Verify
        verify(retrofitMock, only()).pushMessage(pushMessage);
        assertThat(botApiResponse).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    @Test
    public void multicastTest() throws Exception {
        whenCall(retrofitMock.multicast(any()),
                 BOT_API_SUCCESS_RESPONSE);
        final Multicast multicast = new Multicast(singleton("TO"), new TextMessage("text"));

        // Do
        final BotApiResponse botApiResponse =
                target.multicast(multicast).get();

        // Verify
        verify(retrofitMock, only()).multicast(multicast);
        assertThat(botApiResponse).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    @Test
    public void getMessageContentTest() throws Exception {
        whenCall(retrofitMock.getMessageContent(any()),
                 ResponseBody.create(MediaType.parse("image/jpeg"), ZERO_BYTES));

        // Do
        final MessageContentResponse contentResponse = target.getMessageContent("ID").get();

        // Verify
        verify(retrofitMock, only()).getMessageContent("ID");
        assertThat(contentResponse.getLength()).isEqualTo(0);
        assertThat(contentResponse.getMimeType()).isEqualTo("image/jpeg");
    }

    @Test
    public void getNumberOfSentReplyMessages() {
        whenCall(retrofitMock.getNumberOfSentReplyMessages(any()),
                 new NumberOfMessagesResponse(Status.Ready, 1024));

        NumberOfMessagesResponse response = target.getNumberOfSentReplyMessages("20181231").join();
        verify(retrofitMock, only()).getNumberOfSentReplyMessages("20181231");
        assertThat(response.getStatus()).isEqualTo(Status.Ready);
        assertThat(response.getSuccess()).isEqualTo(1024);
    }

    @Test
    public void getNumberOfSentPushMessages() {
        whenCall(retrofitMock.getNumberOfSentPushMessages(any()),
                 new NumberOfMessagesResponse(Status.Ready, 1024));

        NumberOfMessagesResponse response = target.getNumberOfSentPushMessages("20181231").join();
        verify(retrofitMock, only()).getNumberOfSentPushMessages("20181231");
        assertThat(response.getStatus()).isEqualTo(Status.Ready);
        assertThat(response.getSuccess()).isEqualTo(1024);
    }

    @Test
    public void getNumberOfSentMulticastMessages() {
        whenCall(retrofitMock.getNumberOfSentMulticastMessages(any()),
                 new NumberOfMessagesResponse(Status.Ready, 1024));

        NumberOfMessagesResponse response = target.getNumberOfSentMulticastMessages("20181231").join();
        verify(retrofitMock, only()).getNumberOfSentMulticastMessages("20181231");
        assertThat(response.getStatus()).isEqualTo(Status.Ready);
        assertThat(response.getSuccess()).isEqualTo(1024);
    }

    @Test
    public void getProfileTest() throws Exception {
        final UserProfileResponse mockUserProfileResponse =
                new UserProfileResponse("displayName", "userId", "pictureUrl", "statusMessage");
        whenCall(retrofitMock.getProfile(any()),
                 mockUserProfileResponse);

        // Do
        final UserProfileResponse response = target.getProfile("USER_ID").get();

        // Verify
        verify(retrofitMock, only()).getProfile("USER_ID");
        assertThat(response).isEqualTo(mockUserProfileResponse);
    }

    @Test
    public void getProfileOfGroupMemberTest() throws Exception {
        final UserProfileResponse mockUserProfileResponse =
                new UserProfileResponse("displayName", "userId", "pictureUrl", null);
        whenCall(retrofitMock.getMemberProfile(any(), any(), any()),
                 mockUserProfileResponse);

        // Do
        final UserProfileResponse response = target.getGroupMemberProfile("GROUP_ID", "USER_ID").get();

        // Verify
        verify(retrofitMock, only()).getMemberProfile("group", "GROUP_ID", "USER_ID");
        assertThat(response).isEqualTo(mockUserProfileResponse);
    }

    @Test
    public void getGroupMembersIdsTest() throws Exception {
        final MembersIdsResponse membersIdsResponse = new MembersIdsResponse(emptyList(), "TOKEN");
        whenCall(retrofitMock.getMembersIds(any(), any(), any()), membersIdsResponse);

        // Do
        final MembersIdsResponse response = target.getGroupMembersIds("GROUP_ID", "USER_ID").get();

        // Verify
        verify(retrofitMock, only()).getMembersIds("group", "GROUP_ID", "USER_ID");
        assertThat(response).isSameAs(membersIdsResponse);
    }

    @Test
    public void getRoomMembersIdsTest() throws Exception {
        final MembersIdsResponse membersIdsResponse = new MembersIdsResponse(emptyList(), "TOKEN");
        whenCall(retrofitMock.getMembersIds(any(), any(), any()), membersIdsResponse);

        // Do
        final MembersIdsResponse response = target.getRoomMembersIds("ROOM_ID", "USER_ID").get();

        // Verify
        verify(retrofitMock, only()).getMembersIds("room", "ROOM_ID", "USER_ID");
        assertThat(response).isSameAs(membersIdsResponse);
    }

    @Test
    public void leaveGroupTest() throws Exception {
        whenCall(retrofitMock.leaveGroup(any()),
                 BOT_API_SUCCESS_RESPONSE);

        // Do
        final BotApiResponse botApiResponse = target.leaveGroup("ID").get();

        // Verify
        verify(retrofitMock, only()).leaveGroup("ID");
        assertThat(botApiResponse).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    @Test
    public void leaveRoomTest() throws Exception {
        whenCall(retrofitMock.leaveRoom(any()),
                 BOT_API_SUCCESS_RESPONSE);

        // Do
        final BotApiResponse botApiResponse = target.leaveRoom("ID").get();

        // Verify
        verify(retrofitMock, only()).leaveRoom("ID");
        assertThat(botApiResponse).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    @Test
    public void getRichMenuTest() throws Exception {
        final RichMenuResponse richMenuReference = new RichMenuResponse(null, null, false, null, null, null);
        whenCall(retrofitMock.getRichMenu(any()), richMenuReference);

        // Do
        final RichMenuResponse richMenuResponse = target.getRichMenu("ID").get();

        // Verify
        verify(retrofitMock, only()).getRichMenu("ID");
        assertThat(richMenuResponse).isSameAs(richMenuReference);
    }

    @Test
    public void createRichMenuTest() throws Exception {
        final RichMenu richMenuReference = RichMenu.builder().build();
        whenCall(retrofitMock.createRichMenu(any()), RICH_MENU_ID_RESPONSE);

        // Do
        final RichMenuIdResponse richMenuIdResponse = target.createRichMenu(richMenuReference).get();

        // Verify
        verify(retrofitMock, only()).createRichMenu(richMenuReference);
        assertThat(richMenuIdResponse).isEqualTo(RICH_MENU_ID_RESPONSE);
    }

    @Test
    public void deleteRichMenuTest() throws Exception {
        whenCall(retrofitMock.deleteRichMenu(any()),
                 null);

        // Do
        final BotApiResponse botApiResponse = target.deleteRichMenu("ID").get();

        // Verify
        verify(retrofitMock, only()).deleteRichMenu("ID");
        assertThat(botApiResponse).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    @Test
    public void getRichMenuIdOfUserTest() throws Exception {
        whenCall(retrofitMock.getRichMenuIdOfUser(any()),
                 RICH_MENU_ID_RESPONSE);

        // Do
        final RichMenuIdResponse richMenuIdResponse = target.getRichMenuIdOfUser("ID").get();

        // Verify
        verify(retrofitMock, only()).getRichMenuIdOfUser("ID");
        assertThat(richMenuIdResponse).isEqualTo(RICH_MENU_ID_RESPONSE);

    }

    @Test
    public void linkRichMenuToUserTest() throws Exception {
        whenCall(retrofitMock.linkRichMenuToUser(any(), any()),
                 null);

        // Do
        final BotApiResponse botApiResponse = target.linkRichMenuIdToUser("USER_ID", "RICH_MENU_ID").get();

        // Verify
        verify(retrofitMock, only()).linkRichMenuToUser("USER_ID", "RICH_MENU_ID");
        assertThat(botApiResponse).isEqualTo(BOT_API_SUCCESS_RESPONSE);

    }

    @Test
    public void unlinkRichMenuIdToUserTest() throws Exception {
        whenCall(retrofitMock.unlinkRichMenuIdFromUser(any()),
                 null);

        // Do
        final BotApiResponse botApiResponse = target.unlinkRichMenuIdFromUser("ID").get();

        // Verify
        verify(retrofitMock, only()).unlinkRichMenuIdFromUser("ID");
        assertThat(botApiResponse).isEqualTo(BOT_API_SUCCESS_RESPONSE);

    }

    @Test
    public void getRichMenuImageTest() throws Exception {
        whenCall(retrofitMock.getRichMenuImage(any()),
                 ResponseBody.create(MediaType.parse("image/jpeg"), ZERO_BYTES));

        // Do
        final MessageContentResponse messageContentResponse = target.getRichMenuImage("ID").get();

        // Verify
        verify(retrofitMock, only()).getRichMenuImage("ID");
        assertThat(messageContentResponse.getLength()).isZero();
    }

    @Test
    public void uploadRichMenuImageTest() throws Exception {
        whenCall(retrofitMock.uploadRichMenuImage(any(), any()),
                 null);

        // Do
        final BotApiResponse botApiResponse =
                target.setRichMenuImage("ID", "image/jpeg", ZERO_BYTES).get();

        // Verify
        verify(retrofitMock, only())
                .uploadRichMenuImage(eq("ID"), any());
        assertThat(botApiResponse).isEqualTo(BOT_API_SUCCESS_RESPONSE);

    }

    @Test
    public void getRichMenuListTest() throws Exception {
        whenCall(retrofitMock.getRichMenuList(),
                 new RichMenuListResponse(emptyList()));

        // Do
        final RichMenuListResponse richMenuListResponse = target.getRichMenuList().get();

        // Verify
        verify(retrofitMock, only()).getRichMenuList();
        assertThat(richMenuListResponse.getRichMenus()).isEmpty();

    }

    @Test
    public void setDefaultRichMenuTest() throws Exception {
        whenCall(retrofitMock.setDefaultRichMenu(any()), null);

        // Do
        final BotApiResponse botApiResponse = target.setDefaultRichMenu("ID").get();

        // Verify
        verify(retrofitMock, only())
                .setDefaultRichMenu("ID");
        assertThat(botApiResponse).isEqualTo(BOT_API_SUCCESS_RESPONSE);

    }

    @Test
    public void getDefaultRichMenuIdTest() throws Exception {
        whenCall(retrofitMock.getDefaultRichMenuId(), RICH_MENU_ID_RESPONSE);

        // Do
        final RichMenuIdResponse richMenuIdResponse = target.getDefaultRichMenuId().get();

        // Verify
        verify(retrofitMock, only()).getDefaultRichMenuId();
        assertThat(richMenuIdResponse).isEqualTo(RICH_MENU_ID_RESPONSE);

    }

    @Test
    public void cancelDefaultRichMenu() throws Exception {
        whenCall(retrofitMock.cancelDefaultRichMenu(), null);

        // Do
        final BotApiResponse botApiResponse = target.cancelDefaultRichMenu().get();

        // Verify
        verify(retrofitMock, only()).cancelDefaultRichMenu();
        assertThat(botApiResponse).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    @Test
    public void issueLinkToken() throws Exception {
        whenCall(retrofitMock.issueLinkToken(any()), new IssueLinkTokenResponse("ID"));
        final IssueLinkTokenResponse response = target.issueLinkToken("ID").get();
        verify(retrofitMock, only()).issueLinkToken("ID");
        assertThat(response).isEqualTo(new IssueLinkTokenResponse("ID"));
    }

    // Utility methods

    private static <T> void whenCall(Call<T> call, T value) {
        final OngoingStubbing<Call<T>> callOngoingStubbing = when(call);
        callOngoingStubbing.thenReturn(enqueue(value));
    }

    private static <T> Call<T> enqueue(T value) {
        return new Call<T>() {
            @Override
            public Response<T> execute() throws IOException {
                throw new UnsupportedOperationException();
            }

            @Override
            public void enqueue(Callback<T> callback) {
                callback.onResponse(this, Response.success(value));
            }

            @Override
            public boolean isExecuted() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void cancel() {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean isCanceled() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Call<T> clone() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Request request() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
