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

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

import com.google.common.collect.ImmutableList;

import com.linecorp.bot.model.Broadcast;
import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.Narrowcast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.group.GroupMemberCountResponse;
import com.linecorp.bot.model.group.GroupSummaryResponse;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.narrowcast.Filter;
import com.linecorp.bot.model.narrowcast.Limit;
import com.linecorp.bot.model.narrowcast.filter.GenderDemographicFilter;
import com.linecorp.bot.model.narrowcast.filter.GenderDemographicFilter.Gender;
import com.linecorp.bot.model.profile.MembersIdsResponse;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.request.GetFollowersRequest;
import com.linecorp.bot.model.request.SetWebhookEndpointRequest;
import com.linecorp.bot.model.request.TestWebhookEndpointRequest;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.model.response.BotInfoResponse;
import com.linecorp.bot.model.response.GetAggregationUnitNameListResponse;
import com.linecorp.bot.model.response.GetAggregationUnitUsageResponse;
import com.linecorp.bot.model.response.GetFollowersResponse;
import com.linecorp.bot.model.response.GetNumberOfFollowersResponse;
import com.linecorp.bot.model.response.GetNumberOfMessageDeliveriesResponse;
import com.linecorp.bot.model.response.GetStatisticsPerUnitResponse;
import com.linecorp.bot.model.response.GetStatisticsPerUnitResponse.Click;
import com.linecorp.bot.model.response.GetStatisticsPerUnitResponse.Message;
import com.linecorp.bot.model.response.GetStatisticsPerUnitResponse.Overview;
import com.linecorp.bot.model.response.GetWebhookEndpointResponse;
import com.linecorp.bot.model.response.IssueLinkTokenResponse;
import com.linecorp.bot.model.response.MessageQuotaResponse;
import com.linecorp.bot.model.response.MessageQuotaResponse.QuotaType;
import com.linecorp.bot.model.response.NarrowcastProgressResponse;
import com.linecorp.bot.model.response.NarrowcastProgressResponse.Phase;
import com.linecorp.bot.model.response.NumberOfMessagesResponse;
import com.linecorp.bot.model.response.QuotaConsumptionResponse;
import com.linecorp.bot.model.response.SetWebhookEndpointResponse;
import com.linecorp.bot.model.response.TestWebhookEndpointResponse;
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

import okhttp3.Headers;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@ExtendWith(MockitoExtension.class)
@Timeout(5)
public class LineMessagingClientImplTest {
    private static final String REQUEST_ID_FIXTURE = "REQUEST_ID_FIXTURE";
    private static final BotApiResponseBody BOT_API_SUCCESS_RESPONSE_BODY =
            new BotApiResponseBody("", emptyList());
    private static final BotApiResponse BOT_API_SUCCESS_RESPONSE =
            BOT_API_SUCCESS_RESPONSE_BODY.withRequestId(REQUEST_ID_FIXTURE);
    private static final RichMenuIdResponse RICH_MENU_ID_RESPONSE = new RichMenuIdResponse("ID");

    @Mock
    private LineMessagingService retrofitMock;

    @InjectMocks
    private LineMessagingClientImpl target;

