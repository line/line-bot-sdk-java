package com.linecorp.bot.messagingapidemoapp.controller.message;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.message.Message;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class MessageHelper {
    private final ObjectMapper objectMapper;

    public List<Message> buildMessages(String[] messages) {
        return Arrays.stream(messages)
                     .filter(StringUtils::isNotBlank)
                     .map(it -> {
                         try {
                             return objectMapper.readValue(it, Message.class);
                         } catch (JsonProcessingException e) {
                             throw new RuntimeException(e);
                         }
                     }).collect(Collectors.toList());
    }
}
