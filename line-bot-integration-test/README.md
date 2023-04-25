# line-bot-integration-test

Integration test for `LINE Bot SDK for Java`.

## How do I run this component?

By default, the integration test suite does nothing. You need to put the configuration file to run the integration test.
If you want to run this test suite, put `src/test/resources/integration_test_settings.yml`.

The YAML file is mapped to `com.linecorp.bot.client.utils.IntegrationTestSettings`.

(It's bit hard to run all test cases since some test cases depend on the number of the targeted reaches).
