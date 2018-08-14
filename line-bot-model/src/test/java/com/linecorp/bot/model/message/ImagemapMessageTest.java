/*
 * Copyright 2016 LINE Corporation
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

package com.linecorp.bot.model.message;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.message.imagemap.ImagemapArea;
import com.linecorp.bot.model.message.imagemap.ImagemapBaseSize;
import com.linecorp.bot.model.message.imagemap.MessageImagemapAction;
import com.linecorp.bot.model.testutil.TestUtil;

public class ImagemapMessageTest {
    @Test
    public void test() throws JsonProcessingException {
        ObjectMapper objectMapper = TestUtil.objectMapperWithProductionConfiguration(false);

        ImagemapMessage imagemapMessage = ImagemapMessage
                .builder()
                .baseUrl("https://example.com")
                .altText("hoge")
                .baseSize(new ImagemapBaseSize(1040, 1040))
                .actions(singletonList(new MessageImagemapAction("hoge", new ImagemapArea(0, 0, 20, 20))))
                .build();

        String s = objectMapper.writeValueAsString(imagemapMessage);
        assertThat(s).contains("\"type\":\"imagemap\"");
    }
}
