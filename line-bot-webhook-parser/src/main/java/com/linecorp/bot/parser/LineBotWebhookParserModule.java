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

package com.linecorp.bot.parser;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

import com.linecorp.bot.model.event.CallbackRequest;

class LineBotWebhookParserModule extends SimpleModule {
    private static final long serialVersionUID = 1592842153305097463L;

    LineBotWebhookParserModule(LineBotCallbackRequestDeserializer lineBotCallbackRequestDeserializer) {
        super(new Version(0, 1, 0, null, null, null));

        this.addDeserializer(CallbackRequest.class, lineBotCallbackRequestDeserializer);
    }
}
