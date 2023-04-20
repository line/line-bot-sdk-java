/*
 * Copyright (c) 2023 LINE Corporation. All rights reserved.
 * LINE Corporation PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("com.github.spotbugs")
    id("java-library")
    id("checkstyle")
    id("io.freefair.lombok")
    id("jacoco")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

group = rootProject.group
version = rootProject.version

tasks.named("compileJava") {
    inputs.files(tasks.named("processResources"))
}
// http://docs.spring.io/spring-boot/docs/current/reference/html/configuration-metadata.html#configuration-metadata-annotation-processor

tasks.withType<Jar> {
    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to project.name,
                "Implementation-Version" to archiveVersion
            )
        )
    }
}

lombok {
    version.set("1.18.26")
}

configure<CheckstyleExtension> {
    configFile = file("${rootProject.rootDir}/config/checkstyle/checkstyle.xml")
    configProperties = mapOf(
        "projectDir" to rootProject.projectDir.toString(),
    )
    toolVersion = "10.7.0"
}

tasks.withType(com.github.spotbugs.snom.SpotBugsTask::class.java) {
    excludeFilter.set(file("${rootProject.rootDir}/config/findbugs/excludeFilter.xml"))
}

tasks.withType<JavaCompile> {
    options.compilerArgs.addAll(
        listOf(
            "-Xlint:all",
            "-Xlint:deprecation",
            "-Werror",
            "-Xlint:-processing",
            "-parameters"
        )
    )
    options.encoding = "UTF-8"
}

tasks.withType(Test::class.java) {
    useJUnitPlatform()
    testLogging {
        // Make sure output from standard out or error is shown in Gradle output.
        showStandardStreams = true
        showExceptions = true
        showCauses = true
        showStackTraces = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
        xml.outputLocation.set(file("reports/jacoco/report.xml"))
    }
}

