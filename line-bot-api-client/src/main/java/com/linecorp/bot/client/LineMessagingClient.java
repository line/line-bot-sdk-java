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

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.linecorp.bot.model.Broadcast;
import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.Narrowcast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
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
import com.linecorp.bot.model.richmenu.RichMenuIdResponse;
import com.linecorp.bot.model.richmenu.RichMenuListResponse;
import com.linecorp.bot.model.richmenu.RichMenuResponse;
import com.linecorp.bot.model.richmenualias.CreateRichMenuAliasRequest;
import com.linecorp.bot.model.richmenualias.RichMenuAliasListResponse;
import com.linecorp.bot.model.richmenualias.RichMenuAliasResponse;
import com.linecorp.bot.model.richmenualias.UpdateRichMenuAliasRequest;
import com.linecorp.bot.model.room.RoomMemberCountResponse;

public interface LineMessagingClient {
    /**
     * Reply to messages from users.
     *
     * <p>Webhooks are used to notify you when an event occurs. For events that you can respond to,
     * a replyToken is issued for replying to messages.
     *
     * <p>Because the replyToken becomes invalid after a certain period of time,
     * responses should be sent as soon as a message is received. Reply tokens can only be used once.
     *
     * @see #pushMessage(PushMessage)
     * @see <a href="https://developers.line.me/en/reference/messaging-api/#send-reply-message">//developers.line.me/en/reference/messaging-api/#send-reply-message</a>
     */
    CompletableFuture<BotApiResponse> replyMessage(ReplyMessage replyMessage);

    /**
     * Send messages to users when you want to.
     *
     * <p>INFO: Use of the Push Message API is limited to certain plans.
     *
     * @see #replyMessage(ReplyMessage)
     * @see <a href="https://developers.line.me/en/reference/messaging-api/#send-push-message">//developers.line.me/en/reference/messaging-api/#send-push-message</a>
     */
    CompletableFuture<BotApiResponse> pushMessage(PushMessage pushMessage);

    /**
     * Send messages to multiple users at any time. <strong>IDs of groups or rooms cannot be used.</strong>
     *
     * <p>INFO: Only available for plans which support push messages.
     * Messages cannot be sent to groups or rooms.
     *
     * <p>INFO: Use IDs returned via the webhook event of source users. IDs of groups or rooms cannot be used.
     * Do not use the LINE ID found on the LINE app.</p>
     *
     * @see #pushMessage(PushMessage)
     * @see <a href="https://developers.line.me/en/reference/messaging-api/#send-multicast-messages">//developers.line.me/en/reference/messaging-api/#send-multicast-messages</a>
     */
    CompletableFuture<BotApiResponse> multicast(Multicast multicast);

    /**
     * Sends push messages to multiple users at any time.
     * Note: LINE@ accounts cannot call this API endpoint. Please migrate it to a LINE official account.
     * For more information, see <a href="https://developers.line.biz/en/docs/messaging-api/migrating-line-at/">
     * Migration of LINE@ accounts</a>.
     */
    CompletableFuture<BotApiResponse> broadcast(Broadcast broadcast);

    /**
     * Sends a push message to multiple users. You can specify recipients using attributes (such as age, gender,
     * OS, and region) or by retargeting (audiences). Messages cannot be sent to groups or rooms.
     *
     * <p>Note: LINE-@ accounts cannot call this API endpoint. Please migrate it to a LINE official account.
     * For more information, see <a href="https://developers.line.biz/en/docs/messaging-api/migrating-line-at/">
     * Migration of LINE@ accounts</a>.
     */
    CompletableFuture<BotApiResponse> narrowcast(Narrowcast broadcast);

    /**
     * Gets the status of a narrowcast message.
     */
    CompletableFuture<NarrowcastProgressResponse> getNarrowcastProgress(String requestId);

