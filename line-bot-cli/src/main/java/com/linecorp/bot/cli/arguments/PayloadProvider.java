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

package com.linecorp.bot.cli.arguments;

import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS;
import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES;
import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES;
import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.objectmapper.ModelObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Lazy
@Data
@Slf4j
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PayloadProvider {
    private static final ObjectMapper OBJECT_MAPPER = ModelObjectMapper
            .createNewObjectMapper()
            .configure(ALLOW_UNQUOTED_FIELD_NAMES, true)
            .configure(ALLOW_COMMENTS, true)
            .configure(ALLOW_SINGLE_QUOTES, true)
            .configure(ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
            .configure(FAIL_ON_UNKNOWN_PROPERTIES, true)
            .configure(INDENT_OUTPUT, true);
    private static final Yaml YAML = new Yaml();
    private final PayloadArguments arguments;

    /**
     * Reads --body, --json or --yaml data as specific class instance.
     */
    public <T> T read(Class<T> clazz) {
        try {
            return readInternal(clazz);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            if (e instanceof IOException) {
                throw new UncheckedIOException((IOException) e);
            }
            throw new RuntimeException(e);
        }
    }

    <T> T readInternal(Class<T> clazz) throws Exception {
        if (arguments.getData() != null) {
            return OBJECT_MAPPER.readValue(arguments.getData(), clazz);
        }

        if (arguments.getJson() != null) {
            try (FileInputStream is = new FileInputStream(arguments.getJson())) {
                return OBJECT_MAPPER.readValue(is, clazz);
            }
        }

        if (arguments.getYaml() != null) {
            final Object yamlAsObject;
            try (FileInputStream is = new FileInputStream(arguments.getYaml())) {
                yamlAsObject = YAML.load(is);
            }
            log.debug("{}", yamlAsObject);
            return OBJECT_MAPPER.convertValue(yamlAsObject, clazz);
        }

        throw new IllegalArgumentException("--data= or --json= or --yaml is mandatory");
    }
}
