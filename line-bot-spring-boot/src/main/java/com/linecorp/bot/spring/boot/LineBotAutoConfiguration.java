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

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.linecorp.bot.client.LineBotClient;
import com.linecorp.bot.client.LineBotClientBuilder;
import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.servlet.LineBotCallbackRequestParser;
import com.linecorp.bot.spring.boot.interceptor.LineBotServerInterceptor;
import com.linecorp.bot.spring.boot.support.LineBotServerArgumentProcessor;

@Configuration
@AutoConfigureAfter(LineBotWebMvcConfigurer.class)
@EnableConfigurationProperties(LineBotProperties.class)
public class LineBotAutoConfiguration {
    @Autowired
    private LineBotProperties lineBotProperties;

    @Bean
    @ConditionalOnMissingBean(LineBotClient.class)
    public LineBotClient lineBotClient() {
        RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectTimeout(lineBotProperties.getConnectTimeout())
                .setConnectionRequestTimeout(lineBotProperties.getConnectionRequestTimeout())
                .setSocketTimeout(lineBotProperties.getSocketTimeout())
                .build();

        HttpClientBuilder httpClientBuilder = HttpClientBuilder
                .create()
                .disableAutomaticRetries()
                .setDefaultRequestConfig(requestConfig)
                .setUserAgent("line-botsdk-java/" + this.getClass().getPackage().getImplementationVersion());

        return LineBotClientBuilder
                .create(
                        lineBotProperties.getChannelToken()
                )
                .apiEndPoint(lineBotProperties.getApiEndPoint())
                .httpClientBuilder(httpClientBuilder)
                .build();
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
    public LineSignatureValidator lineSignatureValidator()
            throws InvalidKeyException, NoSuchAlgorithmException {
        return new LineSignatureValidator(
                lineBotProperties.getChannelSecret().getBytes(StandardCharsets.US_ASCII));
    }

    @Bean
    @ConditionalOnWebApplication
    public LineBotCallbackRequestParser lineBotCallbackServletUtils(
            LineBotClient lineBotClient,
            LineSignatureValidator lineSignatureValidator) {
        return new LineBotCallbackRequestParser(lineBotClient, lineSignatureValidator);
    }
}
