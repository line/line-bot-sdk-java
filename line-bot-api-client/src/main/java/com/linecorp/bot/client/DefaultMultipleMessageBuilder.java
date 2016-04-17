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

package com.linecorp.bot.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.linecorp.bot.client.exception.LineBotAPIException;
import com.linecorp.bot.model.content.AbstractContent;
import com.linecorp.bot.model.content.TextContent;
import com.linecorp.bot.model.event.SendingMessagesRequest;

import lombok.NonNull;

public class DefaultMultipleMessageBuilder implements MultipleMessageBuilder {
    private final LineBotClient lineBotClient;
    private final List<AbstractContent> contents;

    public DefaultMultipleMessageBuilder(@NonNull LineBotClient lineBotClient) {
        this.lineBotClient = lineBotClient;
        this.contents = new ArrayList<>();
    }

    @Override
    public MultipleMessageBuilder addText(@NonNull String message) {
        return this.add(new TextContent(SendingMessagesRequest.TO_TYPE_USER, message));
    }

    @Override
    public MultipleMessageBuilder add(@NonNull AbstractContent content) {
        this.contents.add(content);
        return this;
    }

    @Override
    public void send(@NonNull String mid) throws LineBotAPIException {
        send(Collections.singletonList(mid));
    }

    @Override
    public void send(List<String> mids) throws LineBotAPIException {
        lineBotClient.sendMultipleMessages(mids, this.contents);
    }
}
