# line-bot-spring-boot

This is a Spring Boot auto-configuration for the LINE Messaging API.

## Synopsis

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

## Usage

Add this library as a dependency of your project.

You can then get parsed messages like the following:

```java
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
```

Use the `@LineBotMessages` annotation for getting messages.

## Configuration

The Messaging API SDK is automatically configured by the system properties. The parameters are shown below.

| Parameter | Description |
| ----- | ------ |
| line.bot.channelToken | Channel access token for the server |
| line.bot.channelSecret | Channel secret for the server |
| line.bot.connectTimeout | Connection timeout in milliseconds |
| line.bot.readTimeout | Read timeout in milliseconds |
| line.bot.writeTimeout | Write timeout in milliseconds |
