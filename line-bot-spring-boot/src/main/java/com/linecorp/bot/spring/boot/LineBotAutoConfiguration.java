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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.client.LineMessagingService;
import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.servlet.LineBotCallbackRequestParser;
import com.linecorp.bot.spring.boot.interceptor.LineBotServerInterceptor;
import com.linecorp.bot.spring.boot.support.LineBotServerArgumentProcessor;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
@AutoConfigureAfter(LineBotWebMvcConfigurer.class)
@EnableConfigurationProperties(LineBotProperties.class)
public class LineBotAutoConfiguration {
    @Autowired
    private LineBotProperties lineBotProperties;

    @Bean
    public LineMessagingService lineMessagingService(ObjectMapper objectMapper) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                                           // will be deprecate
                                           .addHeader("X-LINE-ChannelToken",
                                                      lineBotProperties.getChannelToken())
                                           .addHeader("Authorization",
                                                      "Bearer " + lineBotProperties.getChannelToken())
                                           .build();
                    return chain.proceed(request);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(lineBotProperties.getApiEndPoint())
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();

        return retrofit.create(LineMessagingService.class);
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
    public LineBotCallbackRequestParser lineBotCallbackRequestParser(
            LineSignatureValidator lineSignatureValidator,
            ObjectMapper objectMapper) {
        return new LineBotCallbackRequestParser(lineSignatureValidator, objectMapper);
    }
}
