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


package com.linecorp.bot.messaging.client;

import static org.assertj.core.api.Assertions.assertThat;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import com.linecorp.bot.client.base.BlobContent;
import com.linecorp.bot.client.base.UploadFile;

import java.net.URI;

import java.util.Map;

import com.linecorp.bot.messaging.model.TextMessage;

import com.linecorp.bot.messaging.model.AudienceMatchMessagesRequest;
import com.linecorp.bot.messaging.model.BotInfoResponse;
import com.linecorp.bot.messaging.model.BroadcastRequest;
import com.linecorp.bot.messaging.model.CreateRichMenuAliasRequest;
import com.linecorp.bot.messaging.model.ErrorResponse;
import com.linecorp.bot.messaging.model.GetAggregationUnitNameListResponse;
import com.linecorp.bot.messaging.model.GetAggregationUnitUsageResponse;
import com.linecorp.bot.messaging.model.GetFollowersResponse;
import com.linecorp.bot.messaging.model.GetWebhookEndpointResponse;
import com.linecorp.bot.messaging.model.GroupMemberCountResponse;
import com.linecorp.bot.messaging.model.GroupSummaryResponse;
import com.linecorp.bot.messaging.model.GroupUserProfileResponse;
import com.linecorp.bot.messaging.model.IssueLinkTokenResponse;
import com.linecorp.bot.messaging.model.MarkMessagesAsReadRequest;
import com.linecorp.bot.messaging.model.MembersIdsResponse;
import com.linecorp.bot.messaging.model.MessageQuotaResponse;
import com.linecorp.bot.messaging.model.MulticastRequest;
import com.linecorp.bot.messaging.model.NarrowcastProgressResponse;
import com.linecorp.bot.messaging.model.NarrowcastRequest;
import com.linecorp.bot.messaging.model.NumberOfMessagesResponse;
import com.linecorp.bot.messaging.model.PnpMessagesRequest;
import com.linecorp.bot.messaging.model.PushMessageRequest;
import com.linecorp.bot.messaging.model.PushMessageResponse;
import com.linecorp.bot.messaging.model.QuotaConsumptionResponse;
import com.linecorp.bot.messaging.model.ReplyMessageRequest;
import com.linecorp.bot.messaging.model.ReplyMessageResponse;
import com.linecorp.bot.messaging.model.RichMenuAliasListResponse;
import com.linecorp.bot.messaging.model.RichMenuAliasResponse;
import com.linecorp.bot.messaging.model.RichMenuBatchProgressResponse;
import com.linecorp.bot.messaging.model.RichMenuBatchRequest;
import com.linecorp.bot.messaging.model.RichMenuBulkLinkRequest;
import com.linecorp.bot.messaging.model.RichMenuBulkUnlinkRequest;
import com.linecorp.bot.messaging.model.RichMenuIdResponse;
import com.linecorp.bot.messaging.model.RichMenuListResponse;
import com.linecorp.bot.messaging.model.RichMenuRequest;
import com.linecorp.bot.messaging.model.RichMenuResponse;
import com.linecorp.bot.messaging.model.RoomMemberCountResponse;
import com.linecorp.bot.messaging.model.RoomUserProfileResponse;
import com.linecorp.bot.messaging.model.SetWebhookEndpointRequest;
import com.linecorp.bot.messaging.model.TestWebhookEndpointRequest;
import com.linecorp.bot.messaging.model.TestWebhookEndpointResponse;
import java.util.UUID;
import com.linecorp.bot.messaging.model.UpdateRichMenuAliasRequest;
import com.linecorp.bot.messaging.model.UserProfileResponse;
import com.linecorp.bot.messaging.model.ValidateMessageRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.ocadotechnology.gembus.test.Arranger;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.github.tomakehurst.wiremock.WireMockServer;

