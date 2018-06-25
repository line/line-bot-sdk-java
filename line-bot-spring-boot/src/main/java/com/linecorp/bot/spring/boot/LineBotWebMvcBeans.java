package com.linecorp.bot.spring.boot;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.servlet.LineBotCallbackRequestParser;
import com.linecorp.bot.spring.boot.interceptor.LineBotServerInterceptor;
import com.linecorp.bot.spring.boot.support.LineBotServerArgumentProcessor;

@Component
@ConditionalOnWebApplication
public class LineBotWebMvcBeans {
    @Autowired
    private LineBotProperties lineBotProperties;

    @Bean
    public LineBotServerArgumentProcessor lineBotServerArgumentProcessor() {
        return new LineBotServerArgumentProcessor();
    }

    @Bean
    public LineBotServerInterceptor lineBotServerInterceptor() {
        return new LineBotServerInterceptor();
    }

    @Bean
    public LineSignatureValidator lineSignatureValidator() {
        return new LineSignatureValidator(
                lineBotProperties.getChannelSecret().getBytes(StandardCharsets.US_ASCII));
    }

    @Bean
    public LineBotCallbackRequestParser lineBotCallbackRequestParser(
            LineSignatureValidator lineSignatureValidator) {
        return new LineBotCallbackRequestParser(lineSignatureValidator);
    }
}