    @Test
    public void replyMessageTest() throws Exception {
        whenCall(retrofitMock.replyMessage(any()),
                 BOT_API_SUCCESS_RESPONSE_BODY);
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
        whenCall(retrofitMock.pushMessage(isNull(), any(PushMessage.class)),
                 BOT_API_SUCCESS_RESPONSE_BODY);
        final PushMessage pushMessage = new PushMessage("TO", new TextMessage("text"));

        // Do
        final BotApiResponse botApiResponse =
                target.pushMessage(pushMessage).get();

        // Verify
        verify(retrofitMock, only()).pushMessage(null, pushMessage);
        assertThat(botApiResponse).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    @Test
    public void multicastTest() throws Exception {
        whenCall(retrofitMock.multicast(isNull(), any(Multicast.class)),
                 BOT_API_SUCCESS_RESPONSE_BODY);
        final Multicast multicast = new Multicast(singleton("TO"), new TextMessage("text"));

        // Do
        final BotApiResponse botApiResponse =
                target.multicast(multicast).get();

        // Verify
        verify(retrofitMock, only()).multicast(null, multicast);
        assertThat(botApiResponse).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    @Test
    public void broadcast() {
        whenCall(retrofitMock.broadcast(isNull(), any(Broadcast.class)),
                 BOT_API_SUCCESS_RESPONSE_BODY);
        final Broadcast broadcast = new Broadcast(singletonList(new TextMessage("text")), true);

        final BotApiResponse botApiResponse = target.broadcast(broadcast).join();
        verify(retrofitMock).broadcast(null, broadcast);
        assertThat(botApiResponse).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    @Test
    public void narrowcast() {
        whenCall(retrofitMock.narrowcast(isNull(), any(Narrowcast.class)),
                 BOT_API_SUCCESS_RESPONSE_BODY);
        final Narrowcast narrowcast = new Narrowcast(
                new TextMessage("text"),
                Filter.builder()
                      .demographic(
                              GenderDemographicFilter.builder()
                                                     .oneOf(singletonList(Gender.FEMALE))
                                                     .build()
                      ).build());

        final BotApiResponse botApiResponse = target.narrowcast(narrowcast).join();
        verify(retrofitMock).narrowcast(null, narrowcast);
        assertThat(botApiResponse).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    @Test
    public void narrowcast2() {
        whenCall(retrofitMock.narrowcast(isNull(), any(Narrowcast.class)),
                 BOT_API_SUCCESS_RESPONSE_BODY);
        final Narrowcast narrowcast = new Narrowcast(
                Collections.singletonList(new TextMessage("text")),
                null,
                Filter.builder()
                      .demographic(
                              GenderDemographicFilter.builder()
                                                     .oneOf(singletonList(Gender.FEMALE))
                                                     .build()
                      ).build(),
                Limit.builder()
                     .upToRemainingQuota(true)
                     .build(),
                false
        );

        final BotApiResponse botApiResponse = target.narrowcast(narrowcast).join();
        verify(retrofitMock).narrowcast(null, narrowcast);
        assertThat(botApiResponse).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    @Test
    public void getNarrowcastProgress() {
        whenCall(retrofitMock.getNarrowcastProgress(any()),
                 NarrowcastProgressResponse.builder()
                                           .phase(Phase.SUCCEEDED)
                                           .successCount(35L)
                                           .targetCount(35L)
                                           .build());

        final NarrowcastProgressResponse response = target
                .getNarrowcastProgress("0e6e2d6a-bca4-4275-8b56-fd4f002f6115")
                .join();
        verify(retrofitMock).getNarrowcastProgress("0e6e2d6a-bca4-4275-8b56-fd4f002f6115");
        assertThat(response).isEqualTo(NarrowcastProgressResponse.builder()
                                                                 .phase(Phase.SUCCEEDED)
                                                                 .successCount(35L)
                                                                 .targetCount(35L)
                                                                 .build());
    }

    @Test
    public void getMessageQuota() {
        whenCall(retrofitMock.getMessageQuota(),
                 MessageQuotaResponse.builder()
                                     .type(QuotaType.none)
                                     .build());

        MessageQuotaResponse response = target.getMessageQuota().join();
        verify(retrofitMock, only()).getMessageQuota();
        assertThat(response.getType()).isEqualTo(QuotaType.none);
    }

    @Test
    public void getMessageQuota_limited() {
        whenCall(retrofitMock.getMessageQuota(),
                 MessageQuotaResponse.builder()
                                     .type(QuotaType.limited)
                                     .value(100)
                                     .build());

        MessageQuotaResponse response = target.getMessageQuota().join();
        verify(retrofitMock, only()).getMessageQuota();
        assertThat(response.getType()).isEqualTo(QuotaType.limited);
        assertThat(response.getValue()).isEqualTo(100);
    }

    @Test
    public void getMessageQuotaConsumption() {
        whenCall(retrofitMock.getMessageQuotaConsumption(),
                 new QuotaConsumptionResponse(1024));

        QuotaConsumptionResponse response = target.getMessageQuotaConsumption().join();
        verify(retrofitMock, only()).getMessageQuotaConsumption();
        assertThat(response.getTotalUsage()).isEqualTo(1024);
    }

    @Test
    public void getNumberOfSentReplyMessages() {
        whenCall(retrofitMock.getNumberOfSentReplyMessages(any()),
                 new NumberOfMessagesResponse(NumberOfMessagesResponse.Status.READY, 1024));

        NumberOfMessagesResponse response = target.getNumberOfSentReplyMessages("20181231").join();
        verify(retrofitMock, only()).getNumberOfSentReplyMessages("20181231");
        assertThat(response.getStatus()).isEqualTo(NumberOfMessagesResponse.Status.READY);
        assertThat(response.getSuccess()).isEqualTo(1024);
    }

    @Test
    public void getNumberOfSentPushMessages() {
        whenCall(retrofitMock.getNumberOfSentPushMessages(any()),
                 new NumberOfMessagesResponse(NumberOfMessagesResponse.Status.READY, 1024));

        NumberOfMessagesResponse response = target.getNumberOfSentPushMessages("20181231").join();
        verify(retrofitMock, only()).getNumberOfSentPushMessages("20181231");
        assertThat(response.getStatus()).isEqualTo(NumberOfMessagesResponse.Status.READY);
        assertThat(response.getSuccess()).isEqualTo(1024);
    }

    @Test
    public void getNumberOfSentMulticastMessages() {
        whenCall(retrofitMock.getNumberOfSentMulticastMessages(any()),
                 new NumberOfMessagesResponse(NumberOfMessagesResponse.Status.READY, 1024));

        NumberOfMessagesResponse response = target.getNumberOfSentMulticastMessages("20181231").join();
        verify(retrofitMock, only()).getNumberOfSentMulticastMessages("20181231");
        assertThat(response.getStatus()).isEqualTo(NumberOfMessagesResponse.Status.READY);
        assertThat(response.getSuccess()).isEqualTo(1024);
    }

    @Test
    public void getNumberOfSentBroadcastMessages() {
        whenCall(retrofitMock.getNumberOfSentBroadcastMessages(any()),
                 new NumberOfMessagesResponse(NumberOfMessagesResponse.Status.READY, 1024));

        NumberOfMessagesResponse response = target.getNumberOfSentBroadcastMessages("20181231").join();
        verify(retrofitMock, only()).getNumberOfSentBroadcastMessages("20181231");
        assertThat(response.getStatus()).isEqualTo(NumberOfMessagesResponse.Status.READY);
        assertThat(response.getSuccess()).isEqualTo(1024);
    }

    @Test
    public void getProfileTest() throws Exception {
        final UserProfileResponse mockUserProfileResponse = UserProfileResponse
                .builder()
                .displayName("name")
                .userId("userId")
                .pictureUrl(URI.create("https://line.me/picture_url"))
                .statusMessage("Status message")
                .language("en")
                .build();
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
        final UserProfileResponse mockUserProfileResponse = UserProfileResponse
                .builder()
                .displayName("name")
                .userId("userId")
                .pictureUrl(URI.create("https://line.me/picture_url"))
                .statusMessage("Status message")
                .language("en")
                .build();
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
                 BOT_API_SUCCESS_RESPONSE_BODY);

        // Do
        final BotApiResponse botApiResponse = target.leaveGroup("ID").get();

        // Verify
        verify(retrofitMock, only()).leaveGroup("ID");
        assertThat(botApiResponse).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    @Test
    public void leaveRoomTest() throws Exception {
        whenCall(retrofitMock.leaveRoom(any()),
                 BOT_API_SUCCESS_RESPONSE_BODY);

        // Do
        final BotApiResponse botApiResponse = target.leaveRoom("ID").get();

        // Verify
        verify(retrofitMock, only()).leaveRoom("ID");
        assertThat(botApiResponse).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    @Test
    public void getGroupSummary() throws Exception {
        GroupSummaryResponse dummyResponse = GroupSummaryResponse
                .builder()
                .build();
        whenCall(retrofitMock.getGroupSummary(any()),
                 dummyResponse);

        // Do
        GroupSummaryResponse got = target.getGroupSummary("GGGGGGGGGGGGGGGGGG")
                                         .get();

        // Verify
        verify(retrofitMock, only()).getGroupSummary("GGGGGGGGGGGGGGGGGG");
        assertThat(got).isEqualTo(dummyResponse);
    }

    @Test
    public void getGroupMemberCount() throws Exception {
        GroupMemberCountResponse dummyResponse = GroupMemberCountResponse
                .builder()
                .count(3L)
                .build();
        whenCall(retrofitMock.getGroupMemberCount(any()),
                 dummyResponse);

        // Do
        GroupMemberCountResponse got = target.getGroupMemberCount("GGGGGGGGGGGGGGGGGG")
                                             .get();

        // Verify
        verify(retrofitMock, only()).getGroupMemberCount("GGGGGGGGGGGGGGGGGG");
        assertThat(got).isEqualTo(dummyResponse);
    }

    @Test
    public void getRoomMemberCount() throws Exception {
        RoomMemberCountResponse dummyResponse = RoomMemberCountResponse
                .builder()
                .count(3L)
                .build();
        whenCall(retrofitMock.getRoomMemberCount(any()),
                 dummyResponse);

        // Do
        RoomMemberCountResponse got = target.getRoomMemberCount("GGGGGGGGGGGGGGGGGG")
                                            .get();

        // Verify
        verify(retrofitMock, only()).getRoomMemberCount("GGGGGGGGGGGGGGGGGG");
        assertThat(got).isEqualTo(dummyResponse);
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
    public void linkRichMenuToUsers() {
        whenCall(retrofitMock.linkRichMenuToUsers(any()), null);

        // Do
        final BotApiResponse botApiResponse = target.linkRichMenuIdToUsers(singletonList("USER_ID"),
                                                                           "RICH_MENU_ID")
                                                    .join();

        // Verify
        verify(retrofitMock, only()).linkRichMenuToUsers(RichMenuBulkLinkRequest.builder()
                                                                                .richMenuId("RICH_MENU_ID")
                                                                                .userId("USER_ID")
                                                                                .build());
        assertThat(botApiResponse).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    @Test
    public void unlinkRichMenuIdFromUser() throws Exception {
        whenCall(retrofitMock.unlinkRichMenuIdFromUser(any()),
                 null);

        // Do
        final BotApiResponse botApiResponse = target.unlinkRichMenuIdFromUser("ID").get();

        // Verify
        verify(retrofitMock, only()).unlinkRichMenuIdFromUser("ID");
        assertThat(botApiResponse).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    @Test
    public void unlinkRichMenuIdFromUsers() throws Exception {
        whenCall(retrofitMock.unlinkRichMenuIdFromUsers(any()),
                 null);

        // Do
        final BotApiResponse botApiResponse = target.unlinkRichMenuIdFromUsers(singletonList("ID"))
                                                    .join();

        // Verify
        verify(retrofitMock, only()).unlinkRichMenuIdFromUsers(
                RichMenuBulkUnlinkRequest.builder().userId("ID").build());
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

    @Test
    public void getNumberOfMessageDeliveries() throws Exception {
        final GetNumberOfMessageDeliveriesResponse response = GetNumberOfMessageDeliveriesResponse
                .builder()
                .status(GetNumberOfMessageDeliveriesResponse.Status.READY)
                .apiBroadcast(1L)
                .build();
        whenCall(retrofitMock.getNumberOfMessageDeliveries(any()), response);
        final GetNumberOfMessageDeliveriesResponse actual =
                target.getNumberOfMessageDeliveries("20190805").get();
        verify(retrofitMock, only()).getNumberOfMessageDeliveries("20190805");
        assertThat(actual).isEqualTo(response);
    }

    @Test
    public void getNumberOfFollowers() throws Exception {
        final GetNumberOfFollowersResponse response = GetNumberOfFollowersResponse
                .builder()
                .status(GetNumberOfFollowersResponse.Status.READY)
                .followers(3L)
                .targetedReaches(2L)
                .blocks(1L)
                .build();
        whenCall(retrofitMock.getNumberOfFollowers(any()), response);
        final GetNumberOfFollowersResponse actual =
                target.getNumberOfFollowers("20190805").get();
        verify(retrofitMock, only()).getNumberOfFollowers("20190805");
        assertThat(actual).isEqualTo(response);
    }

    @Test
    public void getBotInfo() throws Exception {
        final BotInfoResponse response = BotInfoResponse
                .builder()
                .userId("userId")
                .basicId("basicId")
                .premiumId("premiumId")
                .displayName("displayName")
                .pictureUrl(URI.create("https://line.me/picture_url"))
                .chatMode(BotInfoResponse.ChatMode.BOT)
                .markAsReadMode(BotInfoResponse.MarkAsReadMode.AUTO)
                .build();
        whenCall(retrofitMock.getBotInfo(), response);
        final BotInfoResponse actual = target.getBotInfo().get();
        verify(retrofitMock, only()).getBotInfo();
        assertThat(actual).isEqualTo(response);
    }

    @Test
    public void getWebhookEndpoint() throws Exception {
        final GetWebhookEndpointResponse response = GetWebhookEndpointResponse
                .builder()
                .endpoint(URI.create("https://line.me/webhook"))
                .active(true)
                .build();
        whenCall(retrofitMock.getWebhookEndpoint(), response);
        final GetWebhookEndpointResponse actual = target.getWebhookEndpoint().get();
        verify(retrofitMock, only()).getWebhookEndpoint();
        assertThat(actual).isEqualTo(response);
    }

    @Test
    public void setWebhookEndpoint() throws Exception {
        final SetWebhookEndpointResponse response = SetWebhookEndpointResponse
                .builder()
                .build();
        final SetWebhookEndpointRequest request = SetWebhookEndpointRequest
                .builder()
                .endpoint(URI.create("http://example.com/my/great/endpoint"))
                .build();
        whenCall(retrofitMock.setWebhookEndpoint(request), response);
        final SetWebhookEndpointResponse actual = target.setWebhookEndpoint(request).get();
        verify(retrofitMock, only()).setWebhookEndpoint(request);
        assertThat(actual).isEqualTo(response);
    }

    @Test
    public void testWebhookEndpoint() throws Exception {
        final TestWebhookEndpointResponse response = TestWebhookEndpointResponse
                .builder()
                .success(true)
                .timestamp(Instant.now())
                .detail("abc")
                .reason("def")
                .statusCode(200)
                .build();
        final TestWebhookEndpointRequest request = TestWebhookEndpointRequest
                .builder()
                .endpoint(URI.create("http://example.com/my/great/endpoint"))
                .build();
        whenCall(retrofitMock.testWebhookEndpoint(request), response);
        final TestWebhookEndpointResponse actual = target.testWebhookEndpoint(request).get();
        verify(retrofitMock, only()).testWebhookEndpoint(request);
        assertThat(actual).isEqualTo(response);
    }

    @Test
    public void createRichMenuAliasTest() throws Exception {
        final CreateRichMenuAliasRequest request = CreateRichMenuAliasRequest
                .builder()
                .richMenuAliasId("richmenu-alias-id")
                .richMenuId("RICHMENU_ID")
                .build();
        whenCall(retrofitMock.createRichMenuAlias(request), BOT_API_SUCCESS_RESPONSE_BODY);
        final BotApiResponse actual = target.createRichMenuAlias(request).get();
        verify(retrofitMock, only()).createRichMenuAlias(request);
        assertThat(actual).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    @Test
    public void updateRichMenuAliasTest() throws Exception {
        final UpdateRichMenuAliasRequest request = UpdateRichMenuAliasRequest
                .builder()
                .richMenuId("RICHMENU_ID")
                .build();
        final String richMenuAliasId = "richmenu-alias-id";
        whenCall(retrofitMock.updateRichMenuAlias(richMenuAliasId, request), BOT_API_SUCCESS_RESPONSE_BODY);
        final BotApiResponse actual = target.updateRichMenuAlias(richMenuAliasId, request).get();
        verify(retrofitMock, only()).updateRichMenuAlias(richMenuAliasId, request);
        assertThat(actual).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    @Test
    public void getRichMenuAliasTest() throws Exception {
        final String richMenuAliasId = "richmenu-alias-id";
        final RichMenuAliasResponse response = RichMenuAliasResponse
                .builder()
                .richMenuAliasId(richMenuAliasId)
                .richMenuId("RICHMENU_ID")
                .build();
        whenCall(retrofitMock.getRichMenuAlias(richMenuAliasId), response);
        final RichMenuAliasResponse actual = target.getRichMenuAlias(richMenuAliasId).get();
        verify(retrofitMock, only()).getRichMenuAlias(richMenuAliasId);
        assertThat(actual).isEqualTo(response);
    }

    @Test
    public void getRichMenuAliasListTest() throws Exception {
        final RichMenuAliasListResponse response = RichMenuAliasListResponse
                .builder()
                .alias(RichMenuAliasResponse
                               .builder()
                               .richMenuAliasId("richmenu-alias-id-1")
                               .richMenuId("RICHMENU_ID_1")
                               .build())
                .alias(RichMenuAliasResponse
                               .builder()
                               .richMenuAliasId("richmenu-alias-id-2")
                               .richMenuId("RICHMENU_ID_2")
                               .build())
                .build();
        whenCall(retrofitMock.getRichMenuAliasList(), response);
        final RichMenuAliasListResponse actual = target.getRichMenuAliasList().get();
        verify(retrofitMock, only()).getRichMenuAliasList();
        assertThat(actual).isEqualTo(response);
    }

    @Test
    public void deleteRichMenuAliasTest() throws Exception {
        final String richMenuAliasId = "richmenu-alias-id";
        whenCall(retrofitMock.deleteRichMenuAlias(richMenuAliasId), BOT_API_SUCCESS_RESPONSE_BODY);
        final BotApiResponse actual = target.deleteRichMenuAlias(richMenuAliasId).get();
        verify(retrofitMock, only()).deleteRichMenuAlias(richMenuAliasId);
        assertThat(actual).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    @Test
    public void getStatisticsPerUnitTest() throws Exception {
        final String customAggregationUnit = "promotion_a";
        final String from = "20210301";
        final String to = "20210331";

        final Overview overview = Overview.builder()
                                          .uniqueImpression(4000L)
                                          .uniqueClick(3000L)
                                          .uniqueMediaPlayed(2800L)
                                          .uniqueMediaPlayed100Percent(2500L)
                                          .build();

        final Message message = Message.builder()
                                       .seq(1L)
                                       .impression(4000L)
                                       .mediaPlayed(2800L)
                                       .uniqueMediaPlayed(2701L)
                                       .build();

        final Click click1 = Click.builder()
                                  .seq(1L)
                                  .url("https://example.com/1st")
                                  .click(2500L)
                                  .uniqueClick(2500L)
                                  .uniqueClickOfRequest(2500L)
                                  .build();

        final GetStatisticsPerUnitResponse response =
                GetStatisticsPerUnitResponse.builder()
                                            .messages(singletonList(message))
                                            .overview(overview)
                                            .clicks(singletonList(click1))
                                            .build();

        whenCall(retrofitMock.getStatisticsPerUnit(customAggregationUnit, from, to), response);
        final GetStatisticsPerUnitResponse actual =
                target.getStatisticsPerUnit(customAggregationUnit, from, to).get();
        verify(retrofitMock, only()).getStatisticsPerUnit(customAggregationUnit, from, to);
        assertThat(actual).isEqualTo(response);
    }

    @Test
    public void getAggregationUnitUsageTest() throws Exception {
        final GetAggregationUnitUsageResponse response =
                GetAggregationUnitUsageResponse.builder().numOfCustomAggregationUnits(100L).build();
        whenCall(retrofitMock.getAggregationUnitUsage(), response);
        final GetAggregationUnitUsageResponse actual = target.getAggregationUnitUsage().get();
        verify(retrofitMock, only()).getAggregationUnitUsage();
        assertThat(actual).isEqualTo(response);
    }

    @Test
    public void getAggregationUnitNameListTest() throws Exception {
        final String limit = "10";
        final String start = "start";
        final GetAggregationUnitNameListResponse response =
                GetAggregationUnitNameListResponse.builder()
                                                  .customAggregationUnits(singletonList("promotion_a"))
                                                  .next("next")
                                                  .build();
        whenCall(retrofitMock.getAggregationUnitNameList(limit, start), response);
        final GetAggregationUnitNameListResponse actual = target.getAggregationUnitNameList(limit, start).get();
        verify(retrofitMock, only()).getAggregationUnitNameList(limit, start);
        assertThat(actual).isEqualTo(response);
    }

    @Test
    public void getFollowers() throws Exception {
        final GetFollowersResponse response = GetFollowersResponse
                .builder()
                .userIds(ImmutableList.of("U1234"))
                .build();
        whenCall(retrofitMock.getFollowers(nullable(String.class), nullable(Integer.class)), response);
        final GetFollowersResponse actual = target.getFollowers(GetFollowersRequest.builder().build()).get();
        verify(retrofitMock, only()).getFollowers(null, null);
        assertThat(actual).isEqualTo(response);
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
                final Headers headers = Headers.of(singletonMap("x-line-request-id", REQUEST_ID_FIXTURE));
                callback.onResponse(this, Response.success(value, headers));
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

            @Override
            public okio.Timeout timeout() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