    /**
     * Gets the target limit for additional messages in the current month. The number of messages retrieved by
     * this operation includes the number of messages sent from LINE Official Account Manager.
     * Set a target limit with LINE Official Account Manager. For the procedures, refer to the LINE Official
     * Account Manager manual.
     * Note: LINE@ accounts cannot call this API endpoint.
     */
    CompletableFuture<MessageQuotaResponse> getMessageQuota();

    /**
     * Gets the number of messages sent in the current month. The number of messages retrieved by this
     * operation includes the number of messages sent from LINE Official Account Manager. The number of
     * messages retrieved by this operation is approximate. To get the correct number of sent messages,
     * use LINE Official Account Manager or execute API operations for getting the number of sent messages.
     * Note: LINE@ accounts cannot call this API endpoint.
     */
    CompletableFuture<QuotaConsumptionResponse> getMessageQuotaConsumption();

    /**
     * Gets the number of messages sent with the /bot/message/reply endpoint. Note that the number of messages
     * retrieved by this operation does not include the number of messages sent from LINE@ Manager.
     *
     * @param date Date the messages were sent. The format should be {@code yyyyMMdd} (for Example:
     *         {@literal "20191231"}) and the timezone should be UTC+9.
     */
    CompletableFuture<NumberOfMessagesResponse> getNumberOfSentReplyMessages(String date);

    /**
     * Gets the number of messages sent with the /bot/message/push endpoint. Note that the number of messages
     * retrieved by this operation does not include the number of messages sent from LINE@ Manager.
     *
     * @param date Date the messages were sent. The format should be {@code yyyyMMdd} (for Example:
     *         {@literal "20191231"}) and the timezone should be UTC+9.
     */
    CompletableFuture<NumberOfMessagesResponse> getNumberOfSentPushMessages(String date);

    /**
     * Gets the number of messages sent with the /bot/message/multicast endpoint. The number of messages
     * retrieved by this operation does not include the number of messages sent from LINE@ Manager.
     *
     * @param date Date the messages were sent. The format should be {@code yyyyMMdd} (for Example:
     *         {@literal "20191231"}) and the timezone should be UTC+9.
     */
    CompletableFuture<NumberOfMessagesResponse> getNumberOfSentMulticastMessages(String date);

    /**
     * Gets the number of messages sent with the {@code "/bot/message/broadcast"} endpoint. The number of
     * messages retrieved by this operation does not include the number of messages sent from LINE Official
     * Account Manager.
     * Note: LINE@ accounts cannot call this API endpoint. Please migrate it to a LINE official account.
     * For more information, see <a href="https://developers.line.biz/en/docs/messaging-api/migrating-line-at/">
     * Migration of LINE@ accounts</a>.
     *
     * @param date Date the messages were sent. The format should be {@code yyyyMMdd} (for Example:
     *         {@literal "20191231"}) and the timezone should be UTC+9.
     */
    CompletableFuture<NumberOfMessagesResponse> getNumberOfSentBroadcastMessages(String date);

    /**
     * Get user profile information.
     *
     * @see <a href="https://developers.line.me/en/reference/messaging-api/#get-profile">//developers.line.me/en/reference/messaging-api/#get-profile</a>
     */
    CompletableFuture<UserProfileResponse> getProfile(String userId);

    /**
     * Get group member profile.
     *
     * @param groupId Identifier of the group. Can be get by {@link GroupSource#getGroupId()}.
     * @param userId Identifier of the user.
     * @see <a href="https://developers.line.me/en/reference/messaging-api/#get-group-member-profile">//developers.line.me/en/reference/messaging-api/#get-group-member-profile</a>
     */
    CompletableFuture<UserProfileResponse> getGroupMemberProfile(String groupId, String userId);

    /**
     * Get room member profile.
     *
     * @param roomId Identifier of the group. Can be get by {@link RoomSource#getRoomId()}.
     * @param userId Identifier of the user.
     * @see <a href="https://developers.line.me/en/reference/messaging-api/#get-room-member-profile">//developers.line.me/en/reference/messaging-api/#get-room-member-profile</a>
     */
    CompletableFuture<UserProfileResponse> getRoomMemberProfile(String roomId, String userId);

