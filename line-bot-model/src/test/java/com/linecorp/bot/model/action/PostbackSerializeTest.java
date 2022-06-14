/*
 * Copyright 2022 LINE Corporation
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

package com.linecorp.bot.model.action;

import static java.lang.ClassLoader.getSystemResourceAsStream;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.objectmapper.ModelObjectMapper;

import lombok.SneakyThrows;

public class PostbackSerializeTest {
    private static final ObjectMapper OBJECT_MAPPER = ModelObjectMapper.createNewObjectMapper();

    @SneakyThrows
    private JsonNode readResource(String name) {
        return PostbackSerializeTest.OBJECT_MAPPER.readTree(getSystemResourceAsStream(name));
    }

    @Test
    public void testPostbackRichMenu() {
        final PostbackAction action = new PostbackAction(
                "LABEL",
                "DATA",
                "DISPLAYTEXT",
                null,
                PostbackAction.InputOptionType.OPEN_RICH_MENU,
                null
        );

        final JsonNode node1 = PostbackSerializeTest.OBJECT_MAPPER.valueToTree(action);
        final JsonNode node2 = readResource("action/postback_richmenu.json");

        assertEquals(node1, node2);
    }

    @Test
    public void testPostbackKeyboard() {
        final PostbackAction action = new PostbackAction(
                "LABEL",
                "DATA",
                "DISPLAYTEXT",
                null,
                PostbackAction.InputOptionType.OPEN_KEYBOARD,
                "FILLINTEXT"
        );

        final JsonNode node1 = PostbackSerializeTest.OBJECT_MAPPER.valueToTree(action);
        final JsonNode node2 = readResource("action/postback_keyboard.json");

        assertEquals(node1, node2);
    }
}
