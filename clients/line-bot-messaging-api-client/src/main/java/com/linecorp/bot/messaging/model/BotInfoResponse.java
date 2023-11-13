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
package com.linecorp.bot.messaging.model;



import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;

/**
 * BotInfoResponse
 *
 * @see <a href="https://developers.line.biz/en/reference/messaging-api/#get-bot-info">
 *     Documentation</a>
 */
@JsonInclude(Include.NON_NULL)
@javax.annotation.Generated(value = "com.linecorp.bot.codegen.LineJavaCodegenGenerator")
public record BotInfoResponse(
    /** Bot&#39;s user ID */
    @JsonProperty("userId") String userId,
    /** Bot&#39;s basic ID */
    @JsonProperty("basicId") String basicId,
    /** Bot&#39;s premium ID. Not included in the response if the premium ID isn&#39;t set. */
    @JsonProperty("premiumId") String premiumId,
    /** Bot&#39;s display name */
    @JsonProperty("displayName") String displayName,
    /**
     * Profile image URL. &#x60;https&#x60; image URL. Not included in the response if the bot
     * doesn&#39;t have a profile image.
     */
    @JsonProperty("pictureUrl") URI pictureUrl,
    /**
     * Chat settings set in the LINE Official Account Manager. One of: &#x60;chat&#x60;: Chat is set
     * to \&quot;On\&quot;. &#x60;bot&#x60;: Chat is set to \&quot;Off\&quot;.
     */
    @JsonProperty("chatMode") ChatMode chatMode,
    /**
     * Automatic read setting for messages. If the chat is set to \&quot;Off\&quot;, auto is
     * returned. If the chat is set to \&quot;On\&quot;, manual is returned. &#x60;auto&#x60;: Auto
     * read setting is enabled. &#x60;manual&#x60;: Auto read setting is disabled.
     */
    @JsonProperty("markAsReadMode") MarkAsReadMode markAsReadMode) {

  /**
   * Chat settings set in the LINE Official Account Manager. One of: &#x60;chat&#x60;: Chat is set
   * to \&quot;On\&quot;. &#x60;bot&#x60;: Chat is set to \&quot;Off\&quot;.
   */
  public enum ChatMode {
    @JsonProperty("chat")
    CHAT,
    @JsonProperty("bot")
    BOT,

    @JsonEnumDefaultValue
    UNDEFINED;
  }

  /**
   * Automatic read setting for messages. If the chat is set to \&quot;Off\&quot;, auto is returned.
   * If the chat is set to \&quot;On\&quot;, manual is returned. &#x60;auto&#x60;: Auto read setting
   * is enabled. &#x60;manual&#x60;: Auto read setting is disabled.
   */
  public enum MarkAsReadMode {
    @JsonProperty("auto")
    AUTO,
    @JsonProperty("manual")
    MANUAL,

    @JsonEnumDefaultValue
    UNDEFINED;
  }

  public static class Builder {
    private String userId;
    private String basicId;
    private String premiumId;
    private String displayName;
    private URI pictureUrl;
    private ChatMode chatMode;
    private MarkAsReadMode markAsReadMode;

    public Builder(
        String userId,
        String basicId,
        String displayName,
        ChatMode chatMode,
        MarkAsReadMode markAsReadMode) {

      this.userId = userId;

      this.basicId = basicId;

      this.displayName = displayName;

      this.chatMode = chatMode;

      this.markAsReadMode = markAsReadMode;
    }

    public Builder premiumId(String premiumId) {
      this.premiumId = premiumId;
      return this;
    }

    public Builder pictureUrl(URI pictureUrl) {
      this.pictureUrl = pictureUrl;
      return this;
    }

    public BotInfoResponse build() {
      return new BotInfoResponse(
          userId, basicId, premiumId, displayName, pictureUrl, chatMode, markAsReadMode);
    }
  }
}
