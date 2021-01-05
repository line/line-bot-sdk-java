/*
 * Copyright 2019 LINE Corporation
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

import com.linecorp.bot.model.oauth.ChannelAccessTokenException;
import com.linecorp.bot.model.oauth.ChannelAccessTokenKeyIdsResponse;
import com.linecorp.bot.model.oauth.IssueChannelAccessTokenRequest;
import com.linecorp.bot.model.oauth.IssueChannelAccessTokenResponse;

/**
 * An OAuth client that issues or revokes channel access tokens. See
 * <a href="https://developers.line.biz/en/reference/messaging-api/#issue-channel-access-token">document</a>
 * for detail.
 */
public interface LineOAuthClient {
    /**
     * Creates a {@link LineOAuthClientBuilder}.
     */
    static LineOAuthClientBuilder builder() {
        return new LineOAuthClientBuilder();
    }

    /**
     * Gets all valid channel access token key IDs.
     * @param jwt A JSON Web Token (JWT) (opens new window)the client needs to create and sign with the private
     *            key.
     * @return <a href="https://developers.line.biz/en/reference/messaging-api/#get-all-valid-channel-access-token-key-ids-v2-1">Get all valid channel access token key IDs v2.1</a>
     */
    CompletableFuture<ChannelAccessTokenKeyIdsResponse> getsAllValidChannelAccessTokenKeyIdsByJWT(
            String jwt);

    /**
     * Issues a channel access token. This method lets you use JWT assertion for authentication.
     *
     * <p>You can issue up to 30 tokens.
     * If you reach the maximum limit, additional requests of issuing channel access tokens are blocked.
     *
     * @param clientAssertion A JSON Web Token the client needs to create and sign with the private key created
     *                        when issuing an assertion signing key.
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#issue-channel-access-token-v2-1">Issue channel access token v2.1</a>
     */
    CompletableFuture<IssueChannelAccessTokenResponse> issueChannelTokenByJWT(
            String clientAssertion);

    /**
     * Revokes a channel access token.
     *
     * @param clientId     Channel ID
     * @param clientSecret Channel Secret
     * @param accessToken  Channel access token
     * @see <a href="https://developers.line.biz/en/reference/messaging-api/#revoke-channel-access-token-v2-1">Revoke channel access token v2.1</a>
     */
    CompletableFuture<Void> revokeChannelTokenByJWT(
            String clientId,
            String clientSecret,
            String accessToken);

    /**
     * Issues a short-lived channel access token. Up to 30 tokens can be issued. If the maximum is exceeded,
     * existing channel access tokens are revoked in the order of when they were first issued.
     * It will return a failed {@link CompletableFuture} with {@link ChannelAccessTokenException} if
     * it has an error during calling the API.
     */
    CompletableFuture<IssueChannelAccessTokenResponse> issueChannelToken(IssueChannelAccessTokenRequest req);

    /**
     * Revokes a channel access token. It will return a failed {@link CompletableFuture} with
     * {@link ChannelAccessTokenException} if it has an error during calling the API.
     */
    CompletableFuture<Void> revokeChannelToken(String accessToken);
}
