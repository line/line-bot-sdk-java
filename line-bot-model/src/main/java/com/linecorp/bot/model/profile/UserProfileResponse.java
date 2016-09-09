package com.linecorp.bot.model.profile;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

/*

{
  "requestId": "1231feaadc...",git
  "profiles": [
    {
      "displayName":"BOT API",
      "userId":"U0047556f2e40dba2456887320ba7c76d",
      "pictureUrl":"http://dl.profile.line.naver.jp/abcdefghijklmn",
      "statusMessage":"Hello, LINE!"
    },
    ...
  ]
}

 */
@Value
public class UserProfileResponse {
    private final String requestId;
    private final String displayName;
    private final String userId;
    private final String pictureUrl;
    private final String statusMessage;

    public UserProfileResponse(
            @JsonProperty("requestIsd") String requestId,
            @JsonProperty("displayName") String displayName,
            @JsonProperty("userId") String userId,
            @JsonProperty("pictureUrl") String pictureUrl,
            @JsonProperty("statusMessage") String statusMessage) {
        this.requestId = requestId;
        this.displayName = displayName;
        this.userId = userId;
        this.pictureUrl = pictureUrl;
        this.statusMessage = statusMessage;
    }
}
