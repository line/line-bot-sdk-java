# LINE Messaging API SDK for Java

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.linecorp.bot/line-bot-model/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.linecorp.bot/line-bot-model)
[![javadoc](https://javadoc.io/badge2/com.linecorp.bot/line-bot-model/javadoc.svg)](https://javadoc.io/doc/com.linecorp.bot/line-bot-model)
[![codecov](https://codecov.io/gh/line/line-bot-sdk-java/branch/master/graph/badge.svg)](https://codecov.io/gh/line/line-bot-sdk-java)


## Introduction

The LINE Messaging API SDK for Java makes it easy to develop bots using LINE Messaging API, and you can create a sample bot within minutes.


## Documentation

See the official API documentation for more information.

- English: https://developers.line.biz/en/docs/messaging-api/overview/
- Japanese: https://developers.line.biz/ja/docs/messaging-api/overview/


## Requirements

This library requires Java 8 or later.


## Installation

We've uploaded this library to the Maven Central Repository. You can install the modules using Maven or Gradle.

http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.linecorp.bot%22


## Modules

This project contains the following modules:

 * line-bot-api-client: API client library for the Messaging API
 * line-bot-model: Model classes for the Messaging API
 * line-bot-servlet: Java servlet utilities for bot servers
 * line-bot-spring-boot: Spring Boot auto configuration library for bot servers

This project contains the following sample projects:

 * sample-spring-boot-echo: A simple echo server. It includes a Heroku button.
 * sample-spring-boot-kitchensink: Full featured sample code.


## Spring Boot integration

The line-bot-spring-boot module lets you build a bot application as a Spring Boot application.

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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@SpringBootApplication
@LineMessageHandler
public class EchoApplication {
    public static void main(String[] args) {
        SpringApplication.run(EchoApplication.class, args);
    }

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        System.out.println("event: " + event);
        return new TextMessage(event.getMessage().getText());
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }
}
```

## How do I use a proxy server?

You can use `LineMessagingServiceBuilder` to configure a proxy server. It accepts your own OkHttpBuilder instance.

Note: You don't need to use an add-on like Fixie to have static IP addresses for proxy servers. You can make API calls without entering IP addresses on the server IP whitelist.


## Help and media
FAQ: https://developers.line.biz/en/faq/

Community Q&A: https://www.line-community.me/questions

News: https://developers.line.biz/en/news/

Twitter: [@LINE_DEV](https://twitter.com/LINE_DEV)


## Versioning

This project respects semantic versioning.

See http://semver.org/.


## Contributing

Please check [CONTRIBUTING](CONTRIBUTING.md) before making a contribution.


## License

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
