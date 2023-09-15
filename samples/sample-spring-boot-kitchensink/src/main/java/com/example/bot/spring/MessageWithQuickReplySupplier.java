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

package com.example.bot.spring;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import com.linecorp.bot.messaging.model.CameraAction;
import com.linecorp.bot.messaging.model.CameraRollAction;
import com.linecorp.bot.messaging.model.LocationAction;
import com.linecorp.bot.messaging.model.Message;
import com.linecorp.bot.messaging.model.MessageAction;
import com.linecorp.bot.messaging.model.PostbackAction;
import com.linecorp.bot.messaging.model.QuickReply;
import com.linecorp.bot.messaging.model.QuickReplyItem;
import com.linecorp.bot.messaging.model.TextMessage;

public class MessageWithQuickReplySupplier implements Supplier<Message> {
    @Override
    public Message get() {
        final List<QuickReplyItem> items = Arrays.asList(
                new QuickReplyItem(new MessageAction("MessageAction", "MessageAction")),
                new QuickReplyItem(new CameraAction("CameraAction")),
                new QuickReplyItem(new CameraRollAction("CemeraRollAction")),
                new QuickReplyItem(new LocationAction("Location")),
                new QuickReplyItem(new PostbackAction("PostbackAction",
                        "{PostbackAction: true}" /* data */,
                        "PostbackAction clicked" /* text */,
                        null,
                        null,
                        null
                ))
        );

        final QuickReply quickReply = new QuickReply(items);

        return new TextMessage(quickReply, null, "Message with QuickReply", null, null);
    }
}
