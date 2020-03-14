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
 *
 */

package com.linecorp.bot.model.manageaudience.request;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.linecorp.bot.model.objectmapper.ModelObjectMapper;

public class AudienceTest {
    @Test
    public void serialize() throws JsonProcessingException {
        String json = ModelObjectMapper.createNewObjectMapper()
                                       .writeValueAsString(Audience.builder()
                                                                   .id("hoge")
                                                                   .build());
        assertThat(json).isEqualTo("{\"id\":\"hoge\"}");
    }

    @Test
    public void deserialize() throws JsonProcessingException {
        Audience audience = ModelObjectMapper.createNewObjectMapper()
                                             .readValue("{\"id\":\"hoge\"}", Audience.class);
        assertThat(audience.getId()).isEqualTo("hoge");
    }
}
