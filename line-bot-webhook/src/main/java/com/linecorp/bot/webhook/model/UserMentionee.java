/*
 * Copyright 2023 LINE Corporation
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

/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech Do not edit the class manually.
 */
package com.linecorp.bot.webhook.model;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/** Mentioned target is user */
@JsonTypeName("user")
@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record UserMentionee(
    /**
     * Index position of the user mention for a character in text, with the first character being at
     * position 0.
     */
    @JsonProperty("index") Integer index,
    /** The length of the text of the mentioned user. For a mention @example, 8 is the length. */
    @JsonProperty("length") Integer length,
    /**
     * User ID of the mentioned user. Only included if mention.mentions[].type is user and the user
     * consents to the LINE Official Account obtaining their user profile information.
     */
    @JsonProperty("userId") String userId,
    /** Whether the mentioned user is the bot that receives the webhook. */
    @JsonProperty("isSelf") Boolean isSelf)
    implements Mentionee {

  public static class Builder {
    private Integer index;
    private Integer length;
    private String userId;
    private Boolean isSelf;

    public Builder(Integer index, Integer length) {

      this.index = index;

      this.length = length;
    }

    public Builder userId(String userId) {
      this.userId = userId;
      return this;
    }

    public Builder isSelf(Boolean isSelf) {
      this.isSelf = isSelf;
      return this;
    }

    public UserMentionee build() {
      return new UserMentionee(index, length, userId, isSelf);
    }
  }
}
