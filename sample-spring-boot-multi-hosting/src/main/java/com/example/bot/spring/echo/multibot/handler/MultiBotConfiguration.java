package com.example.bot.spring.echo.multibot.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.linecorp.bot.client.ChannelTokenSupplier;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Configuration(proxyBeanMethods = false)
public class MultiBotConfiguration {
    private final MultiBotProtperties multiBotProtperties;

    @Bean
    public ChannelTokenSupplier channelTokenSupplier() {
        return BotContextHolder::getToken;
    }
}
