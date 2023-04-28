/*
 * Copyright (c) 2023 LINE Corporation. All rights reserved.
 * LINE Corporation PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

plugins {
    id("signing")
    id("maven-publish")
}

//set build variables based on build type (release, continuous integration, development)
val isReleaseBuild = hasProperty("release")
val sonatypeRepositoryUrl = if (isReleaseBuild) {
    "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
} else {
    "https://oss.sonatype.org/content/repositories/snapshots/"
}

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

signing {
    setRequired(isReleaseBuild)
    publishing.publications.configureEach {
        sign(this)
    }
}
