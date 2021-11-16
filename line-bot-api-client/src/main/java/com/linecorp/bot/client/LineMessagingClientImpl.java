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

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.linecorp.bot.client.exception.GeneralLineMessagingException;
import com.linecorp.bot.model.Broadcast;
import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.Narrowcast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.group.GroupMemberCountResponse;
import com.linecorp.bot.model.group.GroupSummaryResponse;
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

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Proxy implementation of {@link LineMessagingClient} to hind internal implementation.
 */
@Slf4j
@AllArgsConstructor
public class LineMessagingClientImpl implements LineMessagingClient {
    static final ExceptionConverter EXCEPTION_CONVERTER = new ExceptionConverter();
    private static final String ORG_TYPE_GROUP = "group"; // TODO Enum
    private static final String ORG_TYPE_ROOM = "room";
    private static final BotApiResponseBody BOT_API_SUCCESS_RESPONSE_BODY =
            BotApiResponseBody.builder().message("").details(emptyList()).build();

    private final LineMessagingService retrofitImpl;

    private final LineBlobClient blobDelegationTarget;

    @Override
    public CompletableFuture<BotApiResponse> replyMessage(final ReplyMessage replyMessage) {
        return toBotApiResponseFuture(retrofitImpl.replyMessage(replyMessage));
    }

    @Override
    public CompletableFuture<BotApiResponse> pushMessage(final PushMessage pushMessage) {
        return toBotApiResponseFuture(retrofitImpl.pushMessage(null, pushMessage));
    }

    @Override
    public CompletableFuture<BotApiResponse> multicast(final Multicast multicast) {
        return toBotApiResponseFuture(retrofitImpl.multicast(null, multicast));
    }

    @Override
    public CompletableFuture<BotApiResponse> broadcast(Broadcast broadcast) {
        return toBotApiResponseFuture(retrofitImpl.broadcast(null, broadcast));
    }

    @Override
    public CompletableFuture<BotApiResponse> narrowcast(Narrowcast narrowcast) {
        return toBotApiResponseFuture(retrofitImpl.narrowcast(null, narrowcast));
    }

    /**
     * Gets the status of a narrowcast message.
     */
    @Override
    public CompletableFuture<NarrowcastProgressResponse> getNarrowcastProgress(String requestId) {
        return toFuture(retrofitImpl.getNarrowcastProgress(requestId));
    }

    @Override
    public CompletableFuture<MessageQuotaResponse> getMessageQuota() {
        return toFuture(retrofitImpl.getMessageQuota());
    }

    @Override
    public CompletableFuture<QuotaConsumptionResponse> getMessageQuotaConsumption() {
        return toFuture(retrofitImpl.getMessageQuotaConsumption());
    }

    @Override
    public CompletableFuture<NumberOfMessagesResponse> getNumberOfSentReplyMessages(String date) {
        return toFuture(retrofitImpl.getNumberOfSentReplyMessages(date));
    }

    @Override
    public CompletableFuture<NumberOfMessagesResponse> getNumberOfSentPushMessages(String date) {
        return toFuture(retrofitImpl.getNumberOfSentPushMessages(date));
    }

    @Override
    public CompletableFuture<NumberOfMessagesResponse> getNumberOfSentMulticastMessages(String date) {
        return toFuture(retrofitImpl.getNumberOfSentMulticastMessages(date));
    }

    @Override
    public CompletableFuture<NumberOfMessagesResponse> getNumberOfSentBroadcastMessages(String date) {
        return toFuture(retrofitImpl.getNumberOfSentBroadcastMessages(date));
    }

    @Override
    public CompletableFuture<UserProfileResponse> getProfile(final String userId) {
        return toFuture(retrofitImpl.getProfile(userId));
    }

    @Override
    public CompletableFuture<UserProfileResponse> getGroupMemberProfile(
            final String groupId, final String userId) {
        return toFuture(retrofitImpl.getMemberProfile(ORG_TYPE_GROUP, groupId, userId));
    }

    @Override
    public CompletableFuture<UserProfileResponse> getRoomMemberProfile(
            final String roomId, final String userId) {
        return toFuture(retrofitImpl.getMemberProfile(ORG_TYPE_ROOM, roomId, userId));
    }

    @Override
    public CompletableFuture<MembersIdsResponse> getGroupMembersIds(
            final String groupId, final String start) {
        return toFuture(retrofitImpl.getMembersIds(ORG_TYPE_GROUP, groupId, start));
    }

    @Override
    public CompletableFuture<MembersIdsResponse> getRoomMembersIds(
            final String roomId, final String start) {
        return toFuture(retrofitImpl.getMembersIds(ORG_TYPE_ROOM, roomId, start));
    }

    @Override
    public CompletableFuture<BotApiResponse> leaveGroup(final String groupId) {
        return toBotApiResponseFuture(retrofitImpl.leaveGroup(groupId));
    }

    @Override
    public CompletableFuture<BotApiResponse> leaveRoom(final String roomId) {
        return toBotApiResponseFuture(retrofitImpl.leaveRoom(roomId));
    }

