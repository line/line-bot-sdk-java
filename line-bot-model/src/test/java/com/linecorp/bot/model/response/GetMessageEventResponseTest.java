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

package com.linecorp.bot.model.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.response.GetMessageEventResponse.Message;
import com.linecorp.bot.model.response.GetMessageEventResponse.Overview;
import com.linecorp.bot.model.testutil.TestUtil;

public class GetMessageEventResponseTest {
    private static final ObjectMapper OBJECT_MAPPER = TestUtil.objectMapperWithProductionConfiguration(true);

    @Test
    public void testDeserialize() throws IOException {
        InputStream resource = getClass().getClassLoader().getResourceAsStream(
                "response/GetMessageEventResponse.json");
        assertThat(resource).isNotNull();
        GetMessageEventResponse response = OBJECT_MAPPER.readValue(resource,
                                                                   GetMessageEventResponse.class);
        assertThat(response).isNotNull();

        Overview overview = response.getOverview();
        assertThat(overview).isInstanceOf(Overview.class);
        assertThat(overview.getRequestId()).isEqualTo("f70dd685-499a-4231-a441-f24b8d4fba21");
        assertThat(overview.getTimestamp()).isEqualTo(1568214000L);
        assertThat(overview.getDelivered()).isEqualTo(32L);
        assertThat(overview.getUniqueImpression()).isEqualTo(4L);
        assertThat(overview.getUniqueClick()).isEqualTo(5L);
        assertThat(overview.getUniqueMediaPlayed()).isEqualTo(2L);
        assertThat(overview.getUniqueMediaPlayed100Percent()).isEqualTo(-1L);

        assertThat(response.getMessages()).hasSize(1);
        Message message = response.getMessages().get(0);
        assertThat(message).isNotNull();
        assertThat(message.getSeq()).isEqualTo(1L);
        assertThat(message.getImpression()).isEqualTo(18L);
        assertThat(message.getMediaPlayed()).isEqualTo(11L);
        assertThat(message.getMediaPlayed25Percent()).isEqualTo(25289L);
        assertThat(message.getMediaPlayed50Percent()).isEqualTo(523L);
        assertThat(message.getMediaPlayed75Percent()).isEqualTo(20L);
        assertThat(message.getMediaPlayed100Percent()).isEqualTo(9L);
        assertThat(message.getUniqueMediaPlayed()).isEqualTo(2L);
        assertThat(message.getUniqueMediaPlayed25Percent()).isEqualTo(1004L);
        assertThat(message.getUniqueMediaPlayed50Percent()).isEqualTo(204L);
        assertThat(message.getUniqueMediaPlayed75Percent()).isEqualTo(8L);
        assertThat(message.getUniqueMediaPlayed100Percent()).isEqualTo(2L);

        assertThat(response.getClicks()).hasSize(2);
        assertThat(response.getClicks().get(0).getSeq())
                .isEqualTo(1L);
        assertThat(response.getClicks().get(0).getUrl())
                .isEqualTo("https://example.com/1st");
        assertThat(response.getClicks().get(0).getClick())
                .isEqualTo(100L);
        assertThat(response.getClicks().get(0).getUniqueClick())
                .isEqualTo(45L);
        assertThat(response.getClicks().get(0).getUniqueClickOfRequest())
                .isEqualTo(12L);

        assertThat(response.getClicks().get(1).getSeq())
                .isEqualTo(2L);
        assertThat(response.getClicks().get(1).getUrl())
                .isEqualTo("https://example.com/2nd");
        assertThat(response.getClicks().get(1).getClick())
                .isEqualTo(29L);
        assertThat(response.getClicks().get(1).getUniqueClick())
                .isEqualTo(10L);
        assertThat(response.getClicks().get(1).getUniqueClickOfRequest())
                .isEqualTo(8L);
    }
}
