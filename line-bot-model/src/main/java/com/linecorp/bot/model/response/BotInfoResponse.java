package com.linecorp.bot.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.linecorp.bot.model.response.BotInfoResponse.BotInfoResponseBuilder;
import lombok.Builder;
import lombok.Value;

import java.net.URI;

@Value
@Builder
@JsonDeserialize(builder = BotInfoResponseBuilder.class)
public class BotInfoResponse {
    /**
     * Bot's user ID
     */
    String userId;

    /**
     * Bot's basic ID
     */
    String basicId;

    /**
     * Bot's premium ID. Not included in the response if the premium ID isn't set.
     */
    String premiumId;

    /**
     * Bot's display name
     */
    String displayName;

    /**
     * Profile image URL. "https" image URL. Not included in the response if the bot doesn't have a profile image.
     */
    URI pictureUrl;

    /**
     * Bot's response mode set in the LINE Official Account Manager.
     */
    ChatMode chatMode;

    /**
     * Automatic read setting for messages.
     * If the bot's response mode is "Bot", auto is returned.
     * If the response mode is "Chat", manual is returned.
     */
    MarkAsReadMode markAsReadMode;

    public enum ChatMode {
        /**
         * The response mode is set to "Chat".
         */
        @JsonProperty("chat")
        CHAT,

        /**
         * The response mode is set to "Bot".
         */
        @JsonProperty("bot")
        BOT
    }

    public enum MarkAsReadMode {
        /**
         * Auto read setting is enabled.
         */
        @JsonProperty("auto")
        AUTO,

        /**
         * Auto read setting is disabled.
         */
        @JsonProperty("manual")
        MANUAL
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class BotInfoResponseBuilder {
    }
}
