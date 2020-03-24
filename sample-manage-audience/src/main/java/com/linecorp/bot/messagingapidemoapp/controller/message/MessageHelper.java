/*
 * Copyright 2020 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 */

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
