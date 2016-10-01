package com.linecorp.bot.spring.boot.support;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.function.Predicate;

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
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.spring.boot.annotation.DefaultEventMapping;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.linecorp.bot.spring.boot.annotation.MessageEventMapping;
import com.linecorp.bot.spring.boot.support.LineMessageHandlerSupport.HandlerMethod;

public class LineMessageHandlerSupportTest {
    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ConfigurableApplicationContext applicationContext;

    @InjectMocks
    private LineMessageHandlerSupport target;

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

    @LineMessageHandler
    public static class MessageHandler {
        @EventMapping(MessageEvent.class)
        public void generalMessageHandler(MessageEvent messageEvent) {
        }
    }

    @LineMessageHandler
    public static class AnotherMessageHandler {
        @DefaultEventMapping
        public void defaultEventHandler(Event event) {
        }

        @MessageEventMapping(TextMessageContent.class)
        public void textMessageEventHandler(MessageEvent<TextMessageContent> event) {
        }
    }
}
