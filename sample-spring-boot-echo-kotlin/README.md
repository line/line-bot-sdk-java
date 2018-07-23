# sample-spring-boot-echo-kotlin

sample-spring-boot-echo-kotlin is a tiny sample bot application based on Spring Boot.

## Usage

Run this sample bot using Gradle.

    ../gradlew bootRun -Dline.bot.channelToken=YOUR_CHANNEL_TOKEN \
                       -Dline.bot.channelSecret=YOUR_CHANNEL_SECRET

or if you finished create `src/main/resources/application.yml` file based on `src/main/resources/application-template.yml`. You can start configured web server just hitting

    ../gradlew bootRun

You need to pass the following options.

  * line.bot.channelToken: Your Channel access token
  * line.bot.channelSecret: Your Channel secret

For more information about configuration way, refer [Spring Boot Reference - 24. Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html).