    /**
     * Get (a part of) group member list.
     *
     * @param start nullable continuationToken which can be get {@link MembersIdsResponse#getNext()}
     * @see <a href="https://developers.line.me/en/reference/messaging-api/#get-group-member-user-ids">//developers.line.me/en/reference/messaging-api/#get-group-member-user-ids</a>
     * @see MembersIdsResponse#getNext()
     */
    CompletableFuture<MembersIdsResponse> getGroupMembersIds(String groupId, String start);

    /**
     * Get (a part of) room member list.
     *
     * @param start nullable continuationToken which can be get {@link MembersIdsResponse#getNext()}
     * @see <a href="https://developers.line.me/en/reference/messaging-api/#get-room-member-user-ids">//developers.line.me/en/reference/messaging-api/#get-room-member-user-ids</a>
     * @see MembersIdsResponse#getNext()
     */
    CompletableFuture<MembersIdsResponse> getRoomMembersIds(String roomId, String start);

    /**
     * Leave a group.
     *
     * @see <a href="https://developers.line.me/en/reference/messaging-api/#leave-group">//developers.line.me/en/reference/messaging-api/#leave-group</a>
     */
    CompletableFuture<BotApiResponse> leaveGroup(String groupId);

    /**
     * Leave a room.
     *
     * @see <a href="https://developers.line.me/en/reference/messaging-api/#leave-room">//developers.line.me/en/reference/messaging-api/#leave-room</a>
     */
    CompletableFuture<BotApiResponse> leaveRoom(String roomId);

    /**
     * Get group summary.
     *
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#group">//developers.line.biz/en/reference/messaging-api/#group</a>
     */
    CompletableFuture<GroupSummaryResponse> getGroupSummary(String groupId);

    /**
     * Get members in group count.
     *
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#get-members-group-count">//developers.line.biz/en/reference/messaging-api/#get-members-group-count</a>
     */
    CompletableFuture<GroupMemberCountResponse> getGroupMemberCount(String groupId);

    /**
     * Get members in room count.
     *
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#get-members-room-count">//developers.line.biz/en/reference/messaging-api/#get-members-room-count</a>
     */
    CompletableFuture<RoomMemberCountResponse> getRoomMemberCount(String roomId);

    /**
     * Get a rich menu.
     *
     * @see <a href="https://developers.line.me/en/docs/messaging-api/reference/#get-rich-menu">//developers.line.me/en/docs/messaging-api/reference/#get-rich-menu</a>
     */
    CompletableFuture<RichMenuResponse> getRichMenu(String richMenuId);

    /**
     * Creates a rich menu.
     *
     * <p>Note: You must upload a rich menu image
     * and link the rich menu to a user for the rich menu to be displayed.
     * You can create up to 10 rich menus for one bot.
     *
     * @see <a href="https://developers.line.me/en/docs/messaging-api/reference/#create-rich-menu">//developers.line.me/en/docs/messaging-api/reference/#create-rich-menu</a>
     */
    CompletableFuture<RichMenuIdResponse> createRichMenu(RichMenu richMenu);

    /**
     * Deletes a rich menu.
     *
     * @see <a href="https://developers.line.me/en/reference/messaging-api/#delete-rich-menu">//developers.line.me/en/reference/messaging-api/#delete-rich-menu</a>
     */
    CompletableFuture<BotApiResponse> deleteRichMenu(String richMenuId);

    /**
     * Get rich menu ID of user.
     *
     * @see <a href="https://developers.line.me/en/reference/messaging-api/#get-rich-menu-id-of-user">//developers.line.me/en/reference/messaging-api/#get-rich-menu-id-of-user</a>
     */
    CompletableFuture<RichMenuIdResponse> getRichMenuIdOfUser(String userId);

