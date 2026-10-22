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

package com.linecorp.bot.messagingapidemoapp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.linecorp.bot.messaging.model.FlexBox;
import com.linecorp.bot.messaging.model.FlexBubble;
import com.linecorp.bot.messaging.model.FlexMessage;
import com.linecorp.bot.messagingapidemoapp.controller.message.MessageHelper;

@SpringBootTest(properties = {
        "line.bot.channel-token=DUMMY",
        "line.bot.channel-secret=DUMMY",
})
class SampleManageAudienceApplicationTests {
    @Autowired
    private MessageHelper messageHelper;

    @Test
    void contextLoads() {
    }

    @Test
    void mapsUnknownFlexLayoutToUndefined() {
        String json = """
                {"type":"flex","altText":"example","contents":{
                  "type":"bubble","body":{"type":"box","layout":"future","contents":[]}
                }}
                """;

        var messages = messageHelper.buildMessages(new String[] {json});

        assertThat(messages).hasSize(1);
        FlexMessage message = (FlexMessage) messages.get(0);
        FlexBubble bubble = (FlexBubble) message.contents();
        assertThat(bubble.body().layout()).isEqualTo(FlexBox.Layout.UNDEFINED);
    }
}
