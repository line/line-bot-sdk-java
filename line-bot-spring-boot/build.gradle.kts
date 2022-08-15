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

dependencies {
    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    annotationProcessor(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))

    api(project(":line-bot-api-client"))
    api(project(":line-bot-model"))
    implementation(project(":line-bot-parser"))
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation(libs.guava)

    compileOnly("javax.validation:validation-api")

    testImplementation(libs.system.lambda)
    testImplementation(libs.wiremock)
    testImplementation("org.hibernate.validator:hibernate-validator")
    testImplementation("org.springframework.boot:spring-boot-starter-test") // MockHttpServletRequest
    testImplementation("org.springframework.boot:spring-boot-starter-logging")

    // http://docs.spring.io/spring-boot/docs/current/reference/html/configuration-metadata.html#configuration-metadata-annotation-processor
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}
