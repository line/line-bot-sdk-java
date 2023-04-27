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
    id("sdk.java")
    // https://openapi-generator.tech/docs/plugins/
    id("org.openapi.generator")
    id("sdk.publish")
}


dependencies {
    compileOnly(libs.jackson.annotations)
    compileOnly(libs.javax.annotation)
    compileOnly(libs.jakarta.annotation.api)

    testImplementation(libs.bundles.tests)
    testImplementation(libs.jackson.databind)
}

openApiGenerate {
    generatorName.set("line-java-codegen")
    inputSpec.set("$rootDir/line-openapi/webhook.yml")
    outputDir.set("$buildDir/generated")
    modelPackage.set("com.linecorp.bot.webhook.model")

    globalProperties.set(mapOf(
        // "debugModels" to "",
        "supportingFiles" to "",
        "models" to "",
        "modelDocs" to "false",
        "modelTests" to "false",
    ))

    additionalProperties.set(mapOf(
        "dateLibrary" to "java8",
        "templateDir" to "$rootDir/templates",
        "openApiNullable" to "false",
    ))
}

tasks {
    val openApiGenerate by getting

    compileJava {
        dependsOn(openApiGenerate)
    }
}

sourceSets {
    getByName("main") {
        java {
            srcDir("$buildDir/generated/src/main/java")
        }
    }
}

