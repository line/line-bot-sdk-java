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

package com.linecorp.bot.model.callback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.content.ContentType;
import com.linecorp.bot.model.content.StickerContent;
import com.linecorp.bot.model.content.metadata.StickerContentMetadata;

public class StickerContentTest {
    @Test
    public void test() throws Exception {
        InputStream resource = getClass().getClassLoader().getResourceAsStream("sticker-content.json");
        String json = IOUtils.toString(resource);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        CallbackRequest callbackRequest = objectMapper.readValue(json, CallbackRequest.class);
        assertEquals(1, callbackRequest.getResult().size());
        Event event = callbackRequest.getResult().get(0);
        assertEquals("ABCDEF-12345678901", event.getId());
        StickerContent content = (StickerContent) event.getContent();
        assertEquals("325708", content.getId());
        assertThat(content.getContentType())
                .isEqualTo(ContentType.STICKER);

        assertThat(content.getContentMetadata(), instanceOf(StickerContentMetadata.class));
        assertEquals("4913046", content.getContentMetadata().getStkid());
    }
}
