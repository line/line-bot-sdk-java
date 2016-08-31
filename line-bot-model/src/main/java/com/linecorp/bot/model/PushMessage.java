package com.linecorp.bot.model;

import java.util.List;

import com.linecorp.bot.model.message.Message;

import lombok.Value;

@Value
public class PushMessage {
    private String to;
    private List<Message> messages;
}
