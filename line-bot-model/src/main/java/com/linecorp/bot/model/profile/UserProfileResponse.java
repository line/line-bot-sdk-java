package com.linecorp.bot.model.profile;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class UserProfileResponse {
    private final String displayName;
    private final String userId;
    private final String pictureUrl;
    private final String statusMessage;

    public UserProfileResponse(
            @JsonProperty("displayName") String displayName,
            @JsonProperty("userId") String userId,
            @JsonProperty("pictureUrl") String pictureUrl,
            @JsonProperty("statusMessage") String statusMessage) {
        this.displayName = displayName;
        this.userId = userId;
        this.pictureUrl = pictureUrl;
        this.statusMessage = statusMessage;
    }
}
