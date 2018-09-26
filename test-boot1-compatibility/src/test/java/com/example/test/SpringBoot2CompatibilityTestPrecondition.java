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

package com.example.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

public class SpringBoot2CompatibilityTestPrecondition {
    @Test
    public void springBootVersionSuccessfullyOverwrittenTest() {
        final String springBootVersion =
                SpringBootApplication.class.getPackage().getImplementationVersion();

        assertThat(springBootVersion)
                .isEqualTo("1.5.16.RELEASE");
    }

    @Test
    public void springVersionSuccessfullyOverwrittenTest() {
        final String springVersion =
                Controller.class.getPackage().getImplementationVersion();

        assertThat(springVersion)
                .isEqualTo("4.3.19.RELEASE");
    }
}
