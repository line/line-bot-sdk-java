# line-bot-integration-test

This module contains integration test for LINE Messaging API.

## Configuration

By default, this repository do nothing. You need to put the configuration file to run the integration test.
If you want to run this test suite, put `src/test/resources/integration_test_settings.yml`.

The YAML file is mapped to `com.linecorp.bot.client.IntegrationTestSettings`.

(It's bit hard to run all test cases since some test cases depends on the number of the targeted reaches).
