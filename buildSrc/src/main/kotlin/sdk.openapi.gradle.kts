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

import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    // https://openapi-generator.tech/docs/plugins/
    id("org.openapi.generator")
    `java-library`
}

val libs = the<LibrariesForLibs>()

dependencies {
    api(project(":clients:line-bot-client-base"))
    implementation(project(":line-bot-jackson"))

    implementation(libs.slf4j.api)
    implementation(libs.bundles.retrofit2)

    compileOnly(libs.javax.annotation)
    compileOnly(libs.jakarta.annotation.api)

    testImplementation(libs.bundles.tests)
    testImplementation(libs.wiremock)
    testImplementation(libs.jul.to.slf4j)
    testImplementation(libs.test.arranger)
}

openApiGenerate {
    generatorName.set("line-java-codegen")
    outputDir.set("$buildDir/generated")

    globalProperties.set(mapOf(
        // "debugModels" to "",
        "supportingFiles" to "",
        "apis" to "",
        "apiDocs" to "false",
        "apiTests" to "true",
        "models" to "",
        "modelDocs" to "false",
        "modelTests" to "false",
    ))

    additionalProperties.set(mapOf(
        "dateLibrary" to "java8",
        "templateDir" to "$rootDir/templates",
        "openApiNullable" to "false",
        "removeEnumValuePrefix" to "false",
        "apiNameSuffix" to "Client",
        "authenticated" to true,
    ))
}

tasks {
    val openApiGenerate by getting

    compileJava {
        dependsOn(openApiGenerate)
    }
    compileTestJava {
        dependsOn(openApiGenerate)
    }
}

sourceSets {
    getByName("main") {
        java {
            srcDir("$buildDir/generated/src/main/java")
        }
    }
    getByName("test") {
        java {
            srcDir("$buildDir/generated/src/test/java")
        }
    }
}

