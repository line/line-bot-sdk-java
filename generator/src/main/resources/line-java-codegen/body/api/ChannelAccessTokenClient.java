default CompletableFuture<Result<IssueStatelessChannelAccessTokenResponse>> issueStatelessChannelTokenByJWTAssertion(
    String clientAssertion) {
    return issueStatelessChannelToken(
        "client_credentials",
        "urn:ietf:params:oauth:client-assertion-type:jwt-bearer",
        clientAssertion,
        null,
        null);
}

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
