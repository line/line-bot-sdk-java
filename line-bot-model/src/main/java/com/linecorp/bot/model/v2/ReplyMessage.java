package com.linecorp.bot.model.v2;

import java.util.List;

import com.linecorp.bot.model.v2.message.Message;

import lombok.Value;

@Value
public class ReplyMessage {
    private String replyToken;
    private List<Message> messages;
}
