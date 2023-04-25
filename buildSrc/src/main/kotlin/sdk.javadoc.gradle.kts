/*
 * Copyright (c) 2023 LINE Corporation. All rights reserved.
 * LINE Corporation PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

plugins {
    id("java-library")
}

tasks.withType<Javadoc> {
    isFailOnError = false
    options.encoding = "UTF-8"
    options.locale = "en_US"
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
}

val javadocJar = tasks.register<Jar>("javadocJar") {
    val javadoc: Javadoc by tasks
    dependsOn(javadoc)
    archiveClassifier.set("javadoc")
    from(javadoc.destinationDir)
}

val sourcesJar = tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

artifacts {
    archives(javadocJar)
    archives(sourcesJar)
}
