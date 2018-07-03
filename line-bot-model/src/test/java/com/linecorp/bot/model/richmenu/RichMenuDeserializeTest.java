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

package com.linecorp.bot.model.richmenu;

import static java.lang.ClassLoader.getSystemResourceAsStream;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.testutil.TestUtil;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RichMenuDeserializeTest {
    @Test
    public void testRichMenuResponse() {
        final RichMenuResponse target =
                parse("richmenu/richmenu_response.json", RichMenuResponse.class);

        assertThat(target.getName()).isEqualTo("NAME");
        assertThat(target.getChatBarText()).isEqualTo("CHAT_BAR_TEXT");
        assertThat(target.getSize()).isEqualTo(RichMenuSize.HALF);
        assertThat(target.getRichMenuId()).isEqualTo("richmenu-f85ab95699420f1c9330f22bb4f489e8");
        assertThat(target.getAreas()).isEmpty();
        assertThat(target.toString()).contains("richmenu-f85ab95699420f1c9330f22bb4f489e8");
    }

    @Test
    public void testRichMenuRequest() throws Exception {
        final RichMenuArea richMenuArea =
                new RichMenuArea(new RichMenuBounds(0, 0, 2500, 1686),
                                 new PostbackAction(null, "action=buy&itemid=123"));
        final RichMenu richMenu = RichMenu
                .builder()
                .size(RichMenuSize.FULL)
                .selected(false)
                .name("Nice richmenu")
                .chatBarText("Tap here")
                .areas(singletonList(richMenuArea))
                .build();

        final String resourceName = "richmenu/richmenu_request.json";

        assertEqualsAsJsonNode(richMenu, resourceName);
    }

    @Test
    public void testRichMenuIdResponse() {
        final RichMenuIdResponse target =
                parse("richmenu/richmenu_id_response.json", RichMenuIdResponse.class);

        final String referenceId = "richmenu-3c05e9f99d5bee8b21b4c3e0e09d3eec";

        assertThat(target.getRichMenuId()).isEqualTo(referenceId);
        assertThat(target).isEqualTo(new RichMenuIdResponse(referenceId));
        assertThat(target.toString()).contains(referenceId);
    }

    @Test
    public void testRichMenuListResponse() {
        final RichMenuListResponse target =
                parse("richmenu/richmenu_list_response.json", RichMenuListResponse.class);
        final RichMenuArea richMenuAreaReference =
                new RichMenuArea(new RichMenuBounds(0, 0, 2500, 1686),
                                 new PostbackAction(null, "action=buy&itemid=123"));

        assertThat(target.getRichMenus()).hasSize(1);
        assertThat(target.toString()).contains("{richMenuId}");

        final RichMenuResponse richMenuResponse = target.getRichMenus().get(0);

        assertThat(richMenuResponse.getRichMenuId()).isEqualTo("{richMenuId}");
        assertThat(richMenuResponse.getAreas()).isEqualTo(singletonList(richMenuAreaReference));
        assertThat(richMenuResponse.getSize()).isEqualTo(RichMenuSize.FULL);
        assertThat(richMenuResponse.getName()).isEqualTo("NAME");
        assertThat(richMenuResponse.getChatBarText()).isEqualTo("CHAT_BAR_TEXT");
    }

    @SneakyThrows
    private static void assertEqualsAsJsonNode(final Object target, final String resourceName) {
        log.info("Comparing resource {} with {}", resourceName, target);

        final ObjectMapper objectMapper = TestUtil.objectMapperWithProductionConfiguration(true);

        final JsonNode targetJsonNode = objectMapper.valueToTree(target);
        final JsonNode referenceJsonNode = objectMapper.readTree(
                getSystemResourceAsStream(resourceName));

        assertThat(targetJsonNode).isEqualTo(referenceJsonNode);
    }

    @SneakyThrows
    private static <T> T parse(final String resourceName, final Class<T> clazz) {
        final ObjectMapper objectMapper = TestUtil.objectMapperWithProductionConfiguration(true);

        try (InputStream is = getSystemResourceAsStream(resourceName)) {
            return objectMapper.readValue(is, clazz);
        }
    }
}
