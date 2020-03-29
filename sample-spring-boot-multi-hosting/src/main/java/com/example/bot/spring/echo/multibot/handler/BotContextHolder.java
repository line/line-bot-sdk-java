package com.example.bot.spring.echo.multibot.handler;

import java.util.Objects;

import lombok.experimental.PackagePrivate;
import lombok.experimental.UtilityClass;

@UtilityClass
@PackagePrivate
class BotContextHolder {
    private static final ThreadLocal<String> channelToken = new ThreadLocal<>();

    static String getToken() {
        return Objects.requireNonNull(channelToken.get(), "Token not set in this thread.");
    }

    static void setToken(final String token) {
        channelToken.set(token);
    }

    static void clear() {
        channelToken.remove();
    }
}
