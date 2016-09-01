package com.linecorp.bot.model;

import java.util.List;

import com.linecorp.bot.model.message.Message;

import lombok.Value;

@Value
public class ReplyMessage {
    private String replyToken;
    private List<Message> messages;
}
