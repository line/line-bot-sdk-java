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

import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.sender.Sender;

public class FlexMessageTest {

    @Test
    public void constructor() {
        FlexMessage message = new FlexMessage("hello",
                                              Bubble.builder()
                                                    .build());
        assertThat(message.getAltText()).isEqualTo("hello");
        assertThat(message.getContents()).isNotNull();
        assertThat(message.getQuickReply()).isNull();
        assertThat(message.getSender()).isNull();
    }

    @Test
    public void builder() {
        FlexMessage message = FlexMessage
                .builder()
                .altText("hello")
                .contents(
                        Bubble.builder()
                              .build())
                .quickReply(null)
                .sender(Sender.builder()
                              .name("foo")
                              .iconUrl(URI.create("https://example.com/sender"))
                              .build())
                .build();
        assertThat(message.getAltText()).isEqualTo("hello");
        assertThat(message.getContents()).isNotNull();
        assertThat(message.getQuickReply()).isNull();
        assertThat(message.getSender()).isNotNull();
    }
}
