package com.linecorp.bot.model.profile;

import java.util.List;

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
    private String requestId;
    private List<Profile> profiles;
}
