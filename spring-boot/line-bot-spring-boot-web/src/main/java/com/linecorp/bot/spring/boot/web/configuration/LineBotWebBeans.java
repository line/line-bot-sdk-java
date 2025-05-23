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

package com.linecorp.bot.spring.boot.web.configuration;

import java.nio.charset.StandardCharsets;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import com.linecorp.bot.parser.FixedSkipSignatureVerificationSupplier;
import com.linecorp.bot.parser.LineSignatureValidator;
import com.linecorp.bot.parser.SkipSignatureVerificationSupplier;
import com.linecorp.bot.parser.WebhookParser;
import com.linecorp.bot.spring.boot.core.properties.LineBotProperties;
import com.linecorp.bot.spring.boot.web.argument.support.LineBotDestinationArgumentProcessor;
import com.linecorp.bot.spring.boot.web.argument.support.LineBotServerArgumentProcessor;
import com.linecorp.bot.spring.boot.web.interceptor.LineBotServerInterceptor;

@Component
@ConditionalOnWebApplication
@Import({LineBotServerInterceptor.class, LineBotServerArgumentProcessor.class,
        LineBotDestinationArgumentProcessor.class})
public class LineBotWebBeans {
    private final LineBotProperties lineBotProperties;

    public LineBotWebBeans(LineBotProperties lineBotProperties) {
        this.lineBotProperties = lineBotProperties;
    }

    @Bean
    @ConditionalOnMissingBean(SkipSignatureVerificationSupplier.class)
    public SkipSignatureVerificationSupplier skipSignatureVerificationSupplier() {
        final boolean skipVerification = lineBotProperties.skipSignatureVerification();
        return FixedSkipSignatureVerificationSupplier.of(skipVerification);
    }

    /**
     * Expose {@link LineSignatureValidator} as {@link Bean}.
     */
    @Bean
    public LineSignatureValidator lineSignatureValidator() {
        return new LineSignatureValidator(
                lineBotProperties.channelSecret().getBytes(StandardCharsets.US_ASCII));
    }

    /**
     * Expose {@link WebhookParser} as {@link Bean}.
     */
    @Bean
    public WebhookParser lineBotCallbackRequestParser(
            LineSignatureValidator lineSignatureValidator,
            SkipSignatureVerificationSupplier skipSignatureVerificationSupplier) {
        return new WebhookParser(lineSignatureValidator, skipSignatureVerificationSupplier);
    }
}
