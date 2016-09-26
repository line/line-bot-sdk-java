# The LINE bot SDK for Java

[![Build Status](https://travis-ci.org/line/line-bot-sdk-java.svg?branch=master)](https://travis-ci.org/line/line-bot-sdk-java)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.linecorp.bot/line-bot-model/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.linecorp.bot/line-bot-model)
[![javadoc.io](https://javadocio-badges.herokuapp.com/com.linecorp.bot/line-bot-model/badge.svg)](https://javadocio-badges.herokuapp.com/com.linecorp.bot/line-bot-model)


## What's this?

This is a Java SDK for the LINE Messaging API.

## About LINE Messaging API

Please refer to the official api documents for details.

en:  https://devdocs.line.me/en/

ja:  https://devdocs.line.me/ja/

## Spring Boot Integration

line-bot-spring-boot module provides a way to build your bot apps as a Spring Boot Application.

```java
package com.example.bot.spring.echo;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linecorp.bot.client.LineMessagingService;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.LineBotMessages;

@SpringBootApplication
public class EchoApplication {
    public static void main(String[] args) {
        SpringApplication.run(EchoApplication.class, args);
    }

    @RestController
    public static class MyController {
        @Autowired
        private LineMessagingService lineMessagingService;

        /**
         * @param events received webhook event
         */
        @PostMapping("/callback")
        public void callback(@LineBotMessages List<Event> events) throws IOException {
            for (Event event : events) {
                System.out.println("event: " + event);
                if (event instanceof MessageEvent) {
                    MessageContent message = ((MessageEvent) event).getMessage();
                    if (message instanceof TextMessageContent) {
                        System.out.println("Sending reply message");
                        TextMessageContent textMessageContent = (TextMessageContent) message;
                        Source source = event.getSource();
                        String mid = source instanceof GroupSource
                                     ? ((GroupSource) source).getGroupId()
                                     : source.getUserId();
                        BotApiResponse apiResponse = lineMessagingService.push(
                                new PushMessage(
                                        mid,
                                        new TextMessage(textMessageContent.getText()
                                        ))).execute()
                                                                         .body();
                        System.out.println("Sent messages: " + apiResponse);
                    }
                }
            }
        }
    }
}
```

## Modules

This project contains following modules:

 * line-bot-api-client: API client library for LINE Bot API
 * line-bot-model: Model classes for LINE Bot API
 * line-bot-servlet: Java servlet utilities for Bot servers
 * line-bot-spring-boot: spring-boot's auto configuration library for Bot servers

There's some sample projects:

 * sample-spring-boot-echo: Simple echo server.
 * sample-spring-boot-kitchensink: Full featured sample code.

## Requirements

This library requires Java 8 or later.

## Versioning

This project respects semantic versioning.

See http://semver.org/.

## LICENSE

See LICENSE.txt
