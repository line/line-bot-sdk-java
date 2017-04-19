/*
 * Copyright 2016 LINE Corporation
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

package com.linecorp.bot.spring.boot;

import java.net.URI;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.spring.boot.BotPropertiesValidator.ValidBotProperties;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import lombok.Data;

@Data
@Validated
@ValidBotProperties
@ConfigurationProperties(prefix = "line.bot")
public class LineBotProperties {
    /**
     * Channel token supply mode.
     *
     * @see ChannelTokenSupplyMode
     */
    @Valid
    @NotNull
    private ChannelTokenSupplyMode channelTokenSupplyMode = ChannelTokenSupplyMode.FIXED;

    /**
     * Channel acccess token.
     */
    @Valid
    private String channelToken;

    /**
     * Channel secret
     */
    @Valid
    @NotNull
    private String channelSecret;

    @Valid
    @NotNull
    private String apiEndPoint = LineMessagingServiceBuilder.DEFAULT_API_END_POINT;

    /**
     * Connection timeout in milliseconds
     */
    @Valid
    @NotNull
    private long connectTimeout = LineMessagingServiceBuilder.DEFAULT_CONNECT_TIMEOUT;

    /**
     * Read timeout in milliseconds
     */
    @Valid
    @NotNull
    private long readTimeout = LineMessagingServiceBuilder.DEFAULT_READ_TIMEOUT;

    /**
     * Write timeout in milliseconds
     */
    @Valid
    @NotNull
    private long writeTimeout = LineMessagingServiceBuilder.DEFAULT_WRITE_TIMEOUT;

    /**
     * Configuration for {@link LineMessageHandler} and {@link EventMapping}.
     */
    @Valid
    @NotNull
    private Handler handler = new Handler();

    @Data
    public static class Handler {
        /**
         * Flag to enable/disable {@link LineMessageHandler} and {@link EventMapping}.
         *
         * Default: {@code true}
         */
        boolean enabled = true;

        /**
         * REST endpoint path of dispatcher.
         */
        @NotNull
        URI path = URI.create("/callback");
    }

    enum ChannelTokenSupplyMode {
        /**
         * Use fixed channel token for public API user.
         */
        FIXED,

        /**
         * Supply channel token via channel token supplier for specific business partners.
         *
         * @see <a href="https://devdocs.line.me/#issue-channel-access-token"
         * >//devdocs.line.me/#issue-channel-access-token</a>
         */
        SUPPLIER,
    }
}
