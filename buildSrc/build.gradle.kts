/*
 * Copyright (c) 2023 LINE Corporation. All rights reserved.
 * LINE Corporation PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

plugins {
    `kotlin-dsl`
}

dependencies {
    // dependency for LineJavaCodegenGenerator
    implementation("org.openapitools:openapi-generator:7.9.0")

    // plugins
    implementation("io.github.gradle-nexus:publish-plugin:2.0.0")
    implementation("org.openapitools:openapi-generator-gradle-plugin:7.9.0")
    implementation("com.github.spotbugs.snom:spotbugs-gradle-plugin:6.0.26")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:3.3.5")

    // workaround to use libs in a precompiled script plugin.
    // https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
