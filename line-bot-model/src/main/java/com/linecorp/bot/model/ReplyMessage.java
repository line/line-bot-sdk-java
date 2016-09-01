package com.linecorp.bot.model;

import java.util.Collections;
import java.util.List;

import com.linecorp.bot.model.message.Message;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ReplyMessage {
    private String replyToken;
    private List<Message> messages;

    public ReplyMessage(String replyToken, Message message) {
        this(replyToken, Collections.singletonList(message));
    }
}
