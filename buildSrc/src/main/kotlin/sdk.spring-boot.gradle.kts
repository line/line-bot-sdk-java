/*
 * Copyright (c) 2023 LINE Corporation. All rights reserved.
 * LINE Corporation PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

plugins {
    id("org.springframework.boot")
}

tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun> {
    systemProperties = System.getProperties() as Map<String, Any>
}
