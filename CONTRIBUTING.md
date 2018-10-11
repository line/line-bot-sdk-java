## How to contribute to LINE Bot SDK for Java project

First of all, thank you so much for taking your time to contribute! LINE Bot SDK for Java is not very different from any other open
source projects you are aware of. It will be amazing if you could help us by doing any of the following:

- File an issue in [the issue tracker](https://github.com/line/line-bot-sdk-java/issues) to report bugs and propose new features and
  improvements.
- Ask a question using [the issue tracker](https://github.com/line/line-bot-sdk-java/issues).
- Contribute your work by sending [a pull request](https://github.com/line/line-bot-sdk-java/pulls).

### Contributor license agreement

When you are sending a pull request and it's a non-trivial change beyond fixing typos, please make sure to sign
[the ICLA (individual contributor license agreement)](https://cla-assistant.io/line/line-bot-sdk-java). Please
[contact us](mailto:dl_oss_dev@linecorp.com) if you need the CCLA (corporate contributor license agreement).

# Detect outdated dependencies

Run `./gradlew dependencyUpdates -Drevision=release` checks dependency list 
and reports outdated dependencies excepts SpringManaged dependency.

## ./gradlew dependencyUpdates example
```
% ./gradlew dependencyUpdates -Drevision=release
Download https://plugins.gradle.org/m2/org/springframework/boot/spring-boot-gradle-plugin/maven-metadata.xml

> Task :dependencyUpdates

------------------------------------------------------------
: Project Dependency Updates (report to plain text file)
------------------------------------------------------------

The following dependencies are using the latest release version:
 - com.github.ben-manes:gradle-versions-plugin:0.20.0
 - com.github.stefanbirkner:system-rules:1.18.0
 - com.squareup.okhttp3:logging-interceptor:3.11.0
 - com.squareup.okhttp3:mockwebserver:3.11.0
 - com.squareup.retrofit2:converter-jackson:2.4.0
 - com.squareup.retrofit2:retrofit:2.4.0
 - io.franzbecker:gradle-lombok:1.14
 - io.spring.gradle:dependency-management-plugin:1.0.6.RELEASE
 - org.projectlombok:lombok:1.18.2
 - org.slf4j:slf4j-api:1.7.25

The following dependencies have later release versions:
 - com.google.guava:guava [25.1-jre -> 26.0-jre]
     https://github.com/google/guava
 - gradle.plugin.com.github.spotbugs:spotbugs-gradle-plugin [1.6.2 -> 1.6.3]
 - gradle.plugin.com.gorylenko.gradle-git-properties:gradle-git-properties [1.4.17 -> 1.5.2]
 - io.spring.gradle:propdeps-plugin [0.0.9.RELEASE -> 0.0.10.RELEASE]
 - org.jetbrains.kotlin:kotlin-gradle-plugin [1.2.61 -> 1.2.70]
     https://kotlinlang.org/
 - org.jetbrains.kotlin:kotlin-scripting-compiler-embeddable [1.2.61 -> 1.2.70]
     https://kotlinlang.org/
 - org.jetbrains.kotlin:kotlin-stdlib-jdk8 [1.2.61 -> 1.2.70]
     https://kotlinlang.org/

Failed to determine the latest version for the following dependencies (use --info for details):
 - com.fasterxml.jackson.core:jackson-annotations
 - com.fasterxml.jackson.core:jackson-core
 - com.fasterxml.jackson.core:jackson-databind
 - com.fasterxml.jackson.datatype:jackson-datatype-jsr310
 - com.fasterxml.jackson.module:jackson-module-parameter-names
 - javax.servlet:javax.servlet-api
 - javax.validation:validation-api
 - org.hibernate:hibernate-validator
 - org.springframework.boot:spring-boot-autoconfigure
 - org.springframework.boot:spring-boot-configuration-processor
 - org.springframework.boot:spring-boot-gradle-plugin
 - org.springframework.boot:spring-boot-starter-logging
 - org.springframework.boot:spring-boot-starter-test
 - org.springframework.boot:spring-boot-starter-web

Gradle updates:
 - Gradle: [4.8 -> 4.10.1]

Generated report file build/dependencyUpdates/report.txt
```

