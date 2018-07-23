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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.linecorp.bot.client.ChannelManagementSyncClient;
import com.linecorp.bot.client.ChannelTokenSupplier;
import com.linecorp.bot.client.FixedChannelTokenSupplier;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.spring.boot.support.LineMessageHandlerSupport;

/**
 * Also refers {@link LineBotWebMvcBeans} for web only beans definition.
 */
@Configuration
@AutoConfigureAfter(LineBotWebMvcConfigurer.class)
@EnableConfigurationProperties(LineBotProperties.class)
@Import(LineMessageHandlerSupport.class)
public class LineBotAutoConfiguration {
    @Autowired
    private LineBotProperties lineBotProperties;

    /**
     * Expose {@link FixedChannelTokenSupplier} as {@link Bean}
     * in case of no other definition for {@link ChannelTokenSupplier} type.
     */
    @Bean
    @ConditionalOnMissingBean(ChannelTokenSupplier.class)
    public ChannelTokenSupplier channelTokenSupplier() {
        final String channelToken = lineBotProperties.getChannelToken();
        return FixedChannelTokenSupplier.of(channelToken);
    }

    /**
     * Expose {@link LineMessagingClient} as {@link Bean}.
     */
    @Bean
    public LineMessagingClient lineMessagingClient(
            final ChannelTokenSupplier channelTokenSupplier) {
        return LineMessagingClient
                .builder(channelTokenSupplier)
                .apiEndPoint(lineBotProperties.getApiEndPoint())
                .connectTimeout(lineBotProperties.getConnectTimeout())
                .readTimeout(lineBotProperties.getReadTimeout())
                .writeTimeout(lineBotProperties.getWriteTimeout())
                .build();
    }

    /**
     * Expose {@link ChannelManagementSyncClient} as {@link Bean}.
     */
    @Bean
    public ChannelManagementSyncClient channelManagementClient(
            final ChannelTokenSupplier channelTokenSupplier) {
        return ChannelManagementSyncClient.builder(channelTokenSupplier)
                                          .build();
    }
}
