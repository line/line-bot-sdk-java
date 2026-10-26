/*
 * Copyright 2023 LINE Corporation
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

package com.linecorp.bot.jackson;

import tools.jackson.core.StreamReadConstraints;
import tools.jackson.core.StreamWriteConstraints;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.cfg.EnumFeature;
import tools.jackson.databind.json.JsonMapper;

public final class ModelObjectMapper {
    private static final int JSON_NESTING_LIMIT = 1000;
    private static final int JSON_STRING_LENGTH_LIMIT = 20_000_000;
    private static final JsonFactory JSON_FACTORY = JsonFactory.builderWithJackson2Defaults()
            .streamReadConstraints(StreamReadConstraints.builder()
                    .maxNestingDepth(JSON_NESTING_LIMIT)
                    .maxStringLength(JSON_STRING_LENGTH_LIMIT).build())
            .streamWriteConstraints(StreamWriteConstraints.builder()
                    .maxNestingDepth(JSON_NESTING_LIMIT).build())
            .build();
    private static final ObjectMapper OBJECT_MAPPER = JsonMapper.builder(JSON_FACTORY)
            .configureForJackson2()
            .enable(MapperFeature.DEFAULT_VIEW_INCLUSION)
            .disable(MapperFeature.ALLOW_VOID_VALUED_PROPERTIES)
            .enable(SerializationFeature.FAIL_ON_ORDER_MAP_BY_INCOMPARABLE_KEY)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(EnumFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
            .disable(DateTimeFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)
            .build();

    private ModelObjectMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static ObjectMapper createNewObjectMapper() {
        return OBJECT_MAPPER.rebuild().build();
    }
}
