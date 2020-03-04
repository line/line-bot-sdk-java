# line-bot-api-client

LINE Messaging API client for Java 8 or later.

## Synopsis

```java
LineMessagingClient client = LineMessagingClient.builder("YOUR_CHANNEL_TOKEN").build();
```

## Description

This module provides an API client for the LINE Messaging API.

## Integration test

By default, the integration test suite does nothing. You need to put the configuration file to run the integration test.
If you want to run this test suite, put `src/test/resources/integration_test_settings.yml`.

The YAML file is mapped to `com.linecorp.bot.client.IntegrationTestSettings`.

(It's bit hard to run all test cases since some test cases depend on the number of the targeted reaches).
