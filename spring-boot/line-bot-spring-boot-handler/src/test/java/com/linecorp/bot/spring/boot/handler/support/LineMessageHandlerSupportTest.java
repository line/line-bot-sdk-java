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

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ConfigurableApplicationContext;

import com.linecorp.bot.messaging.model.TextMessage;
import com.linecorp.bot.spring.boot.handler.annotation.EventMapping;
import com.linecorp.bot.spring.boot.handler.annotation.LineMessageHandler;
import com.linecorp.bot.spring.boot.web.argument.annotation.LineBotDestination;
import com.linecorp.bot.webhook.model.Event;
import com.linecorp.bot.webhook.model.MessageEvent;

@ExtendWith(MockitoExtension.class)
public class LineMessageHandlerSupportTest {
    @Mock
    private ConfigurableApplicationContext applicationContext;

    @Mock
    private ReplyByReturnValueConsumer.Factory replyByReturnValueConsumerFactory;

    @Mock
    private ReplyByReturnValueConsumer replyByReturnValueConsumer;

    @InjectMocks
    private LineMessageHandlerSupport target;

    @Test
    public void testRefreshForOneItem() throws Exception {
        when(applicationContext.getBeansWithAnnotation(LineMessageHandler.class))
                .thenReturn(singletonMap("bean", new MessageHandler()));

        // Do
        target.refresh();

        // Verify
        assertThat(target.handlerMethods).hasSize(1);
        HandlerMethod handlerMethod = target.handlerMethods.get(0);

        assertThat(handlerMethod.argumentResolvers())
                .isInstanceOf(List.class);
        assertThat(handlerMethod.handler())
                .isInstanceOf(Method.class);
    }

    @Test
    public void testPrioritySelection() throws Exception {
        when(applicationContext.getBeansWithAnnotation(LineMessageHandler.class))
                .thenReturn(java.util.Map.of("bean", new MessageHandler(),
                        "anothrer", new AnotherMessageHandler()));

        // Do
        target.refresh();

        // Verify
        assertThat(target.handlerMethods.stream().map(handlerMethod -> handlerMethod.handler().getName())
                .collect(Collectors.toList()))
                .isEqualTo(Arrays.asList("generalMessageHandler", "defaultEventHandler"));
    }

    @Test
    public void dispatchDestination() {
        when(replyByReturnValueConsumerFactory.createForEvent(any()))
                .thenReturn(replyByReturnValueConsumer);
        final MessageEvent event = EventTestUtil.createTextMessage("text");

        when(applicationContext.getBeansWithAnnotation(LineMessageHandler.class))
                .thenReturn(singletonMap("bean", new DestinationHandler()));

        target.refresh();

        // Do
        target.dispatch("DESTDEST", event);

        // Verify
        verify(replyByReturnValueConsumerFactory).createForEvent(event);
        verify(replyByReturnValueConsumer, times(1)).accept(new TextMessage("DESTDEST"));
    }

    @LineMessageHandler
    public static class MessageHandler {
        @SuppressWarnings("rawtypes")
        @EventMapping
        public void generalMessageHandler(MessageEvent messageEvent) {
        }
    }

    @LineMessageHandler
    public static class AnotherMessageHandler {
        @EventMapping
        public void defaultEventHandler(Event event) {
        }
    }

    @LineMessageHandler
    public static class DestinationHandler {
        public DestinationHandler() {
        }

        @EventMapping
        public TextMessage dest(@LineBotDestination String destination, final Event event) {
            return new TextMessage(destination);
        }
    }
}