    /**
     * Link rich menu to user.
     *
     * @see <a href="https://developers.line.me/en/docs/messaging-api/reference/#link-rich-menu-to-user">//developers.line.me/en/docs/messaging-api/reference/#link-rich-menu-to-user</a>
     */
    CompletableFuture<BotApiResponse> linkRichMenuIdToUser(String userId, String richMenuId);

    /**
     * Link rich menu to users.
     *
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#link-rich-menu-to-users">
     *         Link rich menu to multiple users</a>
     */
    CompletableFuture<BotApiResponse> linkRichMenuIdToUsers(List<String> userIds, String richMenuId);

    /**
     * Unlink rich menu from user.
     *
     * @see <a href="https://developers.line.me/en/docs/messaging-api/reference/#unlink-rich-menu-from-user">//developers.line.me/en/docs/messaging-api/reference/#unlink-rich-menu-from-user</a>
     */
    CompletableFuture<BotApiResponse> unlinkRichMenuIdFromUser(String userId);

    /**
     * Unlink rich menu from users.
     *
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#unlink-rich-menu-from-users">
     *         Unlink rich menu to multiple users</a>
     */
    CompletableFuture<BotApiResponse> unlinkRichMenuIdFromUsers(List<String> userIds);

    /**
     * Gets a list of all uploaded rich menus.
     *
     * @see <a href="https://developers.line.me/en/docs/messaging-api/reference/#get-rich-menu-list">//developers.line.me/en/docs/messaging-api/reference/#get-rich-menu-list</a>
     */
    CompletableFuture<RichMenuListResponse> getRichMenuList();

    /**
     * Set default rich menu.
     *
     * @see <a href="https://developers.line.me/en/reference/messaging-api/#set-default-rich-menu">//developers.line.me/en/reference/messaging-api/#set-default-rich-menu</a>
     */
    CompletableFuture<BotApiResponse> setDefaultRichMenu(String richMenuId);

    /**
     * Get default rich menu ID.
     *
     * @see <a href="https://developers.line.me/en/reference/messaging-api/#get-default-rich-menu-id">//developers.line.me/en/reference/messaging-api/#get-default-rich-menu-id</a>
     */
    CompletableFuture<RichMenuIdResponse> getDefaultRichMenuId();

    /**
     * Cancel default rich menu.
     *
     * @see <a href="https://developers.line.me/en/reference/messaging-api/#cancel-default-rich-menu">//developers.line.me/en/reference/messaging-api/#cancel-default-rich-menu</a>
     */
    CompletableFuture<BotApiResponse> cancelDefaultRichMenu();

    /**
     * Create a rich menu alias.
     *
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#create-rich-menu-alias">//developers.line.biz/en/reference/messaging-api/#create-rich-menu-alias</a>
     */
    CompletableFuture<BotApiResponse> createRichMenuAlias(CreateRichMenuAliasRequest request);

    /**
     * Update the rich menu Id which associated with the rich menu alias.
     *
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#update-rich-menu-alias">//developers.line.biz/en/reference/messaging-api/#update-rich-menu-alias</a>
     */
    CompletableFuture<BotApiResponse> updateRichMenuAlias(String richMenuAliasId,
                                                          UpdateRichMenuAliasRequest request);

    /**
     * Get specified rich menu alias.
     *
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#get-rich-menu-alias-by-id">//developers.line.biz/en/reference/messaging-api/#get-rich-menu-alias-by-id</a>
     */
    CompletableFuture<RichMenuAliasResponse> getRichMenuAlias(String richMenuAliasId);

    /**
     * Get rich menu alias list.
     *
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#get-rich-menu-alias-list">//developers.line.biz/en/reference/messaging-api/#get-rich-menu-alias-list</a>
     */
    CompletableFuture<RichMenuAliasListResponse> getRichMenuAliasList();

    /**
     * Delete specified rich menu alias.
     *
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#delete-rich-menu-alias">//developers.line.biz/en/reference/messaging-api/#delete-rich-menu-alias</a>
     */
    CompletableFuture<BotApiResponse> deleteRichMenuAlias(String richMenuAliasId);

