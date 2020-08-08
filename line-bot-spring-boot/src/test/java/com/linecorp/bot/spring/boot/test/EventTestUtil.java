/*
 * Copyright 2018 LINE Corporation
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
 */

package com.linecorp.bot.spring.boot.test;

import java.time.Instant;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.UserSource;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EventTestUtil {
    public static MessageEvent<TextMessageContent> createTextMessage(final String text) {
        return MessageEvent.<TextMessageContent>builder()
                .replyToken("replyToken")
                .source(UserSource.builder()
                                  .userId("userId")
                                  .build())
                .message(TextMessageContent.builder()
                                           .id("id")
                                           .text(text)
                                           .build())
                .timestamp(Instant.parse("2016-11-19T00:00:00.000Z"))
                .build();
    }
}
