package com.example.bot.spring.echo.multibot.handler;

import static lombok.AccessLevel.NONE;

import java.util.function.Supplier;

import org.slf4j.MDC;

import com.linecorp.bot.model.event.Event;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Value
@Builder
public class CallbackContext {
    public static final String MDC_KEY_BOT_ID = "botId";
    public static final String MDC_KEY_EVENT_ID = "messageId";

    @Getter(NONE)
    String botId;

    @Getter(NONE)
    String channelToken;

    @Getter(NONE)
    String eventId;

    Event event;

    public <T> T makeContextWare(Supplier<T> apiCall) {
        try {
            MDC.put(MDC_KEY_BOT_ID, botId);
            MDC.put(MDC_KEY_EVENT_ID, eventId);
            BotContextHolder.setToken(channelToken);
            return apiCall.get();
        } finally {
            MDC.remove(MDC_KEY_EVENT_ID);
            MDC.remove(MDC_KEY_BOT_ID);
            BotContextHolder.clear();
        }
    }

    public void makeContextAware(Runnable apiCall) {
        try {
            MDC.put(MDC_KEY_BOT_ID, botId);
            MDC.put(MDC_KEY_EVENT_ID, eventId);
            BotContextHolder.setToken(channelToken);
            apiCall.run();
        } finally {
            MDC.remove(MDC_KEY_EVENT_ID);
            MDC.remove(MDC_KEY_BOT_ID);
            BotContextHolder.clear();
        }
    }
}
