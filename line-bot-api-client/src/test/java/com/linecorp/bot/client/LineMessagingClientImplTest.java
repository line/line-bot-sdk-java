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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.OngoingStubbing;

import com.linecorp.bot.model.Broadcast;
import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.Narrowcast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.manageaudience.AudienceGroup;
import com.linecorp.bot.model.manageaudience.AudienceGroupAuthorityLevel;
import com.linecorp.bot.model.manageaudience.AudienceGroupCreateRoute;
import com.linecorp.bot.model.manageaudience.AudienceGroupPermission;
import com.linecorp.bot.model.manageaudience.AudienceGroupStatus;
import com.linecorp.bot.model.manageaudience.AudienceGroupType;
import com.linecorp.bot.model.manageaudience.request.Audience;
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
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.narrowcast.Filter;
import com.linecorp.bot.model.narrowcast.filter.GenderDemographicFilter;
import com.linecorp.bot.model.narrowcast.filter.GenderDemographicFilter.Gender;
import com.linecorp.bot.model.profile.MembersIdsResponse;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.model.response.GetNumberOfFollowersResponse;
import com.linecorp.bot.model.response.GetNumberOfMessageDeliveriesResponse;
import com.linecorp.bot.model.response.IssueLinkTokenResponse;
import com.linecorp.bot.model.response.MessageQuotaResponse;
import com.linecorp.bot.model.response.MessageQuotaResponse.QuotaType;
import com.linecorp.bot.model.response.NarrowcastProgressResponse;
import com.linecorp.bot.model.response.NarrowcastProgressResponse.Phase;
import com.linecorp.bot.model.response.NumberOfMessagesResponse;
import com.linecorp.bot.model.response.QuotaConsumptionResponse;
import com.linecorp.bot.model.richmenu.RichMenu;
import com.linecorp.bot.model.richmenu.RichMenuBulkLinkRequest;
import com.linecorp.bot.model.richmenu.RichMenuBulkUnlinkRequest;
import com.linecorp.bot.model.richmenu.RichMenuIdResponse;
import com.linecorp.bot.model.richmenu.RichMenuListResponse;
import com.linecorp.bot.model.richmenu.RichMenuResponse;

