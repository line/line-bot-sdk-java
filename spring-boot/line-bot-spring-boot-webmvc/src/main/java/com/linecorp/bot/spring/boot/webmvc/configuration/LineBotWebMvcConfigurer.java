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

package com.linecorp.bot.spring.boot.webmvc.configuration;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.linecorp.bot.spring.boot.web.argument.support.LineBotDestinationArgumentProcessor;
import com.linecorp.bot.spring.boot.web.argument.support.LineBotServerArgumentProcessor;
import com.linecorp.bot.spring.boot.web.configuration.LineBotWebBeans;
import com.linecorp.bot.spring.boot.web.interceptor.LineBotServerInterceptor;

@Configuration
@Import(LineBotWebBeans.class)
@ConditionalOnWebApplication
public class LineBotWebMvcConfigurer implements WebMvcConfigurer {
    private final LineBotServerInterceptor lineBotServerInterceptor;
    private final LineBotServerArgumentProcessor lineBotServerArgumentProcessor;
    private final LineBotDestinationArgumentProcessor lineBotDestinationArgumentProcessor;

    public LineBotWebMvcConfigurer(LineBotServerInterceptor lineBotServerInterceptor,
                                   LineBotServerArgumentProcessor lineBotServerArgumentProcessor,
                                   LineBotDestinationArgumentProcessor lineBotDestinationArgumentProcessor) {
        this.lineBotServerInterceptor = lineBotServerInterceptor;
        this.lineBotServerArgumentProcessor = lineBotServerArgumentProcessor;
        this.lineBotDestinationArgumentProcessor = lineBotDestinationArgumentProcessor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(lineBotServerInterceptor);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(lineBotServerArgumentProcessor);
        argumentResolvers.add(lineBotDestinationArgumentProcessor);
    }
}
