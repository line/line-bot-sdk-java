/*
 * Copyright 2016 LINE Corporation
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

package com.linecorp.bot.spring.boot.support;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.context.ConfigurableApplicationContext;

import com.google.common.collect.ImmutableMap;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.ReplyEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.linecorp.bot.spring.boot.support.LineMessageHandlerSupport.HandlerMethod;
import com.linecorp.bot.spring.boot.test.EventTestUtil;

import lombok.AllArgsConstructor;

public class LineMessageHandlerSupportTest {
    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ConfigurableApplicationContext applicationContext;

    @Mock
    private ReplyByReturnValueConsumer.Factory replyByReturnValueConsumerFactory;

    @Mock
    private ReplyByReturnValueConsumer replyByReturnValueConsumer;

    @InjectMocks
    private LineMessageHandlerSupport target;

    @Before
    public void setUp() {
        when(replyByReturnValueConsumerFactory.createForEvent(any()))
                .thenReturn(replyByReturnValueConsumer);
    }

    @Test
    public void testRefreshForOneItem() throws Exception {
        when(applicationContext.getBeansWithAnnotation(LineMessageHandler.class))
                .thenReturn(singletonMap("bean", new MessageHandler()));

        // Do
        target.refresh();

        // Verify
        assertThat(target.eventConsumerList).hasSize(1);
        HandlerMethod handlerMethod = target.eventConsumerList.get(0);

        assertThat(handlerMethod.getSupportType())
                .isInstanceOf(Predicate.class);
        assertThat(handlerMethod.getHandler())
                .isInstanceOf(Method.class);
    }

    @Test
    public void testPrioritySelection() throws Exception {
        when(applicationContext.getBeansWithAnnotation(LineMessageHandler.class))
                .thenReturn(ImmutableMap.of("bean", new MessageHandler(),
                                            "anothrer", new AnotherMessageHandler()));

        // Do
        target.refresh();

        // Verify
        assertThat(target.eventConsumerList).hasSize(3);

        // 1st
        assertThat(target.eventConsumerList.get(0).getHandler().getName())
                .isEqualTo("textMessageEventHandler");

        // 2nd
        assertThat(target.eventConsumerList.get(1).getHandler().getName())
                .isEqualTo("generalMessageHandler");

        // 3rd
        assertThat(target.eventConsumerList.get(2).getHandler().getName())
                .isEqualTo("defaultEventHandler");
    }

    @Test
    public void dispatchAndReplyMessageTest() {
        final MessageEvent event = EventTestUtil.createTextMessage("text");

        when(applicationContext.getBeansWithAnnotation(LineMessageHandler.class))
                .thenReturn(singletonMap("bean", new ReplyHandler("Message from Handler method")));

        target.refresh();

        // Do
        target.dispatch(event);

        // Verify
        verify(replyByReturnValueConsumerFactory).createForEvent(event);
        verify(replyByReturnValueConsumer, times(1)).accept(new TextMessage("Message from Handler method"));
    }

    @LineMessageHandler
    public static class MessageHandler {
        @EventMapping
        public void generalMessageHandler(MessageEvent messageEvent) {
        }
    }

    @LineMessageHandler
    public static class AnotherMessageHandler {
        @EventMapping
        public void defaultEventHandler(Event event) {
        }

        @EventMapping
        public void textMessageEventHandler(MessageEvent<TextMessageContent> event) {
        }
    }

    @LineMessageHandler
    @AllArgsConstructor
    public static class ReplyHandler {
        private final String replyMessage;

        @EventMapping
        public TextMessage reply(final ReplyEvent replySupportEvent) {
            return new TextMessage(replyMessage);
        }
    }
}
