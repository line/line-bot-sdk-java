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

import javax.servlet.http.HttpServletRequest;

import com.google.common.io.ByteStreams;

import com.linecorp.bot.model.event.CallbackRequest;
import com.linecorp.bot.parser.LineSignatureValidator;
import com.linecorp.bot.parser.WebhookParseException;
import com.linecorp.bot.parser.WebhookParser;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Parser of webhook from LINE's messaging server.
 *
 * <p>This is a thin wrapper of {@link WebhookParser}.
 *
 * @see WebhookParser
 */
@Slf4j
public class LineBotCallbackRequestParser {
    private final WebhookParser parser;

    /**
     * Creates a new instance.
     *
     * @param lineSignatureValidator LINE messaging API's signature validator
     */
    public LineBotCallbackRequestParser(@NonNull LineSignatureValidator lineSignatureValidator) {
        parser = new WebhookParser(lineSignatureValidator);
    }

    /**
     * Parses a request.
     *
     * @param req HTTP servlet request.
     * @return Parsed result. If there's an error, this method sends response.
     * @throws LineBotCallbackException There's an error around signature.
     */
    public CallbackRequest handle(HttpServletRequest req) throws LineBotCallbackException, IOException {
        // validate signature
        final String signature = req.getHeader(WebhookParser.SIGNATURE_HEADER_NAME);
        final byte[] json = ByteStreams.toByteArray(req.getInputStream());
        try {
            return parser.handle(signature, json);
        } catch (WebhookParseException e) {
            throw new LineBotCallbackException(e.getMessage(), e);
        }
    }
}
