/*
 * Copyright 2023 LINE Corporation
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

plugins {
    id("sdk.java-library")
}

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
    implementation(libs.slf4j.api)
    implementation(libs.okhttp3.logging.interceptor)
    implementation(libs.bundles.retrofit2)

    testImplementation(libs.wiremock)
    testImplementation(libs.bundles.tests)
    testImplementation(libs.jul.to.slf4j)

    integrationTestImplementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    integrationTestImplementation(project(":clients:line-channel-access-token-client"))
    integrationTestImplementation(project(":clients:line-bot-messaging-api-client"))
    integrationTestImplementation(project(":clients:line-bot-manage-audience-client"))
    integrationTestImplementation(project(":clients:line-bot-module-attach-client"))
    integrationTestImplementation(project(":clients:line-bot-insight-client"))
    integrationTestImplementation(project(":line-bot-jackson"))
    integrationTestImplementation(libs.jjwt.api)
    integrationTestImplementation(libs.jjwt.jackson)
    // https://github.com/jwtk/jjwt/issues/236 jjwt doens't support JWK parsing, yet.
    // Once jjwt support JWK, we can remove this dependency.
    integrationTestImplementation("com.nimbusds:nimbus-jose-jwt:9.39.3")
    integrationTestImplementation("org.springframework.boot:spring-boot-starter-test")
    integrationTestImplementation("org.springframework.boot:spring-boot-starter-logging")
    integrationTestImplementation("com.fasterxml.jackson.core:jackson-core")
    integrationTestImplementation("com.fasterxml.jackson.core:jackson-databind")
    integrationTestImplementation("com.fasterxml.jackson.core:jackson-annotations")
    integrationTestImplementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
    integrationTestImplementation(project(":line-bot-jackson"))
    integrationTestRuntimeOnly(libs.jjwt.impl)
}
