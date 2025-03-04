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
    id("sdk.java-library")
    id("sdk.publish")
}

dependencies {
    implementation(project(":line-bot-jackson"))

    implementation(libs.okhttp3)
    implementation(libs.slf4j.api)
    implementation(libs.okhttp3.logging.interceptor)
    implementation(libs.bundles.retrofit2)

    compileOnly(libs.jackson.annotations)
    implementation(libs.jackson.datatype.jsr310)

    testImplementation(libs.wiremock)
    testImplementation(libs.bundles.tests)
    testImplementation(platform(libs.junit.bom))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(libs.socks.proxy.server)
    testImplementation(libs.littleproxy)
}
