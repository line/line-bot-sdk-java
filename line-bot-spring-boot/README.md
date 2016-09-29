# line-bot-spring-boot

This is a spring-boot autoconfigurer for LINE bot API.

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

Then, you can get a parsed messages like following code:

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

You need to use `@LineBotMessages` annotation for getting messages.

## Configuration

LINE bot SDK automatically configured by system properties. There's following parameters:

### line.bot.channelToken

Channel access token for bot server.

### line.bot.channelSecret

Channel secret for bot server.

### line.bot.connectTimeout

Connecting timeout in milli seconds.

### line.bot.readTimeout

Read timeout in milli seconds.

### line.bot.writeTimeout

Write timeout in milli seconds.
