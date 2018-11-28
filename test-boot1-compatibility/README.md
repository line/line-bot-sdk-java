# test-boot1-compatibility

Runs all test under SpringBoot 1.x (Spring 4.x) environment.

See build.gradle for details.

## How to use line-bot-spring-boot under SpringBoot 1.x (Spring 4.x)

If you want to use `line-bot-spring-boot` under SpringBoot 1.x, 
since `line-bot-spring-boot` depends on SpringBoot 2.x by default, 
you need to enforce to downgrade all SpringBoot libraries to 1.x (and Spring libraries from 5.x to 4.x).

Example by `plugin: 'io.spring.dependency-management'` is follows.

```groovy:build.gradle
apply plugin: 'io.spring.dependency-management'


dependencyManagement {
    imports {
        mavenBom 'io.spring.platform:platform-bom:Brussels-SR13'
    }
}

dependencies {
    compile 'com.linecorp.bot:line-bot-spring-boot:2.1.0'
}
```
