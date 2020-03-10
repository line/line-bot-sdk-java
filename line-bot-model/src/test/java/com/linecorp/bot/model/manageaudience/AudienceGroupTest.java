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

package com.linecorp.bot.model.manageaudience;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.linecorp.bot.model.objectmapper.ModelObjectMapper;

public class AudienceGroupTest {
    @Test
    public void serialize() throws JsonProcessingException {
        AudienceGroup audienceGroup = AudienceGroup.builder()
                                                   .audienceGroupId(3L)
                                                   .type(AudienceGroupType.IMP)
                                                   .description("hello")
                                                   .status(AudienceGroupStatus.EXPIRED)
                                                   .failedType(null)
                                                   .audienceCount(3L)
                                                   .created(1583540070193L)
                                                   .requestId("6744d281-c0bd-4a63-91d5-cab659827828")
                                                   .clickUrl(null)
                                                   .isIfaAudience(false)
                                                   .permission(AudienceGroupPermission.READ_WRITE)
                                                   .createRoute(AudienceGroupCreateRoute.OA_MANAGER)
                                                   .build();
        String s = ModelObjectMapper.createNewObjectMapper()
                                    .writeValueAsString(audienceGroup);
        assertThat(s).isEqualTo("{\"audienceGroupId\":3,\"type\":\"IMP\",\"description\":\"hello\","
                                + "\"status\":\"EXPIRED\",\"failedType\":null,\"audienceCount\":3,"
                                + "\"created\":1583540070193,"
                                + "\"requestId\":\"6744d281-c0bd-4a63-91d5-cab659827828\",\"clickUrl\":null,"
                                + "\"isIfaAudience\":false,\"permission\":\"READ_WRITE\","
                                + "\"createRoute\":\"OA_MANAGER\"}");
    }

    @Test
    public void deserialize() throws JsonProcessingException {
        String json = "{\"audienceGroupId\":3,\"type\":\"IMP\",\"description\":\"hello\","
                      + "\"status\":\"EXPIRED\",\"failedType\":null,\"audienceCount\":3,"
                      + "\"created\":1583540070193,"
                      + "\"requestId\":\"6744d281-c0bd-4a63-91d5-cab659827828\",\"clickUrl\":null,"
                      + "\"isIfaAudience\":false,\"permission\":\"READ_WRITE\","
                      + "\"createRoute\":\"OA_MANAGER\"}";
        AudienceGroup audienceGroup = ModelObjectMapper.createNewObjectMapper()
                                                       .readValue(json, AudienceGroup.class);
        assertThat(audienceGroup.getAudienceGroupId()).isEqualTo(3L);
    }
}
