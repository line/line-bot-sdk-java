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

package com.linecorp.bot.parser;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.jackson.ModelObjectMapper;
import com.linecorp.bot.webhook.model.CallbackRequest;

public class WebhookParser {
    public static final String SIGNATURE_HEADER_NAME = "X-Line-Signature";
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(WebhookParser.class);

    private final ObjectMapper objectMapper = ModelObjectMapper.createNewObjectMapper();
    private final SignatureValidator signatureValidator;
    private final SkipSignatureVerificationSupplier skipSignatureVerificationSupplier;

    /**
     * Creates a new instance.
     *
     * @param signatureValidator LINE messaging API's signature validator
     */
    public WebhookParser(SignatureValidator signatureValidator,
                         SkipSignatureVerificationSupplier skipSignatureVerificationSupplier) {
        this.signatureValidator = requireNonNull(signatureValidator);
        this.skipSignatureVerificationSupplier = requireNonNull(skipSignatureVerificationSupplier);
    }

    /**
     * Parses a request.
     *
     * @param signature X-Line-Signature header.
     * @param payload   Request body.
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

        if (!skipSignatureVerificationSupplier.getAsBoolean()
            && !signatureValidator.validateSignature(payload, signature)) {
            throw new WebhookParseException("Invalid API signature");
        }

        final CallbackRequest callbackRequest = objectMapper.readValue(payload, CallbackRequest.class);
        if (callbackRequest == null || callbackRequest.events() == null) {
            throw new WebhookParseException("Invalid content");
        }
        return callbackRequest;
    }
}
