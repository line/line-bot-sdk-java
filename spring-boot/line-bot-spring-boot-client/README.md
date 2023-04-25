# line-bot-spring-boot

This is a Spring Boot auto-configuration for the LINE Messaging API.

## Synopsis

```java
package com.example.bot.spring.echo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger log = LoggerFactory.getLogger(EchoApplication.class);
    private final MessagingApiClient messagingApiClient;

    public static void main(String[] args) {
        SpringApplication.run(EchoApplication.class, args);
    }

    public EchoApplication(MessagingApiClient messagingApiClient) {
        this.messagingApiClient = messagingApiClient;
    }

    @EventMapping
    public void handleTextMessageEvent(MessageEvent event) {
        log.info("event: " + event);
        if (event.message() instanceof TextMessageContent) {
            TextMessageContent message = (TextMessageContent) event.message();
            final String originalMessageText = message.text();
            messagingApiClient.replyMessage(new ReplyMessageRequest(
                event.replyToken(),
                List.of(new TextMessage(originalMessageText)),
                false));
        }
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }
}
```

## Usage

Add this library as a dependency of your project.

You can then get parsed messages like the following:

```java
@LineMessageHandler
public class EchoApplication {
    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent event, TextMessageContent content) {
        System.out.println("event: " + event);
        return new TextMessage(content.getText());
    }
}
```

Method with `@EventMapping` on `@LineMessageHandler` annotated class is treated as message event handler.

The event handler method should be only one argument with some type implements `Event`.

When webhook is reached, SDK call decide which method should be called based on the arguments type.

All methods detected by SDK is logged into start up time as follows.

```
c.l.b.s.b.s.LineMessageHandlerSupport    : Mapped "[MessageEvent<TextMessageContent>]" onto public java.util.List<com.linecorp.bot.model.message.TextMessage> com.example.bot.spring.echo.EchoApplication.handleTextMessageEvent(com.linecorp.bot.model.event.MessageEvent<com.linecorp.bot.model.event.message.TextMessageContent>) throws java.lang.Exception
c.l.b.s.b.s.LineMessageHandlerSupport    : Mapped "[Event]" onto public void com.example.bot.spring.echo.EchoApplication.handleDefaultMessageEvent(com.linecorp.bot.model.event.Event)
```

## Arguments handling

Here's the document of the `line-bot-spring-boot`'s argument handling.

### instance of `Event`s.

You can get the event object.

```java
@LineMessageHandler
public class EchoApplication {
    @EventMapping
    public TextMessage handleMessageEvent(MessageEvent event) {
        System.out.println("event: " + event);
        return new TextMessage(content.getText());
    }

    @EventMapping
    public TextMessage handleFollowEvent(FollowEvent event) {
        System.out.println("event: " + event);
        return new TextMessage("Folllowed");
    }
}
```

### instance of `MessageContent`s.

You can get the `MessageContent` object.

```java
@LineMessageHandler
public class EchoApplication {
    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent event, TextMessageContent content) {
        System.out.println("event: " + event);
        return new TextMessage(content.getText());
    }
}
```

### `@LineBotDestination`: Using destination

LINE Messaging API sends `destination` field in
a <a href="https://developers.line.biz/en/reference/messaging-api/#request-body">
request body</a>. You can handle it with `line-bot-spring-boot`.

You can put the String parameter with `@LineBotDestination` as the first parameter of the event handler.
`line-bot-spring-boot` pass the `destination` field as the argument. Then, put the event object as a second
parameter.

```java
@LineMessageHandler
public class EchoApplication {
    @EventMapping
    public TextMessage handleTextMessageEvent(@LineBotDestination String destination,
                                              MessageEvent<TextMessageContent> event) {
        System.out.println("event: " + event);
        return new TextMessage(event.getMessage().getText());
    }
}
```

## Configuration

The Messaging API SDK is automatically configured by the system properties. The parameters are shown below.

| Parameter                          | Description                                                                                                                                                    |
|------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| line.bot.channel-token             | Channel access token for the server                                                                                                                            |
| line.bot.channel-secret            | Channel secret for the server                                                                                                                                  |
| line.bot.channel-token-supply-mode | The way to fix channel access token. (default: `FIXED`)<br>LINE Partners should change this value to `SUPPLIER` and create custom `ChannelTokenSupplier` bean. |
| line.bot.connect-timeout           | Connection timeout in milliseconds                                                                                                                             |
| line.bot.read-timeout              | Read timeout in milliseconds                                                                                                                                   |
| line.bot.write-timeout             | Write timeout in milliseconds                                                                                                                                  |
| line.bot.handler.enabled           | Enable @EventMapping mechanism. (default: true)                                                                                                                |
| line.bot.handler.path              | Path to waiting webhook. (default: `/callback`)                                                                                                                |
