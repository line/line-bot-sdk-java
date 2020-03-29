package com.example.bot.spring.echo.multibot.handler;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import com.linecorp.bot.spring.boot.LineBotProperties;

import lombok.Value;

@Value
@ConstructorBinding
@ConfigurationProperties("line.multibot")
public class MultiBotProtperties {
    Map<String, LineBotProperties> channelToPropertiesMap;
}
