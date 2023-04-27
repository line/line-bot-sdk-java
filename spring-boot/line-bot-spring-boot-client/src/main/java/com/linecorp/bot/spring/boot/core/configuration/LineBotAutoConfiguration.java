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

package com.linecorp.bot.spring.boot.core.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.linecorp.bot.audience.client.ManageAudienceBlobClient;
import com.linecorp.bot.audience.client.ManageAudienceClient;
import com.linecorp.bot.client.base.channel.ChannelTokenSupplier;
import com.linecorp.bot.client.base.channel.FixedChannelTokenSupplier;
import com.linecorp.bot.insight.client.InsightClient;
import com.linecorp.bot.liff.client.LiffClient;
import com.linecorp.bot.messaging.client.MessagingApiBlobClient;
import com.linecorp.bot.messaging.client.MessagingApiClient;
import com.linecorp.bot.oauth.client.ChannelAccessTokenClient;
import com.linecorp.bot.moduleattach.client.LineModuleAttachClient;
import com.linecorp.bot.spring.boot.core.properties.LineBotProperties;

/**
 * Also refers for web only beans definition.
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(LineBotProperties.class)
public class LineBotAutoConfiguration {
    private final LineBotProperties lineBotProperties;

    public LineBotAutoConfiguration(LineBotProperties lineBotProperties) {
        this.lineBotProperties = lineBotProperties;
    }

    /**
     * Expose {@link FixedChannelTokenSupplier} as {@link Bean}
     * in case of no other definition for {@link ChannelTokenSupplier} type.
     */
    @Bean
    @ConditionalOnMissingBean(ChannelTokenSupplier.class)
    public ChannelTokenSupplier channelTokenSupplier() {
        final String channelToken = lineBotProperties.channelToken();
        return FixedChannelTokenSupplier.of(channelToken);
    }

    /**
     * Expose {@link MessagingApiClient} as {@link Bean}.
     */
    @Bean
    @ConditionalOnMissingBean
    public MessagingApiClient messagingApiClient(
            final ChannelTokenSupplier channelTokenSupplier) {
        return MessagingApiClient
                .builder(channelTokenSupplier)
                .apiEndPoint(lineBotProperties.apiEndPoint())
                .connectTimeout(lineBotProperties.connectTimeout())
                .readTimeout(lineBotProperties.readTimeout())
                .writeTimeout(lineBotProperties.writeTimeout())
                .build();
    }

    /**
     * Expose {@link MessagingApiClient} as {@link Bean}.
     */
    @Bean
    @ConditionalOnMissingBean
    public MessagingApiBlobClient messagingApiBlobClient(
            final ChannelTokenSupplier channelTokenSupplier) {
        return MessagingApiBlobClient
                .builder(channelTokenSupplier)
                .apiEndPoint(lineBotProperties.blobEndPoint())
                .connectTimeout(lineBotProperties.connectTimeout())
                .readTimeout(lineBotProperties.readTimeout())
                .writeTimeout(lineBotProperties.writeTimeout())
                .build();
    }

    /**
     * Expose {@link LiffClient} as {@link Bean}.
     */
    @Bean
    @ConditionalOnMissingBean
    public LiffClient liffClient(
            final ChannelTokenSupplier channelTokenSupplier) {
        return LiffClient
                .builder(channelTokenSupplier)
                .apiEndPoint(lineBotProperties.apiEndPoint())
                .connectTimeout(lineBotProperties.connectTimeout())
                .readTimeout(lineBotProperties.readTimeout())
                .writeTimeout(lineBotProperties.writeTimeout())
                .build();
    }

    /**
     * Expose {@link ManageAudienceBlobClient} as {@link Bean}.
     */
    @Bean
    @ConditionalOnMissingBean
    public ManageAudienceBlobClient manageAudienceBlobClient(
            final ChannelTokenSupplier channelTokenSupplier) {
        return ManageAudienceBlobClient
                .builder(channelTokenSupplier)
                .apiEndPoint(lineBotProperties.blobEndPoint())
                .connectTimeout(lineBotProperties.connectTimeout())
                .readTimeout(lineBotProperties.readTimeout())
                .writeTimeout(lineBotProperties.writeTimeout())
                .build();
    }

    /**
     * Expose {@link ManageAudienceClient} as {@link Bean}.
     */
    @Bean
    @ConditionalOnMissingBean
    public ManageAudienceClient manageAudienceClient(
            final ChannelTokenSupplier channelTokenSupplier) {
        return ManageAudienceClient
                .builder(channelTokenSupplier)
                .apiEndPoint(lineBotProperties.apiEndPoint())
                .connectTimeout(lineBotProperties.connectTimeout())
                .readTimeout(lineBotProperties.readTimeout())
                .writeTimeout(lineBotProperties.writeTimeout())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public ChannelAccessTokenClient channelAccessTokenClient(
            final ChannelTokenSupplier channelTokenSupplier
    ) {
        return ChannelAccessTokenClient
                .builder(channelTokenSupplier)
                .apiEndPoint(lineBotProperties.apiEndPoint())
                .connectTimeout(lineBotProperties.connectTimeout())
                .readTimeout(lineBotProperties.readTimeout())
                .writeTimeout(lineBotProperties.writeTimeout())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public InsightClient insightClient(
            final ChannelTokenSupplier channelTokenSupplier
    ) {
        return InsightClient
                .builder(channelTokenSupplier)
                .apiEndPoint(lineBotProperties.apiEndPoint())
                .connectTimeout(lineBotProperties.connectTimeout())
                .readTimeout(lineBotProperties.readTimeout())
                .writeTimeout(lineBotProperties.writeTimeout())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public LineModuleAttachClient lineModuleAttachClient(
            final ChannelTokenSupplier channelTokenSupplier
    ) {
        return LineModuleAttachClient
                .builder(channelTokenSupplier)
                .apiEndPoint(lineBotProperties.managerEndPoint())
                .connectTimeout(lineBotProperties.connectTimeout())
                .readTimeout(lineBotProperties.readTimeout())
                .writeTimeout(lineBotProperties.writeTimeout())
                .build();
    }
}
