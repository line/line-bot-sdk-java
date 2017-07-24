# 2017F COMP3111/3111H Software Engineering Project github 

This is actually part of the course website of Hong Kong University of Science and Technology (HKUST) software enginnering course. 

# The Messaging API SDK for Java

[![Build Status](https://travis-ci.org/line/line-bot-sdk-java.svg?branch=master)](https://travis-ci.org/line/line-bot-sdk-java)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.linecorp.bot/line-bot-model/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.linecorp.bot/line-bot-model)
[![javadoc.io](https://javadocio-badges.herokuapp.com/com.linecorp.bot/line-bot-model/badge.svg)](https://javadocio-badges.herokuapp.com/com.linecorp.bot/line-bot-model)


## What is this?

This is a repository forked from https://github.com/line/line-bot-sdk-java. You should use this as a starting point to complete your lab.


# Labs Schedule

| Lab | Week | Topic | Type |
|-----|------|-------|------|
| 1   | 1-2  | [Chatbot Deployment](./lab1.md) |  Individual |
| 2   | 3    | [Fixing a bug](./lab2.md)       |  Individual |
| 3   | 4    | [Building a database](./lab3.md)|  Group      |


## How should I read this?

There are several folders in this repository labeled as [Lab 1](./lab1), [Lab2], or [Project]. Click inside each folder and read the README.md file. This files specifics all the requirements. 

Because this project requires quite a lot of unseen technologies you may need to learn them offline. There is a folder called [Tutorials] which contains some background information that you may want to read. Shall there be any problem in reading those documents, please consult the TA Kevin Wang via (khwang0).

## How do I download it?

If you are familiar with github, straight forward, just fork it.



## How do I install it?

We've uploaded this library to the Maven Central Repository. You can install the modules using Maven or Gradle.

http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.linecorp.bot%22

## About the LINE Messaging API

See the official API documentation for more information.

English: https://devdocs.line.me/en/
Japanese: https://devdocs.line.me/ja/

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

## Modules

This project contains the following modules:

 * line-bot-api-client: API client library for the Messaging API
 * line-bot-model: Model classes for the Messaging API
 * line-bot-servlet: Java servlet utilities for bot servers
 * line-bot-spring-boot: Spring Boot auto configuration library for bot servers

This project contains the following sample projects:

 * sample-spring-boot-echo: A simple echo server. It includes a Heroku button.
 * sample-spring-boot-kitchensink: Full featured sample code.

## Requirements

This library requires Java 8 or later.

## Versioning

This project respects semantic versioning.

See http://semver.org/.



## FAQ

### How to skip task in STS

https://stackoverflow.com/questions/25145162/running-gradle-build-from-eclipse-without-test

### How do I use a proxy server?

You can use `LineMessagingServiceBuilder` to configure a proxy server. It accepts your own OkHttpBuilder instance.

Note: You don't need to use an add-on like Fixie to have static IP addresses for proxy servers. You can make API calls without entering IP addresses on the server IP whitelist.

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
