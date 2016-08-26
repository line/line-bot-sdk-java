package com.linecorp.bot.model.v2;

import java.util.List;

import com.linecorp.bot.model.v2.message.Message;

import lombok.Value;

@Value
public class PushMessage {
    private List<String> to;
    private List<Message> messages;
}
