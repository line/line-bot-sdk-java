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
 *
 */

package com.linecorp.bot.model.message;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.Test;

import com.linecorp.bot.model.message.sender.Sender;

public class StickerMessageTest {

    @Test
    public void constructor() {
        StickerMessage message = new StickerMessage("foo", "bar");
        assertThat(message.getPackageId()).isEqualTo("foo");
        assertThat(message.getStickerId()).isEqualTo("bar");
        assertThat(message.getQuickReply()).isNull();
        assertThat(message.getSender()).isNull();
    }

    @Test
    public void builder() {
        StickerMessage message = StickerMessage.builder()
                                               .packageId("foo")
                                               .stickerId("bar")
                                               .quickReply(null)
                                               .sender(Sender.builder()
                                                             .name("hello")
                                                             .iconUrl(URI.create("http://example.com/"))
                                                             .build())
                                               .build();
        assertThat(message.getPackageId()).isEqualTo("foo");
        assertThat(message.getStickerId()).isEqualTo("bar");
        assertThat(message.getQuickReply()).isNull();
        assertThat(message.getSender()).isNotNull();
    }
}
