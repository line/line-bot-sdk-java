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

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linecorp.bot.spring.boot.handler.annotation.EventMapping;
import com.linecorp.bot.spring.boot.handler.annotation.LineMessageHandler;
import com.linecorp.bot.spring.boot.handler.properties.HandlerProperties;
import com.linecorp.bot.spring.boot.web.argument.annotation.LineBotDestination;
import com.linecorp.bot.spring.boot.web.argument.annotation.LineBotMessages;
import com.linecorp.bot.webhook.model.Event;

/**
 * Dispatcher for LINE Message Event Handling.
 *
 * <p>Dispatch target method is collected by Spring's bean.
 *
 * <p>Event endpoint is configurable by {@code line.bot.callback-path} parameter.
 *
 * <h2>Event Handler method rule.</h2>
 *
 * <p>The class and method with following rules are collected by LINE Messaging Event handler.
 *
 * <ul>
 * <li>Class annotated with {@link LineMessageHandler}</li>
 * <li>Method annotated with {@link EventMapping}.</li>
 * </ul>
 */
@RestController
@Import(ReplyByReturnValueConsumer.Factory.class)
@ConditionalOnProperty(name = "line.bot.handler.enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(HandlerProperties.class)
public class LineMessageHandlerSupport {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(LineMessageHandlerSupport.class);
    private final ReplyByReturnValueConsumer.Factory returnValueConsumerFactory;
    private final ConfigurableApplicationContext applicationContext;
    private final HandlerMethodScanner handlerMethodScanner = new HandlerMethodScanner();

    volatile List<HandlerMethod> handlerMethods;

    public LineMessageHandlerSupport(
            final ReplyByReturnValueConsumer.Factory returnValueConsumerFactory,
            final ConfigurableApplicationContext applicationContext) {
        this.returnValueConsumerFactory = returnValueConsumerFactory;
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void initialize() {
        applicationContext.addApplicationListener(event -> {
            if (event instanceof ContextRefreshedEvent) {
                refresh();
            }
        });
    }

    // VisibleForTesting
    void refresh() {
        final Collection<Object> handlerBeans =
                applicationContext.getBeansWithAnnotation(LineMessageHandler.class)
                        .values();

        List<HandlerMethod> handlerMethods = handlerMethodScanner.scan(handlerBeans);

        log.info("Registered LINE Messaging API event handler: count = {}", handlerMethods.size());
        handlerMethods.forEach(item -> log.info("Mapped \"{}\" onto {}",
                item.argumentResolvers(), item.handler().toGenericString()));

        this.handlerMethods = handlerMethods;
    }

    @PostMapping("${line.bot.handler.path:/callback}")
    public void callback(@LineBotDestination String destination, @LineBotMessages List<Event> events) {
        events.forEach(event -> {
            this.dispatch(destination, event);
        });
    }

    // VisibleForTesting
    void dispatch(String destination, Event event) {
        try {
            dispatchInternal(destination, event);
        } catch (InvocationTargetException e) {
            log.error("InvocationTargetException occurred.", e);
        } catch (Error | Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void dispatchInternal(final String destination, final Event event) throws Exception {
        final HandlerMethod handlerMethod = handlerMethods
                .stream()
                .filter(handler -> handler.isSupported(event))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("Unsupported event type. " + event));
        final Object returnValue = handlerMethod.invoke(destination, event);

        handleReturnValue(event, returnValue);
    }

    private void handleReturnValue(final Event event, final Object returnValue) {
        if (returnValue != null) {
            returnValueConsumerFactory.createForEvent(event)
                    .accept(returnValue);
        }
    }
}
