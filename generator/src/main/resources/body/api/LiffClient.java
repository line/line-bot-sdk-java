@Deprecated
default CompletableFuture<Result<GetAllLiffAppsResponse>> liffV1AppsGet() {
    return getAllLIFFApps();
}

@Deprecated
default CompletableFuture<Result<Void>> liffV1AppsLiffIdDelete(@Path("liffId") String liffId) {
    return deleteLIFFApp(liffId);
}

@Deprecated
default CompletableFuture<Result<Void>> liffV1AppsLiffIdPut(
    @Path("liffId") String liffId,
    @Body UpdateLiffAppRequest updateLiffAppRequest) {
    return updateLIFFApp(liffId, updateLiffAppRequest);
}

@Deprecated
default CompletableFuture<Result<AddLiffAppResponse>> liffV1AppsPost(@Body AddLiffAppRequest addLiffAppRequest) {
    return addLIFFApp(addLiffAppRequest);
}
