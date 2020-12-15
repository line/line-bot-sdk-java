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

package com.linecorp.bot.model.objectmapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * Fallback to null if the json field is not string of array.
 */
public class StringArrayOrNullDeserializer extends StdDeserializer<List<String>> {

    private static final long serialVersionUID = 1L;

    public StringArrayOrNullDeserializer() {
        super((Class<?>) null);
    }

    @Override
    public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode rootNode = p.getCodec().readTree(p);
        if (!rootNode.isArray()) {
            return null;
        }
        List<String> result = new ArrayList<>();
        for (JsonNode element : rootNode) {
            if (!element.isTextual()) {
                return null;
            }
            result.add(element.textValue());
        }
        return result;
    }
}
