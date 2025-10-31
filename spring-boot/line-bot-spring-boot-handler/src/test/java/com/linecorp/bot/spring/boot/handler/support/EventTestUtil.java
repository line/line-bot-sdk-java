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

package com.linecorp.bot.spring.boot.handler.support;

import java.time.Instant;

import com.linecorp.bot.webhook.model.DeliveryContext;
import com.linecorp.bot.webhook.model.EventMode;
import com.linecorp.bot.webhook.model.MessageEvent;
import com.linecorp.bot.webhook.model.TextMessageContent;
import com.linecorp.bot.webhook.model.UserSource;

public final class EventTestUtil {
    private EventTestUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static MessageEvent createTextMessage(final String text) {
        return new MessageEvent.Builder(
                Instant.parse("2016-11-19T00:00:00.000Z").toEpochMilli(),
                EventMode.ACTIVE,
                "AAAAAAAAAAAAAA",
                new DeliveryContext(false),
                new TextMessageContent(
                        "id",
                        text,
                        null,
                        null,
                        null,
                        null
                )
        )
                .source(new UserSource("userId"))
                .replyToken("replyToken")
                .build();
    }
}
