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

import java.util.concurrent.CompletableFuture;

import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.profile.MembersIdsResponse;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.model.response.IssueLinkTokenResponse;
import com.linecorp.bot.model.response.NumberOfMessagesResponse;
import com.linecorp.bot.model.richmenu.RichMenu;
import com.linecorp.bot.model.richmenu.RichMenuIdResponse;
import com.linecorp.bot.model.richmenu.RichMenuListResponse;
import com.linecorp.bot.model.richmenu.RichMenuResponse;

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
     * Download image, video, and audio data sent from users.
     *
     * @see <a href="https://developers.line.me/en/reference/messaging-api/#get-content">//developers.line.me/en/reference/messaging-api/#get-content</a>
     */
    CompletableFuture<MessageContentResponse> getMessageContent(String messageId);

    /**
     * Gets the number of messages sent with the /bot/message/reply endpoint. Note that the number of messages
     * retrieved by this operation does not include the number of messages sent from LINE@ Manager.
     *
     * @param date Date the messages were sent. The format should be {@code yyyyMMdd} (for Example:
     *             {@literal "20191231"}) and the timezone should be UTC+9.
     */
    CompletableFuture<NumberOfMessagesResponse> getNumberOfSentReplyMessages(String date);

    /**
     * Gets the number of messages sent with the /bot/message/push endpoint. Note that the number of messages
     * retrieved by this operation does not include the number of messages sent from LINE@ Manager.
     *
     * @param date Date the messages were sent. The format should be {@code yyyyMMdd} (for Example:
     *             {@literal "20191231"}) and the timezone should be UTC+9.
     */
    CompletableFuture<NumberOfMessagesResponse> getNumberOfSentPushMessages(String date);

    /**
     * Gets the number of messages sent with the /bot/message/multicast endpoint. The number of messages
     * retrieved by this operation does not include the number of messages sent from LINE@ Manager.
     *
     * @param date Date the messages were sent. The format should be {@code yyyyMMdd} (for Example:
     *             {@literal "20191231"}) and the timezone should be UTC+9.
     */
    CompletableFuture<NumberOfMessagesResponse> getNumberOfSentMulticastMessages(String date);

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
     *
     * @see <a href="https://developers.line.me/en/reference/messaging-api/#get-group-member-profile">//developers.line.me/en/reference/messaging-api/#get-group-member-profile</a>
     */
    CompletableFuture<UserProfileResponse> getGroupMemberProfile(String groupId, String userId);

    /**
     * Get room member profile.
     *
     * @param roomId Identifier of the group. Can be get by {@link RoomSource#getRoomId()}.
     * @param userId Identifier of the user.
     *
     * @see <a href="https://developers.line.me/en/reference/messaging-api/#get-room-member-profile">//developers.line.me/en/reference/messaging-api/#get-room-member-profile</a>
     */
    CompletableFuture<UserProfileResponse> getRoomMemberProfile(String roomId, String userId);

    /**
     * Get (a part of) group member list.
     *
     * @param start nullable continuationToken which can be get {@link MembersIdsResponse#getNext()}
     *
     * @see <a href="https://developers.line.me/en/reference/messaging-api/#get-group-member-user-ids">//developers.line.me/en/reference/messaging-api/#get-group-member-user-ids</a>
     * @see MembersIdsResponse#getNext()
     */
    CompletableFuture<MembersIdsResponse> getGroupMembersIds(
            String groupId, String start);

    /**
     * Get (a part of) room member list.
     *
     * @param start nullable continuationToken which can be get {@link MembersIdsResponse#getNext()}
     *
     * @see <a href="https://developers.line.me/en/reference/messaging-api/#get-room-member-user-ids">//developers.line.me/en/reference/messaging-api/#get-room-member-user-ids</a>
     * @see MembersIdsResponse#getNext()
     */
    CompletableFuture<MembersIdsResponse> getRoomMembersIds(
            String roomId, String start);

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
     * Unlink rich menu from user.
     *
     * @see <a href="https://developers.line.me/en/docs/messaging-api/reference/#unlink-rich-menu-from-user">//developers.line.me/en/docs/messaging-api/reference/#unlink-rich-menu-from-user</a>
     */
    CompletableFuture<BotApiResponse> unlinkRichMenuIdFromUser(String userId);

    /**
     * Download rich menu image.
     *
     * @see <a href="https://developers.line.me/en/docs/messaging-api/reference/#download-rich-menu-image">//developers.line.me/en/docs/messaging-api/reference/#download-rich-menu-image</a>
     */
    CompletableFuture<MessageContentResponse> getRichMenuImage(String richMenuId);

    /**
     * Set RichMenu image.
     *
     * @see <a href="https://developers.line.me/en/docs/messaging-api/reference/#upload-rich-menu-image">//developers.line.me/en/docs/messaging-api/reference/#upload-rich-menu-image</a>
     */
    CompletableFuture<BotApiResponse> setRichMenuImage(
            String richMenuId, String contentType, byte[] content);

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
     * Issues a link token used for the account link feature.
     *
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#issue-link-token">Issue link token</a>
     */
    CompletableFuture<IssueLinkTokenResponse> issueLinkToken(String userId);

    static LineMessagingClientBuilder builder(String channelToken) {
        return builder(FixedChannelTokenSupplier.of(channelToken));
    }

    static LineMessagingClientBuilder builder(ChannelTokenSupplier channelTokenSupplier) {
        return new LineMessagingClientBuilder().channelTokenSupplier(channelTokenSupplier);
    }
}
