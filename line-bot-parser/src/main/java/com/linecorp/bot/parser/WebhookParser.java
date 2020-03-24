/*
 * Copyright 2019 LINE Corporation
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
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.event.CallbackRequest;
import com.linecorp.bot.model.objectmapper.ModelObjectMapper;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebhookParser {
    public static final String SIGNATURE_HEADER_NAME = "X-Line-Signature";

    private final ObjectMapper objectMapper = ModelObjectMapper.createNewObjectMapper();
    private final SignatureValidator signatureValidator;

    /**
     * Creates a new instance.
     *
     * @param signatureValidator LINE messaging API's signature validator
     */
    public WebhookParser(@NonNull SignatureValidator signatureValidator) {
        this.signatureValidator = signatureValidator;
    }

    /**
     * Parses a request.
     *
     * @param signature X-Line-Signature header.
     * @param payload Request body.
     * @return Parsed result. If there's an error, this method sends response.
     * @throws WebhookParseException There's an error around signature.
     */
    public CallbackRequest handle(String signature, byte[] payload) throws IOException, WebhookParseException {
        // validate signature
        if (signature == null || signature.isEmpty()) {
            throw new WebhookParseException("Missing 'X-Line-Signature' header");
        }

        if (log.isDebugEnabled()) {
            log.debug("got: {}", new String(payload, StandardCharsets.UTF_8));
        }

        if (!signatureValidator.validateSignature(payload, signature)) {
            throw new WebhookParseException("Invalid API signature");
        }

        final CallbackRequest callbackRequest = objectMapper.readValue(payload, CallbackRequest.class);
        if (callbackRequest == null || callbackRequest.getEvents() == null) {
            throw new WebhookParseException("Invalid content");
        }
        return callbackRequest;
    }
}
