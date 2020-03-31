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

package com.linecorp.bot.model.message.flex;

import static java.lang.ClassLoader.getSystemResource;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.springframework.util.StreamUtils.copyToString;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.skyscreamer.jsonassert.JSONAssert;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.message.flex.container.FlexContainer;
import com.linecorp.bot.model.objectmapper.ModelObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Get valid flex json and test deserialize and serialize it.
 */
@Slf4j
public class FlexDeserializeSerializeTest {
    private static final ObjectMapper mapper =
            ModelObjectMapper.createNewObjectMapper();

    @TestFactory
    public Stream<DynamicTest> testFlexReconstruction() throws Exception {
        return Files.list(Paths.get(getSystemResource("flex/reconstruction/README.md").toURI()).getParent())
                    .filter(path -> path.getFileName().toString().endsWith(".json"))
                    .sorted()
                    .map(this::testResource);
    }

    @SuppressWarnings("GrazieInspection")
    private DynamicTest testResource(final Path resource) {
        return dynamicTest(resource.getFileName().toString(), () -> {
            final FileInputStream fileInputStream = new FileInputStream(resource.toFile());

            final String json = copyToString(fileInputStream, StandardCharsets.UTF_8);
            log.debug("JSON                : {}", json);

            final FlexContainer flexContainer = mapper.readValue(json, FlexContainer.class);
            log.debug("Deserialized        : {}", flexContainer);

            final String jsonReconstructed = mapper.writeValueAsString(flexContainer);
            log.debug("Re-Serialized JSON  : {}", jsonReconstructed);

            JSONAssert.assertEquals(json, jsonReconstructed, true);
        });
    }
}
