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

package com.linecorp.bot.spring.boot.core.properties;

import java.net.URI;
import java.time.Duration;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import com.linecorp.bot.spring.boot.core.properties.BotPropertiesValidator.ValidBotProperties;

@Validated
@ValidBotProperties
@ConfigurationProperties(prefix = "line.bot")
public record LineBotProperties(
        /*
         * Channel token supply mode.
         *
         * @see ChannelTokenSupplyMode
         */
        @DefaultValue("FIXED")
        @NotNull ChannelTokenSupplyMode channelTokenSupplyMode,

        /*
         * Channel acccess token.
         */
        @Valid String channelToken,

        /*
         * Channel secret.
         */
        @Valid @NotNull String channelSecret,

        /*
         * apiEndPoint. default = https://api.line.me
         */
        @DefaultValue("https://api.line.me/")
        @Valid @NotNull URI apiEndPoint,

        /*
         * blobEndPoint. default = https://api-data.line.me
         */
        @DefaultValue("https://api-data.line.me/")
        @Valid @NotNull URI blobEndPoint,

        /*
         * managerEndPoint. default = https://manager.line.biz
         */
        @DefaultValue("https://manager.line.biz/")
        @Valid @NotNull URI managerEndPoint,

        /*
         * Connection timeout in milliseconds.
         */
        @DefaultValue("10s")
        @Valid @NotNull Duration connectTimeout,

        /*
         * Read timeout in milliseconds.
         */
        @DefaultValue("10s")
        @Valid @NotNull Duration readTimeout,

        /*
         * Write timeout in milliseconds.
         */
        @DefaultValue("10s")
        @Valid @NotNull Duration writeTimeout,

        /*
         * Skip signature verification of webhooks.
         */
        boolean skipSignatureVerification
) {
    public enum ChannelTokenSupplyMode {
        /**
         * Use fixed channel token for public API user.
         */
        FIXED,

        /**
         * Supply channel token via channel token supplier for specific business partners.
         *
         * @see <a href="https://developers.line.me/en/reference/messaging-api/#issue-channel-access-token"
         *         >//developers.line.me/en/reference/messaging-api/#issue-channel-access-token</a>
         */
        SUPPLIER,
    }
}
