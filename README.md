# The LINE bot SDK for Java

[![Build Status](https://travis-ci.org/line/line-bot-sdk-java.svg?branch=master)](https://travis-ci.org/line/line-bot-sdk-java)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.linecorp.bot/line-bot-model/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.linecorp.bot/line-bot-model)
[![javadoc.io](https://javadocio-badges.herokuapp.com/com.linecorp.bot/line-bot-model/badge.svg)](https://javadocio-badges.herokuapp.com/com.linecorp.bot/line-bot-model)


## What's this?

This is a client library for the LINE Bot API.

## About LINE Bot API

Please refer to the official api documents for details.

en: http://line.github.io/line-bot-api-doc/en/

ja: http://line.github.io/line-bot-api-doc/ja/

## Create a LINE Bot client

The main entry point is `LineBotClient`. You can create an instance via `LineBotClientBuilder`.

```
 LineBotClient client = LineBotClientBuilder
                          .create("YOUR_CHANNEL_ID", "YOUR_CHANNEL_SECRET", "YOUR_CHANNEL_MID")
                          .build();
```

## Client usage

You can use `LineBotClient` to receive/send events from/to your followers.
The following sketch shows a naive echo bot example.

```
String requestBody = yourWebFramework.getRequestbody();
String signature = yourWebFramework.getRequestHeader(LineBotAPIHeaders.X_LINE_CHANNEL_SIGNATURE);

// parsing callback request
CallbackRequest callbackRequest = client.readCallbackRequest(requestBody);

// signature validation
if (!client.validateSignature(requestBody, signature)) {
    log.error(...);
    return;
}

// processing received events
for (Event event : callbackRequest.getResult()) {
    Content content = event.getContent();

    // handle text message
    if (content instanceof TextContent) {
        TextContent text = (TextContent) content;
        // reply back same text
        client.sendText(text.getFrom(), text.getText());
    }
}

```

## Spring Boot Integration

line-bot-spring-boot provides a way to build your bot apps as a Spring Boot Application.

```
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
                    TextContent text = (TextContent) content;
                    lineBotClient.sendText(text.getFrom(), text.getText());
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
