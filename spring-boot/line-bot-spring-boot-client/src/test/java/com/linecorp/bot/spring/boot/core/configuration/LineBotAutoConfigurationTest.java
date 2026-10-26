/*
 * Copyright 2026 LINE Corporation
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

package com.linecorp.bot.spring.boot.core.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

class LineBotAutoConfigurationTest {
    @Test
    void preservesExistingObjectMapperBeanWithDifferentType() {
        Object applicationMapper = new Object();

        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(LineBotAutoConfiguration.class))
                .withPropertyValues("line.bot.channel-token=TOKEN",
                        "line.bot.channel-secret=SECRET")
                .withInitializer(context -> context.getBeanFactory()
                        .registerSingleton("objectMapper", applicationMapper))
                .run(context -> {
                    assertThat(context.getStartupFailure()).isNull();
                    assertThat(context.getBean("objectMapper")).isSameAs(applicationMapper);
                });
    }
}
