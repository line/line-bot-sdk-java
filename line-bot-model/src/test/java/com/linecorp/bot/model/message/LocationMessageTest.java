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

import org.junit.Test;

import com.linecorp.bot.model.message.sender.Sender;

public class LocationMessageTest {

    @Test
    public void constructor() {
        LocationMessage message = new LocationMessage("hello", "foo", 35.1, 24.3);
        assertThat(message.getTitle()).isEqualTo("hello");
        assertThat(message.getAddress()).isEqualTo("foo");
        assertThat(message.getLatitude()).isEqualTo(35.1);
        assertThat(message.getLongitude()).isEqualTo(24.3);
        assertThat(message.getQuickReply()).isNull();
        assertThat(message.getSender()).isNull();
    }

    @Test
    public void builder() {
        LocationMessage message = LocationMessage.builder()
                                                 .title("hello")
                                                 .address("foo")
                                                 .latitude(35.1)
                                                 .longitude(24.3)
                                                 .sender(Sender.builder().build())
                                                 .build();
        assertThat(message.getTitle()).isEqualTo("hello");
        assertThat(message.getAddress()).isEqualTo("foo");
        assertThat(message.getLatitude()).isEqualTo(35.1);
        assertThat(message.getLongitude()).isEqualTo(24.3);
        assertThat(message.getQuickReply()).isNull();
        assertThat(message.getSender()).isNotNull();
    }
}