/**
* API tests for MessagingApiClient
*/
@Timeout(5)
public class MessagingApiClientTest {
    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private WireMockServer wireMockServer;
    private MessagingApiClient api;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());


        api = MessagingApiClient.builder("MY_OWN_TOKEN")
            .apiEndPoint(URI.create(wireMockServer.baseUrl()))
            .build();
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void audienceMatchTest() {
        stubFor(post(urlPathTemplate("/bot/ad/multicast/phone")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            AudienceMatchMessagesRequest audienceMatchMessagesRequest = Arranger.some(AudienceMatchMessagesRequest.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        api.audienceMatch(audienceMatchMessagesRequest).join().body();

        // TODO: test validations
    }

    @Test
    public void broadcastTest() {
        stubFor(post(urlPathTemplate("/v2/bot/message/broadcast")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            UUID xLineRetryKey = Arranger.some(UUID.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));
            BroadcastRequest broadcastRequest = Arranger.some(BroadcastRequest.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        Object response = api.broadcast(xLineRetryKey, broadcastRequest).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void cancelDefaultRichMenuTest() {
        stubFor(delete(urlPathTemplate("/v2/bot/user/all/richmenu")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));


        api.cancelDefaultRichMenu().join().body();

        // TODO: test validations
    }

    @Test
    public void createRichMenuTest() {
        stubFor(post(urlPathTemplate("/v2/bot/richmenu")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            RichMenuRequest richMenuRequest = Arranger.some(RichMenuRequest.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        RichMenuIdResponse response = api.createRichMenu(richMenuRequest).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void createRichMenuAliasTest() {
        stubFor(post(urlPathTemplate("/v2/bot/richmenu/alias")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            CreateRichMenuAliasRequest createRichMenuAliasRequest = Arranger.some(CreateRichMenuAliasRequest.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        api.createRichMenuAlias(createRichMenuAliasRequest).join().body();

        // TODO: test validations
    }

    @Test
    public void deleteRichMenuTest() {
        stubFor(delete(urlPathTemplate("/v2/bot/richmenu/{richMenuId}")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String richMenuId = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        api.deleteRichMenu(richMenuId).join().body();

        // TODO: test validations
    }

    @Test
    public void deleteRichMenuAliasTest() {
        stubFor(delete(urlPathTemplate("/v2/bot/richmenu/alias/{richMenuAliasId}")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String richMenuAliasId = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        api.deleteRichMenuAlias(richMenuAliasId).join().body();

        // TODO: test validations
    }

    @Test
    public void getAdPhoneMessageStatisticsTest() {
        stubFor(get(urlPathTemplate("/v2/bot/message/delivery/ad_phone")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String date = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        NumberOfMessagesResponse response = api.getAdPhoneMessageStatistics(date).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getAggregationUnitNameListTest() {
        stubFor(get(urlPathTemplate("/v2/bot/message/aggregation/list")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String limit = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));
            String start = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        GetAggregationUnitNameListResponse response = api.getAggregationUnitNameList(limit, start).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getAggregationUnitUsageTest() {
        stubFor(get(urlPathTemplate("/v2/bot/message/aggregation/info")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));


        GetAggregationUnitUsageResponse response = api.getAggregationUnitUsage().join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getBotInfoTest() {
        stubFor(get(urlPathTemplate("/v2/bot/info")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));


        BotInfoResponse response = api.getBotInfo().join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getDefaultRichMenuIdTest() {
        stubFor(get(urlPathTemplate("/v2/bot/user/all/richmenu")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));


        RichMenuIdResponse response = api.getDefaultRichMenuId().join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getFollowersTest() {
        stubFor(get(urlPathTemplate("/v2/bot/followers/ids")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String start = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));
            Integer limit = Arranger.some(Integer.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        GetFollowersResponse response = api.getFollowers(start, limit).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getGroupMemberCountTest() {
        stubFor(get(urlPathTemplate("/v2/bot/group/{groupId}/members/count")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String groupId = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        GroupMemberCountResponse response = api.getGroupMemberCount(groupId).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getGroupMemberProfileTest() {
        stubFor(get(urlPathTemplate("/v2/bot/group/{groupId}/member/{userId}")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String groupId = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));
            String userId = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        GroupUserProfileResponse response = api.getGroupMemberProfile(groupId, userId).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getGroupMembersIdsTest() {
        stubFor(get(urlPathTemplate("/v2/bot/group/{groupId}/members/ids")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String groupId = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));
            String start = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        MembersIdsResponse response = api.getGroupMembersIds(groupId, start).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getGroupSummaryTest() {
        stubFor(get(urlPathTemplate("/v2/bot/group/{groupId}/summary")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String groupId = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        GroupSummaryResponse response = api.getGroupSummary(groupId).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getMessageQuotaTest() {
        stubFor(get(urlPathTemplate("/v2/bot/message/quota")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));


        MessageQuotaResponse response = api.getMessageQuota().join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getMessageQuotaConsumptionTest() {
        stubFor(get(urlPathTemplate("/v2/bot/message/quota/consumption")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));


        QuotaConsumptionResponse response = api.getMessageQuotaConsumption().join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getNarrowcastProgressTest() {
        stubFor(get(urlPathTemplate("/v2/bot/message/progress/narrowcast")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String requestId = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        NarrowcastProgressResponse response = api.getNarrowcastProgress(requestId).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getNumberOfSentBroadcastMessagesTest() {
        stubFor(get(urlPathTemplate("/v2/bot/message/delivery/broadcast")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String date = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        NumberOfMessagesResponse response = api.getNumberOfSentBroadcastMessages(date).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getNumberOfSentMulticastMessagesTest() {
        stubFor(get(urlPathTemplate("/v2/bot/message/delivery/multicast")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String date = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        NumberOfMessagesResponse response = api.getNumberOfSentMulticastMessages(date).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getNumberOfSentPushMessagesTest() {
        stubFor(get(urlPathTemplate("/v2/bot/message/delivery/push")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String date = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        NumberOfMessagesResponse response = api.getNumberOfSentPushMessages(date).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getNumberOfSentReplyMessagesTest() {
        stubFor(get(urlPathTemplate("/v2/bot/message/delivery/reply")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String date = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        NumberOfMessagesResponse response = api.getNumberOfSentReplyMessages(date).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getPNPMessageStatisticsTest() {
        stubFor(get(urlPathTemplate("/v2/bot/message/delivery/pnp")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String date = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        NumberOfMessagesResponse response = api.getPNPMessageStatistics(date).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getProfileTest() {
        stubFor(get(urlPathTemplate("/v2/bot/profile/{userId}")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String userId = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        UserProfileResponse response = api.getProfile(userId).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getRichMenuTest() {
        stubFor(get(urlPathTemplate("/v2/bot/richmenu/{richMenuId}")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String richMenuId = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        RichMenuResponse response = api.getRichMenu(richMenuId).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getRichMenuAliasTest() {
        stubFor(get(urlPathTemplate("/v2/bot/richmenu/alias/{richMenuAliasId}")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String richMenuAliasId = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        RichMenuAliasResponse response = api.getRichMenuAlias(richMenuAliasId).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getRichMenuAliasListTest() {
        stubFor(get(urlPathTemplate("/v2/bot/richmenu/alias/list")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));


        RichMenuAliasListResponse response = api.getRichMenuAliasList().join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getRichMenuBatchProgressTest() {
        stubFor(get(urlPathTemplate("/v2/bot/richmenu/progress/batch")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String requestId = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        RichMenuBatchProgressResponse response = api.getRichMenuBatchProgress(requestId).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getRichMenuIdOfUserTest() {
        stubFor(get(urlPathTemplate("/v2/bot/user/{userId}/richmenu")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String userId = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        RichMenuIdResponse response = api.getRichMenuIdOfUser(userId).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getRichMenuListTest() {
        stubFor(get(urlPathTemplate("/v2/bot/richmenu/list")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));


        RichMenuListResponse response = api.getRichMenuList().join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getRoomMemberCountTest() {
        stubFor(get(urlPathTemplate("/v2/bot/room/{roomId}/members/count")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String roomId = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        RoomMemberCountResponse response = api.getRoomMemberCount(roomId).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getRoomMemberProfileTest() {
        stubFor(get(urlPathTemplate("/v2/bot/room/{roomId}/member/{userId}")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String roomId = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));
            String userId = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        RoomUserProfileResponse response = api.getRoomMemberProfile(roomId, userId).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getRoomMembersIdsTest() {
        stubFor(get(urlPathTemplate("/v2/bot/room/{roomId}/members/ids")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String roomId = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));
            String start = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        MembersIdsResponse response = api.getRoomMembersIds(roomId, start).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getWebhookEndpointTest() {
        stubFor(get(urlPathTemplate("/v2/bot/channel/webhook/endpoint")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));


        GetWebhookEndpointResponse response = api.getWebhookEndpoint().join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void issueLinkTokenTest() {
        stubFor(post(urlPathTemplate("/v2/bot/user/{userId}/linkToken")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String userId = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        IssueLinkTokenResponse response = api.issueLinkToken(userId).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void leaveGroupTest() {
        stubFor(post(urlPathTemplate("/v2/bot/group/{groupId}/leave")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String groupId = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        api.leaveGroup(groupId).join().body();

        // TODO: test validations
    }

    @Test
    public void leaveRoomTest() {
        stubFor(post(urlPathTemplate("/v2/bot/room/{roomId}/leave")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String roomId = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        api.leaveRoom(roomId).join().body();

        // TODO: test validations
    }

    @Test
    public void linkRichMenuIdToUserTest() {
        stubFor(post(urlPathTemplate("/v2/bot/user/{userId}/richmenu/{richMenuId}")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String userId = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));
            String richMenuId = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        api.linkRichMenuIdToUser(userId, richMenuId).join().body();

        // TODO: test validations
    }

    @Test
    public void linkRichMenuIdToUsersTest() {
        stubFor(post(urlPathTemplate("/v2/bot/richmenu/bulk/link")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            RichMenuBulkLinkRequest richMenuBulkLinkRequest = Arranger.some(RichMenuBulkLinkRequest.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        api.linkRichMenuIdToUsers(richMenuBulkLinkRequest).join().body();

        // TODO: test validations
    }

    @Test
    public void markMessagesAsReadTest() {
        stubFor(post(urlPathTemplate("/v2/bot/message/markAsRead")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            MarkMessagesAsReadRequest markMessagesAsReadRequest = Arranger.some(MarkMessagesAsReadRequest.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        api.markMessagesAsRead(markMessagesAsReadRequest).join().body();

        // TODO: test validations
    }

    @Test
    public void multicastTest() {
        stubFor(post(urlPathTemplate("/v2/bot/message/multicast")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            UUID xLineRetryKey = Arranger.some(UUID.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));
            MulticastRequest multicastRequest = Arranger.some(MulticastRequest.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        Object response = api.multicast(xLineRetryKey, multicastRequest).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void narrowcastTest() {
        stubFor(post(urlPathTemplate("/v2/bot/message/narrowcast")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            UUID xLineRetryKey = Arranger.some(UUID.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));
            NarrowcastRequest narrowcastRequest = Arranger.some(NarrowcastRequest.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        Object response = api.narrowcast(xLineRetryKey, narrowcastRequest).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void pushMessageTest() {
        stubFor(post(urlPathTemplate("/v2/bot/message/push")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            UUID xLineRetryKey = Arranger.some(UUID.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));
            PushMessageRequest pushMessageRequest = Arranger.some(PushMessageRequest.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        PushMessageResponse response = api.pushMessage(xLineRetryKey, pushMessageRequest).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void pushMessagesByPhoneTest() {
        stubFor(post(urlPathTemplate("/bot/pnp/push")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String xLineDeliveryTag = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));
            PnpMessagesRequest pnpMessagesRequest = Arranger.some(PnpMessagesRequest.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        api.pushMessagesByPhone(xLineDeliveryTag, pnpMessagesRequest).join().body();

        // TODO: test validations
    }

    @Test
    public void replyMessageTest() {
        stubFor(post(urlPathTemplate("/v2/bot/message/reply")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            ReplyMessageRequest replyMessageRequest = Arranger.some(ReplyMessageRequest.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        ReplyMessageResponse response = api.replyMessage(replyMessageRequest).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void richMenuBatchTest() {
        stubFor(post(urlPathTemplate("/v2/bot/richmenu/batch")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            RichMenuBatchRequest richMenuBatchRequest = Arranger.some(RichMenuBatchRequest.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        api.richMenuBatch(richMenuBatchRequest).join().body();

        // TODO: test validations
    }

    @Test
    public void setDefaultRichMenuTest() {
        stubFor(post(urlPathTemplate("/v2/bot/user/all/richmenu/{richMenuId}")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String richMenuId = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        api.setDefaultRichMenu(richMenuId).join().body();

        // TODO: test validations
    }

    @Test
    public void setWebhookEndpointTest() {
        stubFor(put(urlPathTemplate("/v2/bot/channel/webhook/endpoint")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            SetWebhookEndpointRequest setWebhookEndpointRequest = Arranger.some(SetWebhookEndpointRequest.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        api.setWebhookEndpoint(setWebhookEndpointRequest).join().body();

        // TODO: test validations
    }

    @Test
    public void testWebhookEndpointTest() {
        stubFor(post(urlPathTemplate("/v2/bot/channel/webhook/test")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            TestWebhookEndpointRequest testWebhookEndpointRequest = Arranger.some(TestWebhookEndpointRequest.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        TestWebhookEndpointResponse response = api.testWebhookEndpoint(testWebhookEndpointRequest).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void unlinkRichMenuIdFromUserTest() {
        stubFor(delete(urlPathTemplate("/v2/bot/user/{userId}/richmenu")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String userId = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        api.unlinkRichMenuIdFromUser(userId).join().body();

        // TODO: test validations
    }

    @Test
    public void unlinkRichMenuIdFromUsersTest() {
        stubFor(post(urlPathTemplate("/v2/bot/richmenu/bulk/unlink")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            RichMenuBulkUnlinkRequest richMenuBulkUnlinkRequest = Arranger.some(RichMenuBulkUnlinkRequest.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        api.unlinkRichMenuIdFromUsers(richMenuBulkUnlinkRequest).join().body();

        // TODO: test validations
    }

    @Test
    public void updateRichMenuAliasTest() {
        stubFor(post(urlPathTemplate("/v2/bot/richmenu/alias/{richMenuAliasId}")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            String richMenuAliasId = Arranger.some(String.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));
            UpdateRichMenuAliasRequest updateRichMenuAliasRequest = Arranger.some(UpdateRichMenuAliasRequest.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        api.updateRichMenuAlias(richMenuAliasId, updateRichMenuAliasRequest).join().body();

        // TODO: test validations
    }

    @Test
    public void validateBroadcastTest() {
        stubFor(post(urlPathTemplate("/v2/bot/message/validate/broadcast")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            ValidateMessageRequest validateMessageRequest = Arranger.some(ValidateMessageRequest.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        api.validateBroadcast(validateMessageRequest).join().body();

        // TODO: test validations
    }

    @Test
    public void validateMulticastTest() {
        stubFor(post(urlPathTemplate("/v2/bot/message/validate/multicast")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            ValidateMessageRequest validateMessageRequest = Arranger.some(ValidateMessageRequest.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        api.validateMulticast(validateMessageRequest).join().body();

        // TODO: test validations
    }

    @Test
    public void validateNarrowcastTest() {
        stubFor(post(urlPathTemplate("/v2/bot/message/validate/narrowcast")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            ValidateMessageRequest validateMessageRequest = Arranger.some(ValidateMessageRequest.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        api.validateNarrowcast(validateMessageRequest).join().body();

        // TODO: test validations
    }

    @Test
    public void validatePushTest() {
        stubFor(post(urlPathTemplate("/v2/bot/message/validate/push")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            ValidateMessageRequest validateMessageRequest = Arranger.some(ValidateMessageRequest.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        api.validatePush(validateMessageRequest).join().body();

        // TODO: test validations
    }

    @Test
    public void validateReplyTest() {
        stubFor(post(urlPathTemplate("/v2/bot/message/validate/reply")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            ValidateMessageRequest validateMessageRequest = Arranger.some(ValidateMessageRequest.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        api.validateReply(validateMessageRequest).join().body();

        // TODO: test validations
    }

    @Test
    public void validateRichMenuBatchRequestTest() {
        stubFor(post(urlPathTemplate("/v2/bot/richmenu/validate/batch")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            RichMenuBatchRequest richMenuBatchRequest = Arranger.some(RichMenuBatchRequest.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        api.validateRichMenuBatchRequest(richMenuBatchRequest).join().body();

        // TODO: test validations
    }

    @Test
    public void validateRichMenuObjectTest() {
        stubFor(post(urlPathTemplate("/v2/bot/richmenu/validate")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            RichMenuRequest richMenuRequest = Arranger.some(RichMenuRequest.class, Map.of("message", () -> new TextMessage("hello"), "recipient", () -> null, "filter", () -> null));

        api.validateRichMenuObject(richMenuRequest).join().body();

        // TODO: test validations
    }

}
