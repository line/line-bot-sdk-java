package com.linecorp.bot.model;

import java.util.Collections;
import java.util.List;

import com.linecorp.bot.model.message.Message;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class PushMessage {
    private final List<String> to;
    private final List<Message> messages;

    public PushMessage(List<String> to, Message message) {
        this.to = to;
        this.messages = Collections.singletonList(message);
    }
}
