# line-bot-spring-boot

This is a spring-boot autoconfigurer for LINE bot API.

## Synopsis

    package com.example.bot.spring.echo;
    
    import java.util.List;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    import com.linecorp.bot.client.LineBotClient;
    import com.linecorp.bot.client.exception.LineBotAPIException;
    import com.linecorp.bot.model.deprecated.callback.Event;
    import com.linecorp.bot.model.deprecated.content.Content;
    import com.linecorp.bot.model.deprecated.content.TextContent;
    import com.linecorp.bot.spring.boot.annotation.LineBotMessages;
    
    @SpringBootApplication
    public class EchoApplication {
        public static void main(String[] args) {
            SpringApplication.run(EchoApplication.class, args);
        }
    
        @RestController
        public static class MyController {
            @Autowired
            private LineBotClient lineBotClient;
    
            @RequestMapping("/callback")
            public void callback(@LineBotMessages List<Event> events) throws LineBotAPIException {
                for (Event event : events) {
                    Content content = event.getContent();
                    if (content instanceof TextContent) {
                        TextContent textContent = (TextContent) content;
                        lineBotClient.sendText(textContent.getFrom(),
                                               textContent.getText());
                    }
                }
            }
        }
    }

## Usage

Add this library as a dependency of your project.

Then, you can get a parsed messages like following code:

    @RequestMapping("/callback")
    public void callback(@LineBotMessages List<Event> events) throws LineBotAPIException {
        for (Event event : events) {
            Content content = event.getContent();
            if (content instanceof TextContent) {
                TextContent textContent = (TextContent) content;
                lineBotClient.sendText(textContent.getFrom(),
                                       textContent.getText());
            }
        }
    }

You need to use `@LineBotMessages` annotation for getting messages.

## Configuration

LINE bot SDK automatically configured by system properties. There's following parameters:

### line.bot.channelMid

Channel MID for bot server.

### line.bot.channelId

Channel ID for bot server.

### line.bot.channelSecret

Channel secret for bot server.

### line.bot.connectTimeout

Connecting timeout in milli seconds.

### line.bot.connectionRequestTimeout

Connection request timeout in milli seconds.

### line.bot.socketTimeout

Connecting timeout in milli seconds.
