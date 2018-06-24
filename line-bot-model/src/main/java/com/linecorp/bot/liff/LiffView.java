package com.linecorp.bot.liff;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class LiffView {
    public enum Type {
        /**
         * 50% of the screen height of the device. This size can be specified only for the chat screen.
         */
        @JsonProperty("compact")
        COMPACT,

        /**
         * 80% of the screen height of the device. This size can be specified only for the chat screen.
         */
        @JsonProperty("tall")
        TALL,

        /**
         * 100% of the screen height of the device. This size can be specified for any screens in the LINE app.
         */
        @JsonProperty("full")
        FULL,
    }

    /**
     * Size of the LIFF app view. Specify one of the following values:
     */
    Type type;

    /**
     * URL of the LIFF app. The URL scheme must be https.
     */
    URI url;
}
