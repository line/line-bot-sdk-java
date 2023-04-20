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

pluginManagement {
    repositories {
        mavenCentral()
        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven { url = uri("https://repo.spring.io/plugins-release") }
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            from(files("./gradle/libs.versions.toml"))
        }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include("line-bot-api-client")
include("line-bot-model")
include("line-bot-spring-boot")
include("line-bot-parser")
include("line-bot-jackson")

// openapi based clients
include("clients:line-bot-client-base")
include("clients:line-bot-insight-client")
include("clients:line-bot-manage-audience-client")
include("clients:line-bot-messaging-api-client")
include("clients:line-bot-channel-access-token-client")
include("clients:line-liff-client")

// samples
include("samples:sample-spring-boot-echo")
include("samples:sample-spring-boot-echo-kotlin")
include("samples:sample-spring-boot-kitchensink")
include("samples:sample-manage-audience")
