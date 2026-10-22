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

package com.linecorp.bot.messaging.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonView;
import com.linecorp.bot.jackson.ModelObjectMapper;

import tools.jackson.databind.SerializationFeature;

class ModelObjectMapperTest {
    @Test
    void ignoresUnknownProperties() throws Exception {
        GetWebhookEndpointResponse response = ModelObjectMapper.createNewObjectMapper()
                .readValue(
                        "{\"endpoint\":\"https://example.com\","
                                + "\"active\":true,\"newField\":true}",
                        GetWebhookEndpointResponse.class);

        assertThat(response).isEqualTo(
                new GetWebhookEndpointResponse(URI.create("https://example.com"), true));
    }

    @Test
    void mapsUnknownQuotaTypeToUndefined() throws Exception {
        MessageQuotaResponse response = ModelObjectMapper.createNewObjectMapper()
                .readValue("{\"type\":\"future\"}", MessageQuotaResponse.class);

        assertThat(response.type()).isEqualTo(QuotaType.UNDEFINED);
    }

    @Test
    void readsNumericTimestampAsMilliseconds() throws Exception {
        Instant timestamp = ModelObjectMapper.createNewObjectMapper()
                .readValue("1462629479859", Instant.class);

        assertThat(timestamp).isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z"));
    }

    @Test
    void readsIsoTimestamp() throws Exception {
        Instant timestamp = ModelObjectMapper.createNewObjectMapper()
                .readValue("\"2016-05-07T13:57:59.859Z\"", Instant.class);

        assertThat(timestamp).isEqualTo(Instant.parse("2016-05-07T13:57:59.859Z"));
    }

    @Test
    void writesTimestampWithFractionalSeconds() throws Exception {
        String json = ModelObjectMapper.createNewObjectMapper()
                .writeValueAsString(Instant.parse("2016-05-07T13:57:59.859Z"));

        assertThat(json).isEqualTo("1462629479.859000000");
    }

    @Test
    void readsOffsetDateTimeInGeneratedResponse() throws Exception {
        TestWebhookEndpointResponse response = ModelObjectMapper.createNewObjectMapper()
                .readValue("{\"timestamp\":\"2020-12-03T10:15:30.121Z\","
                                + "\"statusCode\":200}",
                        TestWebhookEndpointResponse.class);

        assertThat(response.timestamp())
                .isEqualTo(OffsetDateTime.parse("2020-12-03T10:15:30.121Z"));
    }

    @Test
    void writesOffsetDateTimeWithFractionalSeconds() throws Exception {
        String json = ModelObjectMapper.createNewObjectMapper()
                .writeValueAsString(OffsetDateTime.parse("1970-01-01T00:00:01.125Z"));

        assertThat(json).isEqualTo("1.125000000");
    }

    @Test
    void readsNullPrimitiveArrayElementAsDefaultValue() throws Exception {
        boolean[] values = ModelObjectMapper.createNewObjectMapper()
                .readValue("[null]", boolean[].class);

        assertThat(values).containsExactly(false);
    }

    @Test
    void writesDurationAsNumericTimestamp() throws Exception {
        String json = ModelObjectMapper.createNewObjectMapper()
                .writeValueAsString(Duration.ofMillis(1250));

        assertThat(json).isEqualTo("1.250000000");
    }

    @Test
    void readsAndWritesSixHundredNestedArrays() throws Exception {
        String json = "[".repeat(600) + "0" + "]".repeat(600);
        var mapper = ModelObjectMapper.createNewObjectMapper();

        Object value = mapper.readValue(json, Object.class);

        assertThat(mapper.writeValueAsString(value)).isEqualTo(json);
    }

    @Test
    void rejectsStringLongerThanTwentyMillionCharacters() {
        String json = "\"" + "x".repeat(20_100_000) + "\"";
        var mapper = ModelObjectMapper.createNewObjectMapper();

        assertThatThrownBy(() -> mapper.readValue(json, String.class))
                .hasMessageContaining("String value length");
    }

    @Test
    void includesUnannotatedPropertyWhenWritingWithView() throws Exception {
        String json = ModelObjectMapper.createNewObjectMapper()
                .writerWithView(SelectedView.class)
                .writeValueAsString(new ViewPayload("selected", "common"));

        assertThat(json).isEqualTo("{\"selected\":\"selected\",\"common\":\"common\"}");
    }

    @Test
    void ignoresTrailingClosingBraceAfterJsonObject() throws Exception {
        byte[] body = ("{\"endpoint\":\"https://example.com\",\"active\":true}}")
                .getBytes(StandardCharsets.UTF_8);
        GetWebhookEndpointResponse response = ModelObjectMapper.createNewObjectMapper()
                .readValue(new ByteArrayInputStream(body), GetWebhookEndpointResponse.class);

        assertThat(response).isEqualTo(
                new GetWebhookEndpointResponse(URI.create("https://example.com"), true));
    }

    @Test
    void usesLastValueForDuplicateMapKey() throws Exception {
        var values = ModelObjectMapper.createNewObjectMapper()
                .readValue("{\"value\":1,\"value\":2}", Map.class);

        assertThat(values.get("value")).isEqualTo(2);
    }

    @Test
    void omitsVoidGetterWhenWritingJson() throws Exception {
        String json = ModelObjectMapper.createNewObjectMapper()
                .writeValueAsString(new VoidGetterPayload("value"));

        assertThat(json).isEqualTo("{\"value\":\"value\"}");
    }

    @Test
    void rejectsNonComparableKeysWhenMapSortingIsEnabled() {
        Map<Object, String> values = Map.of(new Object(), "value");
        var writer = ModelObjectMapper.createNewObjectMapper().writer()
                .with(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);

        assertThatThrownBy(() -> writer.writeValueAsString(values))
                .hasMessageContaining("Cannot order Map entries");
    }

    @Test
    void writesWebhookEndpointRequestAsJson() throws Exception {
        String json = ModelObjectMapper.createNewObjectMapper()
                .writeValueAsString(
                        new SetWebhookEndpointRequest(URI.create("https://example.com")));

        assertThat(json).isEqualTo("{\"endpoint\":\"https://example.com\"}");
    }

    @Test
    void returnsSeparateMappers() {
        assertThat(ModelObjectMapper.createNewObjectMapper())
                .isNotSameAs(ModelObjectMapper.createNewObjectMapper());
    }

    private interface SelectedView {
    }

    private record ViewPayload(@JsonView(SelectedView.class) String selected, String common) {
    }

    private record VoidGetterPayload(String value) {
        public Void getAbsent() {
            return null;
        }
    }
}
