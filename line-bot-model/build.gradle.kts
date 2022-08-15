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
    api(libs.jackson.databind) // Provide configured ObjectMapper.
    implementation(libs.jackson.annotations)
    implementation(libs.jackson.datatype.jsr310)
    implementation(libs.jackson.module.parameter.names)
    implementation(libs.findbugs.jsr305)

    testImplementation(libs.guava)
    testImplementation(libs.reflections)
    testImplementation(libs.commons.io)
    testImplementation(libs.bundles.tests)
}
