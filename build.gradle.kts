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

import com.github.spotbugs.snom.SpotBugsTask
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.springframework.boot.gradle.plugin.SpringBootPlugin

// ./gradlew clean && ./gradlew uploadArchives -Prelease

plugins {
    `java-library`
    checkstyle
    id("org.jetbrains.kotlin.jvm") version "1.7.10" apply false
    id("com.github.spotbugs") version "5.0.9" apply false
    id("io.spring.dependency-management") version "1.0.12.RELEASE" apply false
    id("org.springframework.boot") version "2.7.1" apply false
    id("io.freefair.lombok") version "6.5.0.2"
}

apply(plugin = "idea")
apply(plugin = "jacoco")

//set build variables based on build type (release, continuous integration, development)
val isReleaseBuild = hasProperty("release")
val sonatypeRepositoryUrl = if (isReleaseBuild) {
    "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
} else {
    "https://oss.sonatype.org/content/repositories/snapshots/"
}

group = "com.linecorp.bot"
version = "4.10.0" + if (isReleaseBuild) {
    ""
} else {
    "-SNAPSHOT"
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply {
        plugin("com.github.spotbugs")
        plugin("java-library")
        plugin("checkstyle")
        plugin("io.spring.dependency-management")
        plugin("io.freefair.lombok")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    group = rootProject.group
    version = rootProject.version

    configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
        imports {
            mavenBom(SpringBootPlugin.BOM_COORDINATES)
        }

        dependencies {
            dependency("com.google.guava:guava:31.1-jre")
            dependency("com.github.stefanbirkner:system-lambda:1.2.1")
            dependency("com.github.tomakehurst:wiremock-jre8:2.33.2")
            dependencySet("com.squareup.retrofit2:2.9.0") {
                entry("converter-jackson")
                entry("retrofit")
            }
            dependencySet("io.jsonwebtoken:0.11.5") {
                entry("jjwt-api")
                entry("jjwt-impl")
                entry("jjwt-jackson")
            }
        }
    }

    dependencies {
        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
        // http://docs.spring.io/spring-boot/docs/current/reference/html/configuration-metadata.html#configuration-metadata-annotation-processor

        testImplementation("com.google.guava:guava")
        testImplementation("com.github.stefanbirkner:system-lambda")
        testImplementation("com.github.tomakehurst:wiremock-jre8")
        testImplementation("org.hibernate.validator:hibernate-validator")
        testImplementation("org.springframework.boot:spring-boot-starter-test") // MockHttpServletRequest
        testImplementation("org.springframework.boot:spring-boot-starter-logging")
    }

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
        version.set("1.18.22")
    }


    if (!project.name.startsWith("sample-") && !project.name.startsWith("test-")) {
        tasks.withType<Javadoc> {
            isFailOnError = false
            options.encoding = "UTF-8"
            options.locale = "en_US"
            (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
            // To create javadoc for generated method&constructor, delombok & run javadoc on delombok.outputDir.
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
    }

    configure<CheckstyleExtension> {
        configFile = file("${rootProject.rootDir}/config/checkstyle/checkstyle.xml")
        configProperties = mapOf(
            "projectDir" to rootProject.projectDir.toString(),
        )
        toolVersion = "8.23"
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
            exceptionFormat = TestExceptionFormat.FULL
        }
    }

    tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun> {
        systemProperties = System.getProperties() as Map<String, Any>
    }
}


tasks.register<JacocoReport>("codeCoverageReport") {
    val sourceSets = project.extensions.getByType<SourceSetContainer>()

    onlyIf { true }
    executionData.setFrom(project.fileTree(".") { include("**/build/jacoco/*.exec") })

    listOf(":line-bot-api-client", ":line-bot-model", ":line-bot-servlet", ":line-bot-spring-boot", ":line-bot-cli").forEach {
        sourceSets(project(it).sourceSets["main"])
    }

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
        xml.destination = file("reports/jacoco/report.xml")
    }
}

listOf(
    ":line-bot-api-client",
    ":line-bot-model",
    ":line-bot-parser",
    ":line-bot-servlet",
    ":line-bot-spring-boot",
    ":line-bot-cli"
).forEach { projectName ->
    project(projectName) {
        apply(plugin = "jacoco")
        apply(plugin = "signing")
        apply(plugin = "maven-publish")

        configure<JavaPluginExtension> {
            withJavadocJar()
            withSourcesJar()
        }

        configure<PublishingExtension> {
            publications {
                create<MavenPublication>("mavenJava") {
                    from(components["java"])

                    pom {
                        name.set(project.name)
                        packaging = "jar"
                        if (project.description != null) {
                            description.set(project.description)
                        } else {
                            description.set("LINE Bot SDK Java - " + project.name)
                        }
                        url.set("https://github.com/line/line-bot-sdk-java")

                        scm {
                            url.set("scm:git@github.com:line/line-bot-sdk-java.git")
                            connection.set("scm:git@github.com:line/line-bot-sdk-java.git")
                            developerConnection.set("scm:git@github.com:line/line-bot-sdk-java.git")
                        }
                        licenses {
                            license {
                                name.set("Apache")
                                url.set("https://opensource.org/licenses/Apache-2.0")
                            }
                        }
                        developers {
                            developer {
                                id.set("tokuhirom")
                                name.set("Tokuhiro Matsuno")
                                email.set("tokuhirom@gmail.com")
                            }
                            developer {
                                id.set("kazuki-ma")
                                name.set("Kazuki MATSUDA")
                                email.set("matsuda.kazuki@gmail.com")
                            }
                        }
                    }
                }
                repositories {
                    maven {
                        url = uri(sonatypeRepositoryUrl)
                        if (project.hasProperty("sonatypeUsername")) {
                            credentials {
                                username = properties["sonatypeUsername"] as String
                                password = properties["sonatypePassword"] as String
                            }
                        }
                    }
                }
            }
        }

        tasks.withType<Sign>().configureEach {
            setRequired(isReleaseBuild)
            val publishing = extensions.findByName("publishing") as PublishingExtension
            sign(publishing.publications["mavenJava"])
        }
    }
}
