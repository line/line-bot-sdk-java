/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

// https://docs.gradle.org/current/userguide/java_testing.html#sec:configuring_java_integration_tests
sourceSets {
    create("integrationTest") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

val integrationTestImplementation by configurations.getting {
    extendsFrom(configurations.implementation.get())
}
val integrationTestCompileOnly by configurations.getting {
    extendsFrom(configurations.compileOnly.get())
}
val integrationTestAnnotationProcessor by configurations.getting {
    extendsFrom(configurations.annotationProcessor.get())
}
val integrationTestRuntimeOnly by configurations.getting {
    extendsFrom(configurations.runtimeOnly.get())
}

val integrationTest = task<Test>("integrationTest") {
    description = "Runs integration tests."
    group = "verification"

    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
    shouldRunAfter("test")
}

tasks.check { dependsOn(integrationTest) }

dependencies {
    api(project(":line-bot-model"))
    implementation("org.slf4j:slf4j-api")
    implementation("com.squareup.okhttp3:logging-interceptor")
    implementation("com.squareup.retrofit2:converter-jackson")
    implementation("com.squareup.retrofit2:retrofit")

    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    testImplementation("com.github.stefanbirkner:system-lambda")
    testImplementation("com.github.tomakehurst:wiremock-jre8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    integrationTestCompileOnly("org.projectlombok:lombok")
    integrationTestAnnotationProcessor("org.projectlombok:lombok")
    integrationTestImplementation("com.google.guava:guava")
    integrationTestImplementation("io.jsonwebtoken:jjwt-api")
    integrationTestImplementation("io.jsonwebtoken:jjwt-jackson")
    // https://github.com/jwtk/jjwt/issues/236 jjwt doens't support JWK parsing, yet.
    // Once jjwt support JWK, we can remove this dependency.
    integrationTestImplementation("com.nimbusds:nimbus-jose-jwt:9.23")
    integrationTestImplementation("org.springframework.boot:spring-boot-starter-test")
    integrationTestImplementation("org.springframework.boot:spring-boot-starter-logging")
    integrationTestImplementation("com.fasterxml.jackson.core:jackson-core")
    integrationTestImplementation("com.fasterxml.jackson.core:jackson-databind")
    integrationTestImplementation("com.fasterxml.jackson.core:jackson-annotations")
    integrationTestImplementation("com.fasterxml.jackson.module:jackson-module-parameter-names")
    integrationTestImplementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
    integrationTestImplementation("org.junit.vintage:junit-vintage-engine") {
        exclude(group = "org.hamcrest", module = "hamcrest-core")
    }
    integrationTestRuntimeOnly("io.jsonwebtoken:jjwt-impl")
}
