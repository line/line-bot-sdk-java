package com.linecorp.bot.spring.boot.test;

import java.time.Instant;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.UserSource;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EventTestUtil {
    public static MessageEvent<TextMessageContent> createTextMessage(final String text) {
        return new MessageEvent<>("replyToken", new UserSource("userId"),
                                  new TextMessageContent("id", text),
                                  Instant.parse("2016-11-19T00:00:00.000Z"));
    }
}
