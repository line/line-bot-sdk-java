/**
 * Issues a new stateless channel access token by JWT assertion.
 * The newly issued token is only valid for 15 minutes but can not be revoked until it naturally expires.
 *
 * @param clientAssertion A JSON Web Token the client needs to create and sign with the private key of the Assertion Signing Key.
 * @return A {@link CompletableFuture} containing the {@link IssueStatelessChannelAccessTokenResponse}.
 * @see <a href="https://developers.line.biz/en/reference/messaging-api/#issue-stateless-channel-access-token">Documentation</a>
 */
@SuppressWarnings("deprecation")
default CompletableFuture<Result<IssueStatelessChannelAccessTokenResponse>> issueStatelessChannelTokenByJWTAssertion(
    String clientAssertion) {
    return issueStatelessChannelToken(
        "client_credentials",
        "urn:ietf:params:oauth:client-assertion-type:jwt-bearer",
        clientAssertion,
        null,
        null);
}

/**
 * Issues a new stateless channel access token by client secret.
 * The newly issued token is only valid for 15 minutes but can not be revoked until it naturally expires.
 *
 * @param clientId Channel ID.
 * @param clientSecret Channel secret.
 * @return A {@link CompletableFuture} containing the {@link IssueStatelessChannelAccessTokenResponse}.
 * @see <a href="https://developers.line.biz/en/reference/messaging-api/#issue-stateless-channel-access-token">Documentation</a>
 */
@SuppressWarnings("deprecation")
default CompletableFuture<Result<IssueStatelessChannelAccessTokenResponse>> issueStatelessChannelTokenByClientSecret(
    String clientId,
    String clientSecret) {
    return issueStatelessChannelToken(
        "client_credentials",
        null,
        null,
        clientId,
        clientSecret);
}
