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

package com.linecorp.bot.model;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.narrowcast.filter.DemographicFilter;
import com.linecorp.bot.model.narrowcast.filter.GenderDemographicFilter;
import com.linecorp.bot.model.narrowcast.filter.GenderDemographicFilter.Gender;
import com.linecorp.bot.model.narrowcast.filter.OperatorDemographicFilter;
import com.linecorp.bot.model.narrowcast.recipient.AudienceRecipient;
import com.linecorp.bot.model.narrowcast.recipient.LogicalOperatorRecipient;
import com.linecorp.bot.model.narrowcast.recipient.Recipient;
import com.linecorp.bot.model.narrowcast.recipient.RedeliveryRecipient;
import com.linecorp.bot.model.objectmapper.ModelObjectMapper;

public class NarrowcastTest {
    @Test
    public void testSerializeOperatorDemographicFilter() throws JsonProcessingException {
        ObjectMapper objectMapper = ModelObjectMapper.createNewObjectMapper();
        String json = objectMapper.writeValueAsString(
                OperatorDemographicFilter
                        .builder()
                        .and(Collections.singletonList(GenderDemographicFilter.builder()
                                                                              .oneOf(Collections.singletonList(
                                                                                      Gender.MALE))
                                                                              .build()))
                        .build()
        );
        System.out.println(json);
        assertThat(json).isEqualTo(
                "{\"type\":\"operator\","
                + "\"and\":[{\"type\":\"gender\",\"oneOf\":[\"male\"]}],"
                + "\"or\":null,"
                + "\"not\":null}");
    }

    @Test
    public void testDeserializeOperatorDemographicFilter() throws JsonProcessingException {
        String json = "{\"type\":\"operator\", \"and\":  [{\"type\": \"gender\", \"oneOf\": [\"male\"]}]}";

        ObjectMapper objectMapper = ModelObjectMapper.createNewObjectMapper();
        DemographicFilter filter = objectMapper.readValue(json, DemographicFilter.class);
        assertThat(filter).isInstanceOf(OperatorDemographicFilter.class);
    }

    @Test
    public void testRecipientDeserializeAudience() throws JsonProcessingException {
        ObjectMapper objectMapper = ModelObjectMapper.createNewObjectMapper();
        Recipient recipient = objectMapper.readValue(
                "{\"type\":\"audience\", \"audienceGroupId\":59693}",
                Recipient.class
        );
        assertThat(recipient).isInstanceOf(AudienceRecipient.class);
        assertThat(recipient.getType()).isEqualTo("audience");
        assertThat(((AudienceRecipient) recipient).getAudienceGroupId()).isEqualTo(59693);
    }

    @Test
    public void testRecipientDeserializeOperator() throws JsonProcessingException {
        ObjectMapper objectMapper = ModelObjectMapper.createNewObjectMapper();
        Recipient recipient = objectMapper.readValue(
                "{\"type\":\"operator\","
                + " \"and\":[{\"type\":\"audience\", \"audienceGroupId\":  5963}]}",
                Recipient.class
        );
        assertThat(recipient).isInstanceOf(LogicalOperatorRecipient.class);
        assertThat(recipient.getType()).isEqualTo("operator");
        assertThat(((LogicalOperatorRecipient) recipient).getAnd())
                .isEqualTo(singletonList(AudienceRecipient.builder()
                                                          .audienceGroupId(5963L)
                                                          .build()));
    }

    @Test
    public void testRecipientDeserializeRedelivery() throws JsonProcessingException {
        ObjectMapper objectMapper = ModelObjectMapper.createNewObjectMapper();
        Recipient recipient = objectMapper.readValue(
                //language=JSON
                "{\n"
                + "  \"type\": \"redelivery\",\n"
                + "  \"requestId\": \"5b59509c-c57b-11e9-aa8c-2a2ae2dbcce4\"\n"
                + "}", Recipient.class);

        assertThat(recipient)
                .isInstanceOf(RedeliveryRecipient.class)
                .isEqualTo(RedeliveryRecipient.builder()
                                              .requestId("5b59509c-c57b-11e9-aa8c-2a2ae2dbcce4")
                                              .build());
    }
}
