/*
 * Copyright 2020 LINE Corporation
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

package com.linecorp.bot.client;

import static org.assertj.core.api.Assumptions.assumeThat;

import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

public class IntegrationTestSettingsLoader {
    private static final URL TEST_RESOURCE = ClassLoader.getSystemResource("integration_test_settings.yml");

    public static IntegrationTestSettings load() throws IOException {
        // Do not run all test cases in this class when src/test/resources/integration_test_settings.yml doesn't
        // exist.
        assumeThat(TEST_RESOURCE)
                .as("exists integration_test_settings.yml in resource directory")
                .isNotNull();

        return new ObjectMapper(new YAMLFactory())
                .registerModule(new ParameterNamesModule())
                .readValue(TEST_RESOURCE, IntegrationTestSettings.class);
    }
}
