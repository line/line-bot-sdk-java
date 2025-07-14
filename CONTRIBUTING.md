# How to contribute to LINE Bot SDK for Java project

First of all, thank you so much for taking your time to contribute! LINE Bot SDK for Java is not very different from any other open
source projects you are aware of. It will be amazing if you could help us by doing any of the following:

- File an issue in [the issue tracker](https://github.com/line/line-bot-sdk-java/issues) to report bugs and propose new features and
  improvements.
- Ask a question using [the issue tracker](https://github.com/line/line-bot-sdk-java/issues).
- Contribute your work by sending [a pull request](https://github.com/line/line-bot-sdk-java/pulls).

## Development

### Install dependencies

This project uses Gradle as the build system. Make sure you have JDK installed (JDK 17 or later is recommended).

```bash
./gradlew build
```

This will download all dependencies and build the project.

### Understand the project structure

The project structure is as follows:
- `clients`: The main client libraries.
- `line-bot-webhook`: Webhook handling code.
- `line-bot-parser`: Parser utilities.
- `line-bot-jackson`: Jackson customizations for the SDK.
- `generator`: Code generator for the libraries.
- `samples`: Example projects that use the library.
- `tests`: Test utilities.
- `spring-boot`: Spring Boot integration components.

### Edit pebble template

Almost all code is generated with [pebble templates](https://pebbletemplates.io/), based on [line-openapi](https://github.com/line/line-openapi)'s YAML files.
Thus, you can't edit the source code under the `clients` and `line-bot-webhook` directory directly.

You need to edit the pebble templates under [generator/src/main/resources/line-java-codegen](generator/src/main/resources/line-java-codegen) instead.

After editing the templates, run `python generate-code.py` to generate the code, and then commit all affected files.
If not, CI status will be red.

### Add unit tests

We use JUnit for unit testing. Please add tests to verify your changes continuously.

Especially for bug fixes, please follow this flow for testing and development:
1. Write a test before making changes to the library and confirm that the test fails.
2. Modify the code of the library.
3. Run the test again and confirm that it passes thanks to your changes.

### Run your code in your local

The [sample projects](samples/) can be used to test your changes locally. You can run them with Gradle:

```bash
./gradlew :samples:sample-spring-boot-echo:bootRun
```

### Run all CI tasks locally

You can run all CI tasks locally by running:

```bash
./gradlew check
```

#### Documentation

We use JavaDoc to generate and maintain our code documentation.
Run the following command to generate JavaDoc:

```bash
./gradlew javadoc
```

**Please make sure your new or modified code is also covered by proper JavaDoc comments.**
Good documentation ensures that contributors and users can easily read and understand how the methods and classes work.

### Contributor license agreement

When you are sending a pull request and it's a non-trivial change beyond fixing typos, please make sure to sign
[the ICLA (individual contributor license agreement)](https://cla-assistant.io/line/line-bot-sdk-java). Please
[contact us](mailto:dl_oss_dev@linecorp.com) if you need the CCLA (corporate contributor license agreement).
