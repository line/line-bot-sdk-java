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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import com.linecorp.bot.model.event.CallbackRequest;
import com.linecorp.bot.model.event.Event;

class LineBotCallbackRequestDeserializer extends JsonDeserializer<CallbackRequest> {
    private final LineBotDeserializerOption deserializeOption;

    LineBotCallbackRequestDeserializer(LineBotDeserializerOption deserializeOption) {
        this.deserializeOption = deserializeOption;
    }

    @Override
    public CallbackRequest deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        final JsonNode node = p.getCodec().readTree(p);
        if (!node.isObject()) {
            throw new LineBotWebhookParseException("request body is not json object");
        }

        final JsonNode eventsNode = node.get("events");
        if (eventsNode == null || !eventsNode.isArray()) {
            throw new LineBotWebhookParseException("`events` is not array");
        }

        final ArrayNode arrayNode = (ArrayNode) eventsNode;
        final List<Event> result = new ArrayList<>();
        final ObjectCodec codec = p.getCodec();
        for (JsonNode n : arrayNode) {
            try {
                result.add(codec.treeToValue(n, Event.class));
            } catch (JsonProcessingException e) {
                final Event fallback = deserializeOption.getDeserializeExceptionHandler().apply(e);
                if (fallback != null || !deserializeOption.isSkipNull()) {
                    result.add(fallback);
                }
            }
        }
        return new CallbackRequest(result);
    }
}
