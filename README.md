# LINE Messaging API SDK for Java

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.linecorp.bot/line-bot-messaging-api-client/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.linecorp.bot/line-bot-messaging-api-client)
[![javadoc](https://javadoc.io/badge2/com.linecorp.bot/line-bot-parser/javadoc.svg)](https://javadoc.io/doc/com.linecorp.bot/line-bot-parser)

## Introduction

The LINE Messaging API SDK for Java makes it easy to develop bots using LINE Messaging API, and you can create a sample
bot within minutes.

## Documentation

See the official API documentation for more information.

- English: https://developers.line.biz/en/docs/messaging-api/overview/
- Japanese: https://developers.line.biz/ja/docs/messaging-api/overview/

## Requirements

This library requires Java 17 or later.

## Installation

We've uploaded this library to the Maven Central Repository. You can install the modules using Maven or Gradle.

https://central.sonatype.com/search?smo=true&q=com.linecorp.bot

### Gradle (Kotlin) example

```kotlin
implementation("com.linecorp.bot:line-bot-messaging-api-client:<VERSION>")
implementation("com.linecorp.bot:line-bot-insight-client:<VERSION>")
implementation("com.linecorp.bot:line-bot-manage-audience-client:<VERSION>")
implementation("com.linecorp.bot:line-bot-module-attach-client:<VERSION>")
implementation("com.linecorp.bot:line-bot-module-client:<VERSION>")
implementation("com.linecorp.bot:line-bot-shop-client:<VERSION>")
implementation("com.linecorp.bot:line-channel-access-token-client:<VERSION>")
implementation("com.linecorp.bot:line-liff-client:<VERSION>")

implementation("com.linecorp.bot:line-bot-webhook:<VERSION>")
implementation("com.linecorp.bot:line-bot-parser:<VERSION>") // You don't need to depend on this explicitly.

implementation("com.linecorp.bot:line-bot-spring-boot-webmvc:<VERSION>")
implementation("com.linecorp.bot:line-bot-spring-boot-client:<VERSION>") // If you want to write spring-boot API client
implementation("com.linecorp.bot:line-bot-spring-boot-handler:<VERSION>") // You don't need to depend on this explicitly.
implementation("com.linecorp.bot:line-bot-spring-boot-web:<VERSION>") // You don't need to depend on this explicitly.
```

## Sample code

This project contains the following sample projects:

* [sample-spring-boot-echo](samples/sample-spring-boot-echo): A simple echo server.
* [sample-spring-boot-kitchensink](samples/sample-spring-boot-kitchensink): Full featured sample code.
* [sample-spring-boot-echo-kotlin](samples/sample-spring-boot-echo-kotlin): A simple echo server written in Kotlin.
* [sample-manage-audience](samples/sample-manage-audience): A sample code for Manage Audience API.

## Spring Boot integration

The line-bot-spring-boot module lets you build a bot application as a Spring Boot application.

```java
package com.example.bot.spring.echo;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.linecorp.bot.messaging.client.MessagingApiClient;
import com.linecorp.bot.messaging.model.ReplyMessageRequest;
import com.linecorp.bot.messaging.model.TextMessage;
import com.linecorp.bot.spring.boot.handler.annotation.EventMapping;
import com.linecorp.bot.spring.boot.handler.annotation.LineMessageHandler;
import com.linecorp.bot.webhook.model.Event;
import com.linecorp.bot.webhook.model.MessageEvent;
import com.linecorp.bot.webhook.model.TextMessageContent;

@SpringBootApplication
@LineMessageHandler
public class EchoApplication {
    private final MessagingApiClient messagingApiClient;

    public static void main(String[] args) {
        SpringApplication.run(EchoApplication.class, args);
    }

    public EchoApplication(MessagingApiClient messagingApiClient) {
        this.messagingApiClient = messagingApiClient;
    }

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        System.out.println("event: " + event);
        final String originalMessageText = ((TextMessageContent) event.message()).text();
        messagingApiClient.replyMessage(
            new ReplyMessageRequest.Builder(event.replyToken(), List.of(new TextMessage(originalMessageText)))
                .build()
        );
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }
}
```

## How do I use a proxy server?

You can use a proxy with this module.

```java
api = MessagingApiClient.builder("MY_OWN_TOKEN")
        .apiEndPoint(URI.create("https://api.line.me/"))
        .proxy(new Proxy(Proxy.Type.HTTP,
                new InetSocketAddress("proxy.example.com", 8080)
        ))
        .build();
```
Note: You don't need to use an add-on like Fixie to have static IP addresses for proxy servers. You can make API calls
without entering IP addresses on the server IP whitelist.

## How to get x-line-request-id header and error message

You may need to store the x-line-request-id header obtained as a response from several APIs. In this case, you can get it from `Result<T>`.
```java
Result<Object> apiResponse = messagingApiClient
    .narrowcast(retryKey, new NarrowcastRequest.Builder(messages).build())
    .get();
System.out.println("x-line-request-id: " + apiResponse.requestId());
```

You can get error messages from `MessagingApiClientException` when you use `MessagingApiClient`. Each client defines its own exception class.
```java
try {
    messagingApiClient.replyMessage(new ReplyMessage(replyToken, messages));
} catch (ExecutionException e) {
    if (e.getCause() instanceof MessagingApiClientException){
        MessagingApiClientException exception=(MessagingApiClientException)e.getCause();
        System.out.println("Error http status code: " + exception.getCode());
        System.out.println("Error response: " + exception.getDetails());
        System.out.println("Error message: " + exception.getMessage());
    }
}
```
When you need to get `x-line-accepted-request-id` header from error response, you can get it: `exception.getHeader("x-line-accepted-request-id")`.

## Help and media

FAQ: https://developers.line.biz/en/faq/

News: https://developers.line.biz/en/news/

## Versioning

This project respects semantic versioning.
- See https://semver.org/

However, if a feature that was publicly released is discontinued for business reasons and becomes completely unusable, we will release changes as a patch release.

## From version 7.x

LINE's SDK developer team decides to generate SDK code based on OpenAPI spec.
As a result, LINE bot sdk 7.x is not compatible with 6.x. But it can follow the future API changes very quickly.

- `line-bot-model` and `line-bot-api-client` are splitted to `line-bot-webhook` and `clients/` modules
- `line-bot-servlet` is no longer supported.
- `line-bot-cli` is no longer supported.
- `line-bot-spring-boot` was splitted.
    - Splitted to following modules.
        - `line-bot-spring-boot-client` is a client bean configuration module.
            - If you want to write spring-boot API client,
        - `line-bot-spring-boot-handler` is a handler configuration.
            - You don't need to depend this explicitly.
        - `line-bot-spring-boot-web` is a spring-web binding.
            - You don't need to depend this explicitly.
        - `line-bot-spring-boot-webmvc` is a spring-webmvc binding.
            - **usually, you want to depend on this module.**

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