import okhttp3.Headers;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LineMessagingClientImplTest {
    private static final String REQUEST_ID_FIXTURE = "REQUEST_ID_FIXTURE";
    private static final BotApiResponseBody BOT_API_SUCCESS_RESPONSE_BODY =
            new BotApiResponseBody("", emptyList());
    private static final BotApiResponse BOT_API_SUCCESS_RESPONSE =
            BOT_API_SUCCESS_RESPONSE_BODY.withRequestId(REQUEST_ID_FIXTURE);
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
        whenCall(retrofitMock.pushMessage(any()),
                 BOT_API_SUCCESS_RESPONSE_BODY);
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
                 BOT_API_SUCCESS_RESPONSE_BODY);
        final Multicast multicast = new Multicast(singleton("TO"), new TextMessage("text"));

        // Do
        final BotApiResponse botApiResponse =
                target.multicast(multicast).get();

        // Verify
        verify(retrofitMock, only()).multicast(multicast);
        assertThat(botApiResponse).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    @Test
    public void broadcast() {
        whenCall(retrofitMock.broadcast(any()), BOT_API_SUCCESS_RESPONSE_BODY);
        final Broadcast broadcast = new Broadcast(singletonList(new TextMessage("text")), true);

        final BotApiResponse botApiResponse = target.broadcast(broadcast).join();
        verify(retrofitMock).broadcast(broadcast);
        assertThat(botApiResponse).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    @Test
    public void narrowcast() {
        whenCall(retrofitMock.narrowcast(any()), BOT_API_SUCCESS_RESPONSE_BODY);
        final Narrowcast narrowcast = new Narrowcast(
                new TextMessage("text"),
                Filter.builder()
                      .demographic(
                              GenderDemographicFilter.builder()
                                                     .oneOf(singletonList(Gender.FEMALE))
                                                     .build()
                      ).build());

        final BotApiResponse botApiResponse = target.narrowcast(narrowcast).join();
        verify(retrofitMock).narrowcast(narrowcast);
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
        final UserProfileResponse mockUserProfileResponse =
                new UserProfileResponse("displayName", "userId",
                                        URI.create("http://pictureUrl/"), "statusMessage");
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
                new UserProfileResponse("displayName", "userId",
                                        URI.create("http://pictureUrl"), null);
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
    public void getAudienceGroups() throws Exception {

        final GetAudienceGroupsResponse response = GetAudienceGroupsResponse
                .builder()
                .audienceGroups(singletonList(AudienceGroup.builder()
                                                           .audienceGroupId(3L)
                                                           .type(AudienceGroupType.IMP)
                                                           .description("hello")
                                                           .status(AudienceGroupStatus.EXPIRED)
                                                           .failedType(null)
                                                           .audienceCount(3L)
                                                           .created(1583540070193L)
                                                           .requestId("6744d281-c0bd-4a63-91d5-cab659827828")
                                                           .clickUrl(null)
                                                           .isIfaAudience(false)
                                                           .permission(AudienceGroupPermission.READ_WRITE)
                                                           .createRoute(AudienceGroupCreateRoute.OA_MANAGER)
                                                           .build()))
                .hasNextPage(true)
                .totalCount(4L)
                .size(1L)
                .page(1L)
                .readWriteAudienceGroupTotalCount(1L)
                .build();
        whenCall(retrofitMock.getAudienceGroups(anyLong(), any(), any(), any(), any(),
                                                any()), response);
        final GetAudienceGroupsResponse actual =
                target.getAudienceGroups(1L, null, null, 40L, null, null).get();
        verify(retrofitMock, only()).getAudienceGroups(1L, null, null, 40L,
                                                       null, null);
        assertThat(actual).isEqualTo(response);
    }

    @Test
    public void createAudienceGroup() throws Exception {
        final CreateAudienceGroupResponse response =
                CreateAudienceGroupResponse.builder()
                                           .build();
        CreateAudienceGroupRequest request =
                CreateAudienceGroupRequest.builder()
                                          .description("test")
                                          .isIfaAudience(false)
                                          .uploadDescription("test")
                                          .audiences(singletonList(new Audience("Uabcdef")))
                                          .build();

        whenCall(retrofitMock.createAudienceGroup(any()), response);
        final CreateAudienceGroupResponse actual =
                target.createAudienceGroup(request).get();
        verify(retrofitMock, only()).createAudienceGroup(request);
        assertThat(actual).isEqualTo(response);
    }

    @Test
    public void createClickBasedAudienceGroup() throws Exception {
        CreateClickBasedAudienceGroupRequest request = CreateClickBasedAudienceGroupRequest.builder()
                                                                                           .build();
        CreateClickBasedAudienceGroupResponse response = CreateClickBasedAudienceGroupResponse.builder()
                                                                                              .build();

        whenCall(retrofitMock.createClickBasedAudienceGroup(any()), response);
        final CreateClickBasedAudienceGroupResponse actual =
                target.createClickBasedAudienceGroup(request).get();
        verify(retrofitMock, only()).createClickBasedAudienceGroup(request);
        assertThat(actual).isEqualTo(response);
    }

    @Test
    public void createImpBasedAudienceGroup() throws Exception {
        CreateImpBasedAudienceGroupRequest request = CreateImpBasedAudienceGroupRequest.builder()
                                                                                       .build();
        CreateImpBasedAudienceGroupResponse response = CreateImpBasedAudienceGroupResponse.builder()
                                                                                          .build();

        whenCall(retrofitMock.createImpBasedAudienceGroup(any()), response);
        final CreateImpBasedAudienceGroupResponse actual =
                target.createImpBasedAudienceGroup(request).get();
        verify(retrofitMock, only()).createImpBasedAudienceGroup(request);
        assertThat(actual).isEqualTo(response);
    }

    @Test
    public void updateAudienceGroupDescription() throws Exception {
        UpdateAudienceGroupDescriptionRequest request = UpdateAudienceGroupDescriptionRequest.builder()
                                                                                             .description(
                                                                                                     "Hello")
                                                                                             .build();
        whenCall(retrofitMock.updateAudienceGroupDescription(anyLong(), any()), null);
        final BotApiResponse actual =
                target.updateAudienceGroupDescription(5693L,
                                                      request
                ).get();
        verify(retrofitMock, only()).updateAudienceGroupDescription(5693L, request);
        assertThat(actual).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    @Test
    public void deleteAudienceGroup() throws Exception {
        whenCall(retrofitMock.deleteAudienceGroup(anyLong()), null);
        final BotApiResponse actual =
                target.deleteAudienceGroup(5693L).get();
        verify(retrofitMock, only()).deleteAudienceGroup(5693L);
        assertThat(actual).isEqualTo(BOT_API_SUCCESS_RESPONSE);
    }

    @Test
    public void getAudienceData() throws Exception {
        final GetAudienceDataResponse response = GetAudienceDataResponse
                .builder()
                .build();
        whenCall(retrofitMock.getAudienceData(anyLong()), response);
        final GetAudienceDataResponse actual =
                target.getAudienceData(4649L).get();
        verify(retrofitMock, only()).getAudienceData(4649L);
        assertThat(actual).isEqualTo(response);
    }

    @Test
    public void getAudienceGroupAuthorityLevel() throws Exception {
        final GetAudienceGroupAuthorityLevelResponse response = GetAudienceGroupAuthorityLevelResponse
                .builder()
                .authorityLevel(AudienceGroupAuthorityLevel.PRIVATE)
                .build();
        whenCall(retrofitMock.getAudienceGroupAuthorityLevel(), response);
        final GetAudienceGroupAuthorityLevelResponse actual =
                target.getAudienceGroupAuthorityLevel().get();
        verify(retrofitMock, only()).getAudienceGroupAuthorityLevel();
        assertThat(actual).isEqualTo(response);
    }

    @Test
    public void updateAudienceGroupAuthorityLevel() throws Exception {
        final UpdateAudienceGroupAuthorityLevelRequest request = UpdateAudienceGroupAuthorityLevelRequest
                .builder()
                .authorityLevel(AudienceGroupAuthorityLevel.PRIVATE)
                .build();
        whenCall(retrofitMock.updateAudienceGroupAuthorityLevel(request), null);
        final BotApiResponse actual =
                target.updateAudienceGroupAuthorityLevel(request).get();
        verify(retrofitMock, only()).updateAudienceGroupAuthorityLevel(request);
        assertThat(actual).isEqualTo(BOT_API_SUCCESS_RESPONSE);
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
        };
    }
}
