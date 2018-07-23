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

package com.linecorp.bot.servlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;

import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.model.event.CallbackRequest;
import com.linecorp.bot.model.objectmapper.ModelObjectMapper;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LineBotCallbackRequestParser {
    private final ObjectMapper objectMapper = ModelObjectMapper.createNewObjectMapper();
    private final LineSignatureValidator lineSignatureValidator;

    /**
     * Create new instance.
     *
     * @param lineSignatureValidator LINE messaging API's signature validator
     */
    public LineBotCallbackRequestParser(
            @NonNull LineSignatureValidator lineSignatureValidator) {
        this.lineSignatureValidator = lineSignatureValidator;
    }

    /**
     * Parse request.
     *
     * @param req HTTP servlet request.
     *
     * @return Parsed result. If there's an error, this method sends response.
     *
     * @throws LineBotCallbackException There's an error around signature.
     */
    public CallbackRequest handle(HttpServletRequest req) throws LineBotCallbackException, IOException {
        // validate signature
        String signature = req.getHeader("X-Line-Signature");
        final byte[] json = ByteStreams.toByteArray(req.getInputStream());
        return handle(signature, new String(json, StandardCharsets.UTF_8));
    }

    /**
     * Parse request.
     *
     * @param signature X-Line-Signature header.
     * @param payload Request body.
     *
     * @return Parsed result. If there's an error, this method sends response.
     *
     * @throws LineBotCallbackException There's an error around signature.
     */
    public CallbackRequest handle(String signature, String payload)
            throws LineBotCallbackException, IOException {
        // validate signature
        if (signature == null || signature.length() == 0) {
            throw new LineBotCallbackException("Missing 'X-Line-Signature' header");
        }

        log.debug("got: {}", payload);

        final byte[] json = payload.getBytes(StandardCharsets.UTF_8);

        if (!lineSignatureValidator.validateSignature(json, signature)) {
            throw new LineBotCallbackException("Invalid API signature");
        }

        final CallbackRequest callbackRequest = objectMapper.readValue(json, CallbackRequest.class);
        if (callbackRequest == null || callbackRequest.getEvents() == null) {
            throw new LineBotCallbackException("Invalid content");
        }
        return callbackRequest;
    }
}
