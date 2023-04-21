/*
 * Copyright 2023 LINE Corporation
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

package com.linecorp.bot.spring.boot.handler.support;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.linecorp.bot.spring.boot.handler.annotation.EventMapping;
import com.linecorp.bot.spring.boot.web.argument.annotation.LineBotDestination;
import com.linecorp.bot.webhook.model.Event;
import com.linecorp.bot.webhook.model.MemberJoinedEvent;
import com.linecorp.bot.webhook.model.MessageEvent;
import com.linecorp.bot.webhook.model.TextMessageContent;

class HandlerMethodScannerTest {
    private HandlerMethodScanner scanner = new HandlerMethodScanner();

    @Test
    void scan() {
    }

    @Test
    void testScan() {
        List<HandlerMethod> result = scanner.scan(List.of(new MyHandlers()));
        result.forEach(handlerMethod -> System.out.println(handlerMethod.handler().getName() + " " + handlerMethod.priority()));
        assertThat(result.stream().map(handlerMethod -> handlerMethod.handler().getName()).collect(Collectors.toList()))
                .isEqualTo(List.of("customPriority", "messageEventWithContent", "messageEvent", "eventWithDestination", "event"));
    }

    public static class MyHandlers {
        @EventMapping
        public void event(Event event) {
        }

        @EventMapping(priority = 10000)
        public void customPriority(MemberJoinedEvent event) {
        }

        @EventMapping
        public void messageEvent(MessageEvent event) {
        }

        @EventMapping
        public void messageEventWithContent(MessageEvent event, TextMessageContent textMessageContent) {
        }

        @EventMapping
        public void eventWithDestination(@LineBotDestination String destination, Event event) {
        }
    }
}
