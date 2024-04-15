# sample-spring-boot-echo

sample-spring-boot-echo is a tiny sample bot application based on Spring Boot.

## Usage

Run this sample bot using Gradle.

    ../gradlew bootRun -Dline.bot.channelToken= 2004599221\
                       -Dline.bot.channelSecret=2f5f7dd93bb415c749927c0235150ea8

or if you finished create `src/main/resources/application.yml` file based on `src/main/resources/application-template.yml`. You can start configured web server just hitting

    ../gradlew bootRun

You need to pass the following options.

  * line.bot.channelToken: Your Channel access token
  * line.bot.channelSecret: Your Channel secret

For more information about configuration way, refer [Spring Boot Reference - 24. Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html).

