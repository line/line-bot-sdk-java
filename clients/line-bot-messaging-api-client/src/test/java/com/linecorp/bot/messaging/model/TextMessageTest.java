/*
 * Copyright 2023 LINE Corporation
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

package com.linecorp.bot.messaging.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class TextMessageTest {
    @Test
    public void testBuilder() {
        var builder = new TextMessage.Builder();
        TextMessage textMessage = builder.text("Hello").build();
        assertThat(textMessage.text()).isEqualTo("Hello");
        assertThat(textMessage.quoteToken()).isNull();
    }

    @Test
    public void testBuilderMissingParameter() {
        assertThatThrownBy(() -> {
            var builder = new TextMessage.Builder();
            builder.build();
        })
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("'text' must be set for TextMessage.");
    }

}
