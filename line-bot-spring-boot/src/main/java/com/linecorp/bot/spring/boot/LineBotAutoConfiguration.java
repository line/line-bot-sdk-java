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

import java.util.HashMap;

import com.linecorp.bot.client.*;
import com.linecorp.bot.spring.boot.custom.LineMessagingClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.linecorp.bot.servlet.LineBotCallbackRequestParser;
import com.linecorp.bot.spring.boot.interceptor.LineBotServerInterceptor;
import com.linecorp.bot.spring.boot.support.LineBotServerArgumentProcessor;
import com.linecorp.bot.spring.boot.support.LineMessageHandlerSupport;
import org.springframework.util.StringUtils;

@Configuration
@AutoConfigureAfter(LineBotWebMvcConfigurer.class)
@EnableConfigurationProperties(LineBotProperties.class)
@Import(LineMessageHandlerSupport.class)
public class LineBotAutoConfiguration {
    @Autowired
    private LineBotProperties lineBotProperties;

    @Bean
    public LineMessagingClient lineMessagingClient(final LineMessagingClientFactory lineMessagingClientFactory) {
        return lineMessagingClientFactory.get(lineBotProperties.getChannelSecret());
    }

    @Bean
    public LineMessagingClientFactory lineMessagingClientFactory()
    {
        return new LineMessagingClientFactory(
            lineBotProperties.getAllBotList().stream()
                    .filter(bot -> !StringUtils.isEmpty(bot.getChannelSecret()))
                    .reduce(new HashMap<String, LineMessagingClient>(), (map, bot) -> {
                        map.put(bot.getChannelSecret(),
                                new LineMessagingClientImpl(LineMessagingServiceBuilder
                                    .create(bot.getChannelToken())
                                    .apiEndPoint(bot.getApiEndPoint())
                                    .connectTimeout(bot.getConnectTimeout())
                                    .readTimeout(bot.getReadTimeout())
                                    .writeTimeout(bot.getWriteTimeout())
                                    .build()));
                        return map;
                    },
                    (map1, map2) -> {
                        map1.putAll(map2);
                        return map1;
                    }));
    }

    @Bean
    @ConditionalOnWebApplication
    public LineBotServerArgumentProcessor lineBotServerArgumentProcessor() {
        return new LineBotServerArgumentProcessor();
    }

    @Bean
    @ConditionalOnWebApplication
    public LineBotServerInterceptor lineBotServerInterceptor() {
        return new LineBotServerInterceptor();
    }

    @Bean
    @ConditionalOnWebApplication
    public LineSignatureValidator lineSignatureValidator() {
        return new LineSignatureValidator(lineBotProperties.getAllBotList().stream()
                .filter(bot -> !StringUtils.isEmpty(bot.getChannelSecret()))
                .map(bot -> bot.getChannelSecret()));
    }

    @Bean
    @ConditionalOnWebApplication
    public LineBotCallbackRequestParser lineBotCallbackRequestParser(
            LineSignatureValidator lineSignatureValidator) {
        return new LineBotCallbackRequestParser(lineSignatureValidator);
    }
}
