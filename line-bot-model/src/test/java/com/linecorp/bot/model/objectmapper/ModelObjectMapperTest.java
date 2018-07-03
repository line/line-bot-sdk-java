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

package com.linecorp.bot.model.objectmapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

public class ModelObjectMapperTest {
    @Test
    public void createdInstanceIsIsolatedTest() {
        final ObjectMapper first = ModelObjectMapper.createNewObjectMapper();
        final ObjectMapper second = ModelObjectMapper.createNewObjectMapper();

        // Precondition
        assertThat(first).isNotEqualTo(second);
        assertThat(first.getPropertyNamingStrategy())
                .isEqualTo(second.getPropertyNamingStrategy())
                .isNull();

        // Do
        first.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        // Verify
        assertThat(first.getPropertyNamingStrategy())
                .isNotEqualTo(second.getPropertyNamingStrategy());
    }
}
