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

public interface LineMessagingClient {
    /**
     * Reply to messages from users.
     *
     * <p>Webhooks are used to notify you when an event occurs. For events that you can respond to,
     * a replyToken is issued for replying to messages.
     * <p>Because the replyToken becomes invalid after a certain period of time,
     * responses should be sent as soon as a message is received. Reply tokens can only be used once.
     *
     * @see #pushMessage(PushMessage)
     * @see <a href="https://devdocs.line.me?java#reply-message">//devdocs.line.me#reply-message</a>
     */
    CompletableFuture<BotApiResponse> replyMessage(ReplyMessage replyMessage);

    /**
     * Send messages to users when you want to.
     *
     * <p>INFO: Use of the Push Message API is limited to certain plans.
     *
     * @see #replyMessage(ReplyMessage)
     * @see <a href="https://devdocs.line.me?java#push-message">//devdocs.line.me#push-message</a>
     */
    CompletableFuture<BotApiResponse> pushMessage(PushMessage pushMessage);

    /**
     * Send messages to multiple users at any time. <strong>IDs of groups or rooms cannot be used.</strong>
     *
     * <p>INFO: Only available for plans which support push messages. Messages cannot be sent to groups or rooms.
     * <p>INFO: Use IDs returned via the webhook event of source users. IDs of groups or rooms cannot be used.
     * Do not use the LINE ID found on the LINE app.</p>
     * @see #pushMessage(PushMessage)
     * @see <a href="https://devdocs.line.me?java#multicast">//devdocs.line.me#multicast</a>
     */
    CompletableFuture<BotApiResponse> multicast(Multicast multicast);

    /**
     * Download image, video, and audio data sent from users.
     *
     * @see <a href="https://devdocs.line.me?java#get-content">//devdocs.line.me#get-content</a>
     */
    CompletableFuture<MessageContentResponse> getMessageContent(String messageId);

    /**
     * Get user profile information.
     *
     * @see <a href="https://devdocs.line.me?java#bot-api-get-profile">//devdocs.line.me#bot-api-get-profile</a>
     */
    CompletableFuture<UserProfileResponse> getProfile(String userId);

    /**
     * Get room member profile.
     *
     * @param groupId Identifier of the group. Can be get by {@link GroupSource#getGroupId()}.
     * @param userId Identifier of the user.
     *
     * @see <a href="https://devdocs.line.me?java#get-group-room-member-profile">//devdocs.line.me#get-group-room-member-profile</a>
     */
    CompletableFuture<UserProfileResponse> getGroupMemberProfile(String groupId, String userId);

    /**
     * Get group member profile.
     *
     * @param roomId Identifier of the group. Can be get by {@link RoomSource#getRoomId()}.
     * @param userId Identifier of the user.
     *
     * @see <a href="https://devdocs.line.me?java#get-group-room-member-profile">//devdocs.line.me#get-group-room-member-profile</a>
     */
    CompletableFuture<UserProfileResponse> getRoomMemberProfile(String roomId, String userId);

    /**
     * Get (a part of) group member list.
     *
     * @param start nullable continuationToken which can be get {@link MembersIdsResponse#getNext()}
     *
     * @see MembersIdsResponse#getNext()
     */
    CompletableFuture<MembersIdsResponse> getGroupMembersIds(
            String groupId, String start);

    /**
     * Get (a part of) room member list.
     *
     * @param start nullable continuationToken which can be get {@link MembersIdsResponse#getNext()}
     *
     * @see MembersIdsResponse#getNext()
     */
    CompletableFuture<MembersIdsResponse> getRoomMembersIds(
            String roomId, String start);

    /**
     * Leave a group.
     *
     * @see <a href="https://devdocs.line.me?java#leave">//devdocs.line.me#leave</a>
     */
    CompletableFuture<BotApiResponse> leaveGroup(String groupId);

    /**
     * Leave a room.
     *
     * @see <a href="https://devdocs.line.me?java#leave">//devdocs.line.me#leave</a>
     */
    CompletableFuture<BotApiResponse> leaveRoom(String roomId);

    static LineMessagingClientBuilder builder(String channelToken) {
        return new LineMessagingClientBuilder(channelToken);
    }

    static LineMessagingClientBuilder builder(ChannelTokenSupplier channelTokenSupplier) {
        return new LineMessagingClientBuilder(channelTokenSupplier);
    }
}