    @Override
    public CompletableFuture<GroupSummaryResponse> getGroupSummary(final String groupId) {
        return toFuture(retrofitImpl.getGroupSummary(groupId));
    }

    @Override
    public CompletableFuture<GroupMemberCountResponse> getGroupMemberCount(String groupId) {
        return toFuture(retrofitImpl.getGroupMemberCount(groupId));
    }

    @Override
    public CompletableFuture<RoomMemberCountResponse> getRoomMemberCount(String roomId) {
        return toFuture(retrofitImpl.getRoomMemberCount(roomId));
    }

    @Override
    public CompletableFuture<RichMenuResponse> getRichMenu(final String richMenuId) {
        return toFuture(retrofitImpl.getRichMenu(richMenuId));
    }

    @Override
    public CompletableFuture<RichMenuIdResponse> createRichMenu(final RichMenu richMenu) {
        return toFuture(retrofitImpl.createRichMenu(richMenu));
    }

    @Override
    public CompletableFuture<BotApiResponse> deleteRichMenu(final String richMenuId) {
        return toBotApiFuture(retrofitImpl.deleteRichMenu(richMenuId));
    }

    @Override
    public CompletableFuture<RichMenuIdResponse> getRichMenuIdOfUser(final String userId) {
        return toFuture(retrofitImpl.getRichMenuIdOfUser(userId));
    }

    @Override
    public CompletableFuture<BotApiResponse> linkRichMenuIdToUser(
            final String userId, final String richMenuId) {
        return toBotApiFuture(retrofitImpl.linkRichMenuToUser(userId, richMenuId));
    }

    @Override
    public CompletableFuture<BotApiResponse> linkRichMenuIdToUsers(List<String> userIds, String richMenuId) {
        return toBotApiFuture(retrofitImpl.linkRichMenuToUsers(RichMenuBulkLinkRequest.builder()
                                                                                      .richMenuId(richMenuId)
                                                                                      .userIds(userIds)
                                                                                      .build()));
    }

    @Override
    public CompletableFuture<BotApiResponse> unlinkRichMenuIdFromUser(final String userId) {
        return toBotApiFuture(retrofitImpl.unlinkRichMenuIdFromUser(userId));
    }

    @Override
    public CompletableFuture<BotApiResponse> unlinkRichMenuIdFromUsers(List<String> userIds) {
        return toBotApiFuture(retrofitImpl.unlinkRichMenuIdFromUsers(RichMenuBulkUnlinkRequest.builder()
                                                                                              .userIds(userIds)
                                                                                              .build()));
    }

    @Override
    public CompletableFuture<RichMenuListResponse> getRichMenuList() {
        return toFuture(retrofitImpl.getRichMenuList());
    }

    @Override
    public CompletableFuture<BotApiResponse> setDefaultRichMenu(final String richMenuId) {
        return toBotApiFuture(retrofitImpl.setDefaultRichMenu(richMenuId));
    }

    @Override
    public CompletableFuture<RichMenuIdResponse> getDefaultRichMenuId() {
        return toFuture(retrofitImpl.getDefaultRichMenuId());
    }

    @Override
    public CompletableFuture<BotApiResponse> cancelDefaultRichMenu() {
        return toBotApiFuture(retrofitImpl.cancelDefaultRichMenu());
    }

    @Override
    public CompletableFuture<BotApiResponse> createRichMenuAlias(CreateRichMenuAliasRequest request) {
        return toBotApiResponseFuture(retrofitImpl.createRichMenuAlias(request));
    }

    @Override
    public CompletableFuture<BotApiResponse> updateRichMenuAlias(String richMenuAliasId,
                                                                 UpdateRichMenuAliasRequest request) {
        return toBotApiResponseFuture(retrofitImpl.updateRichMenuAlias(richMenuAliasId, request));
    }

    @Override
    public CompletableFuture<RichMenuAliasResponse> getRichMenuAlias(
            String richMenuAliasId) {
        return toFuture(retrofitImpl.getRichMenuAlias(richMenuAliasId));
    }

    @Override
    public CompletableFuture<RichMenuAliasListResponse> getRichMenuAliasList() {
        return toFuture(retrofitImpl.getRichMenuAliasList());
    }

    @Override
    public CompletableFuture<BotApiResponse> deleteRichMenuAlias(String richMenuAliasId) {
        return toBotApiResponseFuture(retrofitImpl.deleteRichMenuAlias(richMenuAliasId));
    }

    @Override
    public CompletableFuture<IssueLinkTokenResponse> issueLinkToken(String userId) {
        return toFuture(retrofitImpl.issueLinkToken(userId));
    }

    @Override
    public CompletableFuture<GetFriendsDemographicsResponse> getFriendsDemographics() {
        return toFuture(retrofitImpl.getFriendsDemographics());
    }

    @Override
    public CompletableFuture<GetNumberOfMessageDeliveriesResponse> getNumberOfMessageDeliveries(String date) {
        return toFuture(retrofitImpl.getNumberOfMessageDeliveries(date));
    }

