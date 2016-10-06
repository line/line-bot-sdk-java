# The LINE bot SDK for Java

[![Build Status](https://travis-ci.org/line/line-bot-sdk-java.svg?branch=master)](https://travis-ci.org/line/line-bot-sdk-java)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.linecorp.bot/line-bot-model/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.linecorp.bot/line-bot-model)
[![javadoc.io](https://javadocio-badges.herokuapp.com/com.linecorp.bot/line-bot-model/badge.svg)](https://javadocio-badges.herokuapp.com/com.linecorp.bot/line-bot-model)


## What's this?

This is a Java SDK for the LINE Messaging API.

## How do I install this?

We're uploaded this library on maven central. You can install modules via maven or gradle.

http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.linecorp.bot%22

## About LINE Messaging API

Please refer to the official api documents for details.

en:  https://devdocs.line.me/en/

ja:  https://devdocs.line.me/ja/

## Spring Boot Integration

line-bot-spring-boot module provides a way to build your bot apps as a Spring Boot Application.

```java
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

package com.example.bot.spring.echo;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linecorp.bot.client.LineMessagingService;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
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

        @PostMapping("/callback")
        public void callback(@LineBotMessages List<Event> events) throws IOException {
            for (Event event : events) {
                System.out.println("event: " + event);
                if (event instanceof MessageEvent) {
                    MessageContent message = ((MessageEvent) event).getMessage();
                    if (message instanceof TextMessageContent) {
                        System.out.println("Sending reply message");
                        TextMessageContent textMessageContent = (TextMessageContent) message;
                        BotApiResponse apiResponse = lineMessagingService.replyMessage(
                                new ReplyMessage(
                                        ((MessageEvent) event).getReplyToken(),
                                        new TextMessage(textMessageContent.getText()
                                        ))).execute().body();
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

## FAQ

### How do I use proxy server?

You can use `LineMessagingServiceBuilder` to configure a proxy server. It accepts your own OkHttpBuilder instance.

(Note. You don't need to use IP fixing proxy servers like FIXIE to avoid server IP white list. LINE Messaging API no longer denies your API call if you don't put IPs on server IP white list.)

## LICENSE

    Copyright (C) 2016 LINE Corp.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
