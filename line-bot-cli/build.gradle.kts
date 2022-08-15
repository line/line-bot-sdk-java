/*
 * Copyright 2018 LINE Corporation
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

import org.springframework.boot.gradle.tasks.bundling.BootJar

apply(plugin = "org.springframework.boot")

dependencies {
    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))

    implementation(project(":line-bot-spring-boot"))
    implementation(project(":line-bot-api-client"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation(libs.guava)
    implementation("org.yaml:snakeyaml")

    testImplementation(libs.system.lambda)
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.jar {
    enabled = true
    archiveClassifier.set("")
}

val bootJar = tasks.getByName<BootJar>("bootJar") {
    enabled = true
    // Custom Launch Script avoid https://github.com/spring-projects/spring-boot/issues/5164
    launchScript {
        script = file("src/main/resources/launch.script")
    }
    archiveClassifier.set("exec")
    mainClass.set("com.linecorp.bot.cli.Application")
}

// Add exec jar into archives to be uploaded.
artifacts.add(
    "archives",
    file("$buildDir/libs/${project.name}-${project.version}-${bootJar.archiveClassifier.get()}.jar")
) {
    classifier = bootJar.archiveClassifier.get()
}
