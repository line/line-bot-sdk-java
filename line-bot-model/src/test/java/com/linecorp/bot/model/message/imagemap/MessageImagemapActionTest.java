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

package com.linecorp.bot.model.message.imagemap;

import static org.assertj.core.api.Java6Assertions.assertThat;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageImagemapActionTest {
    @Test
    public void getText() throws Exception {
        MessageImagemapAction imageMapAction = new MessageImagemapAction("hoge", new ImagemapArea(1, 2, 3, 4));

        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(imageMapAction);
        assertThat(s).contains("\"type\":\"message\"");
    }

}