    /**
     * Issues a link token used for the account link feature.
     *
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#issue-link-token">Issue link token</a>
     */
    CompletableFuture<IssueLinkTokenResponse> issueLinkToken(String userId);

    /**
     * Get number of message deliveries.
     *
     * @see LineMessagingService#getNumberOfMessageDeliveries(String)
     */
    CompletableFuture<GetNumberOfMessageDeliveriesResponse> getNumberOfMessageDeliveries(String date);

    /**
     * Gets the number of users who have added the bot on or before a specified date.
     */
    CompletableFuture<GetNumberOfFollowersResponse> getNumberOfFollowers(String date);

    /**
     * Get a list of users who added your LINE Official Account as a friend.
     */
    CompletableFuture<GetFollowersResponse> getFollowers(GetFollowersRequest request);

    /**
     * Returns statistics about how users interact with narrowcast messages or broadcast messages sent from your
     * LINE Official Account.
     *
     * <p>You can get statistics per message or per bubble.</p>
     * @param requestId Request ID of a narrowcast message or broadcast message. Each Messaging API request has
     *                  a request ID. Find it in the response headers.
     */
    CompletableFuture<GetMessageEventResponse> getMessageEvent(String requestId);

    /**
     * Retrieves the demographic attributes for a bot's friends.
     *
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#get-demographic">Get friends demographics</a>
     */
    CompletableFuture<GetFriendsDemographicsResponse> getFriendsDemographics();

    /**
     * Gets a bot's basic information.
     *
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#get-bot-info">Get bot info</a>
     */
    CompletableFuture<BotInfoResponse> getBotInfo();

    /**
     * Gets webhook endpoint information.
     *
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#get-webhook-endpoint-information">Get webhook endpoint information</a>
     */
    CompletableFuture<GetWebhookEndpointResponse> getWebhookEndpoint();

    /**
     * Sets webhook endpoint URL.
     *
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#set-webhook-endpoint-url">Set webhook URL</a>
     */
    CompletableFuture<SetWebhookEndpointResponse> setWebhookEndpoint(SetWebhookEndpointRequest request);

    /**
     * Tests webhook endpoint.
     *
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#test-webhook-endpoint">Test webhook endpoint</a>
     */
    CompletableFuture<TestWebhookEndpointResponse> testWebhookEndpoint(TestWebhookEndpointRequest request);

    /**
     * Get statistics per aggregation unit.
     * <p>The API can only be used by corporate users who have submitted the required applications.</p>
     *
     * @see <a href="https://developers.line.biz/en/reference/partner-docs/#get-statistics-per-unit">Get statistics per unit</a>
     */
    CompletableFuture<GetStatisticsPerUnitResponse> getStatisticsPerUnit(String customAggregationUnit,
                                                                         String from, String to);

    /**
     * Get number of units used this month.
     * <p>The API can only be used by corporate users who have submitted the required applications.</p>
     *
     * @see <a href="https://developers.line.biz/en/reference/partner-docs/#get-number-of-units-used-this-month">Get number of units used this month</a>
     */
    CompletableFuture<GetAggregationUnitUsageResponse> getAggregationUnitUsage();

    /**
     * Get name list of units used this month.
     * <p>The API can only be used by corporate users who have submitted the required applications.</p>
     *
     * @see <a href="https://developers.line.biz/en/reference/partner-docs/#get-name-list-of-units-used-this-month">Get name list of units used this month</a>
     */
    CompletableFuture<GetAggregationUnitNameListResponse> getAggregationUnitNameList(String limit,
                                                                                     String start);

    static LineMessagingClientBuilder builder(String channelToken) {
        return builder(FixedChannelTokenSupplier.of(channelToken));
    }

    static LineMessagingClientBuilder builder(ChannelTokenSupplier channelTokenSupplier) {
        return new LineMessagingClientBuilder().channelTokenSupplier(channelTokenSupplier);
    }
}
