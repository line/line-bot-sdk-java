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
import com.linecorp.bot.model.response.BotApiResponse;
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

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

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
        return toBotApiResponseFuture(retrofitImpl.pushMessage(pushMessage));
    }

    @Override
    public CompletableFuture<BotApiResponse> multicast(final Multicast multicast) {
        return toBotApiResponseFuture(retrofitImpl.multicast(multicast));
    }

    @Override
    public CompletableFuture<BotApiResponse> broadcast(Broadcast broadcast) {
        return toBotApiResponseFuture(retrofitImpl.broadcast(broadcast));
    }

    @Override
    public CompletableFuture<BotApiResponse> narrowcast(Narrowcast narrowcast) {
        return toBotApiResponseFuture(retrofitImpl.narrowcast(narrowcast));
    }

    /**
     * Gets the status of a narrowcast message.
     */
    @Override
    public CompletableFuture<NarrowcastProgressResponse> getNarrowcastProgress(String requestId) {
        return toFuture(retrofitImpl.getNarrowcastProgress(requestId));
    }

    @Override
    @SuppressWarnings("deprecation")
    public CompletableFuture<MessageContentResponse> getMessageContent(String messageId) {
        return blobDelegationTarget.getMessageContent(messageId);
    }

    @Override
    @SuppressWarnings("deprecation")
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
    @SuppressWarnings("deprecation")
    public CompletableFuture<MessageContentResponse> getRichMenuImage(final String richMenuId) {
        return blobDelegationTarget.getRichMenuImage(richMenuId);
    }

    @Override
    @SuppressWarnings("deprecation")
    public CompletableFuture<BotApiResponse> setRichMenuImage(
            final String richMenuId, final String contentType, final byte[] content) {
        return blobDelegationTarget.setRichMenuImage(richMenuId, contentType, content);
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
    public CompletableFuture<CreateAudienceGroupResponse> createAudienceGroup(
            CreateAudienceGroupRequest request) {
        return toFuture(retrofitImpl.createAudienceGroup(request));
    }

    @Override
    public CompletableFuture<BotApiResponse> addAudienceToAudienceGroup(
            AddAudienceToAudienceGroupRequest request) {
        return toBotApiFuture(retrofitImpl.addAudienceToAudienceGroup(request));
    }

    @Override
    public CompletableFuture<CreateClickBasedAudienceGroupResponse> createClickBasedAudienceGroup(
            CreateClickBasedAudienceGroupRequest request) {
        return toFuture(retrofitImpl.createClickBasedAudienceGroup(request));
    }

    @Override
    public CompletableFuture<CreateImpBasedAudienceGroupResponse> createImpBasedAudienceGroup(
            CreateImpBasedAudienceGroupRequest request) {
        return toFuture(retrofitImpl.createImpBasedAudienceGroup(request));
    }

    @Override
    public CompletableFuture<BotApiResponse> updateAudienceGroupDescription(
            long audienceGroupId, UpdateAudienceGroupDescriptionRequest request) {
        return toBotApiFuture(retrofitImpl.updateAudienceGroupDescription(audienceGroupId, request));
    }

    @Override
    public CompletableFuture<BotApiResponse> deleteAudienceGroup(long audienceGroupId) {
        return toBotApiFuture(retrofitImpl.deleteAudienceGroup(audienceGroupId));
    }

    @Override
    public CompletableFuture<GetAudienceGroupsResponse> getAudienceGroups(
            long page, String description, AudienceGroupStatus status, Long size,
            Boolean includesExternalPublicGroups, AudienceGroupCreateRoute createRoute) {
        return toFuture(retrofitImpl.getAudienceGroups(
                page, description, status, size,
                includesExternalPublicGroups, createRoute));
    }

    @Override
    public CompletableFuture<GetAudienceDataResponse> getAudienceData(long audienceGroupId) {
        return toFuture(retrofitImpl.getAudienceData(audienceGroupId));
    }

    @Override
    public CompletableFuture<GetAudienceGroupAuthorityLevelResponse> getAudienceGroupAuthorityLevel() {
        return toFuture(retrofitImpl.getAudienceGroupAuthorityLevel());
    }

    @Override
    public CompletableFuture<BotApiResponse> updateAudienceGroupAuthorityLevel(
            @Body UpdateAudienceGroupAuthorityLevelRequest request) {
        return toBotApiFuture(retrofitImpl.updateAudienceGroupAuthorityLevel(request));
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
