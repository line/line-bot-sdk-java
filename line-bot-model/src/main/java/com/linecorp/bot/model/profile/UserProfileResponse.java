package com.linecorp.bot.model.profile;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

/*

{
  "requestId": "1231feaadc...",
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
    private final List<Profile> profiles;

    public UserProfileResponse(@JsonProperty("requestid") String requestId,
                               @JsonProperty("profiles") List<Profile> profiles) {
        this.requestId = requestId;
        this.profiles = profiles;
    }
}
