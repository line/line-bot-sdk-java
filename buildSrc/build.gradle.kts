/*
 * Copyright (c) 2023 LINE Corporation. All rights reserved.
 * LINE Corporation PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("io.freefair.gradle:lombok-plugin:8.0.1")
    implementation("com.github.spotbugs.snom:spotbugs-gradle-plugin:5.0.14")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:3.0.5")
}