    @Override
    public CompletableFuture<GetNumberOfFollowersResponse> getNumberOfFollowers(String date) {
        return toFuture(retrofitImpl.getNumberOfFollowers(date));
    }

    @Override
    public CompletableFuture<GetFollowersResponse> getFollowers(GetFollowersRequest request) {
        return toFuture(retrofitImpl.getFollowers(request.getNext(), request.getLimit()));
    }

    @Override
    public CompletableFuture<GetMessageEventResponse> getMessageEvent(String requestId) {
        return toFuture(retrofitImpl.getMessageEvent(requestId));
    }

    @Override
    public CompletableFuture<BotInfoResponse> getBotInfo() {
        return toFuture(retrofitImpl.getBotInfo());
    }

    @Override
    public CompletableFuture<GetWebhookEndpointResponse> getWebhookEndpoint() {
        return toFuture(retrofitImpl.getWebhookEndpoint());
    }

    @Override
    public CompletableFuture<SetWebhookEndpointResponse> setWebhookEndpoint(SetWebhookEndpointRequest request) {
        return toFuture(retrofitImpl.setWebhookEndpoint(request));
    }

    @Override
    public CompletableFuture<TestWebhookEndpointResponse> testWebhookEndpoint(
            TestWebhookEndpointRequest request) {
        return toFuture(retrofitImpl.testWebhookEndpoint(request));
    }

    @Override
    public CompletableFuture<GetStatisticsPerUnitResponse> getStatisticsPerUnit(
            String customAggregationUnit, String from, String to) {
        return toFuture(retrofitImpl.getStatisticsPerUnit(customAggregationUnit, from, to));
    }

    @Override
    public CompletableFuture<GetAggregationUnitUsageResponse> getAggregationUnitUsage() {
        return toFuture(retrofitImpl.getAggregationUnitUsage());
    }

    @Override
    public CompletableFuture<GetAggregationUnitNameListResponse> getAggregationUnitNameList(
            String limit, String start) {
        return toFuture(retrofitImpl.getAggregationUnitNameList(limit, start));
    }

    // TODO: Extract this method.
    static <T> CompletableFuture<T> toFuture(Call<T> callToWrap) {
        final CallbackAdaptor<T> completableFuture = new CallbackAdaptor<>();
        callToWrap.enqueue(completableFuture);
        return completableFuture;
    }

    private static CompletableFuture<BotApiResponse> toBotApiResponseFuture(
            final Call<BotApiResponseBody> callToWrap) {
        final BotApiCallbackAdaptor completableFuture = new BotApiCallbackAdaptor();
        callToWrap.enqueue(completableFuture);
        return completableFuture;
    }

    static CompletableFuture<BotApiResponse> toBotApiFuture(Call<Void> callToWrap) {
        final VoidToBotApiCallbackAdaptor completableFuture = new VoidToBotApiCallbackAdaptor();
        callToWrap.enqueue(completableFuture);
        return completableFuture;
    }

    static class CallbackAdaptor<T> extends CompletableFuture<T> implements Callback<T> {
        @Override
        public void onResponse(final Call<T> call, final Response<T> response) {
            if (response.isSuccessful()) {
                complete(response.body());
            } else {
                completeExceptionally(EXCEPTION_CONVERTER.apply(response));
            }
        }

        @Override
        public void onFailure(final Call<T> call, final Throwable t) {
            completeExceptionally(
                    new GeneralLineMessagingException(t.getMessage(), null, t));
        }
    }

    static class VoidToBotApiCallbackAdaptor extends CompletableFuture<BotApiResponse>
            implements Callback<Void> {
        @Override
        public void onResponse(final Call<Void> call, final Response<Void> response) {
            if (response.isSuccessful()) {
                final String requestId = response.headers().get("x-line-request-id");
                complete(BOT_API_SUCCESS_RESPONSE_BODY.withRequestId(requestId));
            } else {
                completeExceptionally(EXCEPTION_CONVERTER.apply(response));
            }
        }

        @Override
        public void onFailure(final Call<Void> call, final Throwable t) {
            completeExceptionally(
                    new GeneralLineMessagingException(t.getMessage(), null, t));
        }
    }

    static class BotApiCallbackAdaptor extends CompletableFuture<BotApiResponse>
            implements Callback<BotApiResponseBody> {
        @Override
        public void onResponse(final Call<BotApiResponseBody> call,
                               final Response<BotApiResponseBody> response) {
            if (response.isSuccessful()) {
                final String requestId = response.headers().get("x-line-request-id");
                complete(response.body().withRequestId(requestId));
            } else {
                completeExceptionally(EXCEPTION_CONVERTER.apply(response));
            }
        }

        @Override
        public void onFailure(final Call<BotApiResponseBody> call, final Throwable t) {
            completeExceptionally(
                    new GeneralLineMessagingException(t.getMessage(), null, t));
        }
    }
}
