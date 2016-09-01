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
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.model.event.CallbackRequest;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LineBotCallbackRequestParser {
    private final LineSignatureValidator lineSignatureValidator;
    private final ObjectMapper objectMapper;

    public LineBotCallbackRequestParser(
            @NonNull LineSignatureValidator lineSignatureValidator,
            @NonNull ObjectMapper objectMapper) {
        this.lineSignatureValidator = lineSignatureValidator;
        this.objectMapper = objectMapper;
    }

    public CallbackRequest handle(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // validate signature
        String signature = req.getHeader("X-Line-Signature");
        if (signature == null || signature.length() == 0) {
            sendError(resp, "Missing 'X-Line-Signature' header");
            return null;
        }

        final byte[] json = IOUtils.toByteArray(req.getInputStream());
        if (log.isDebugEnabled()) {
            log.debug("got: {}", new String(json, StandardCharsets.UTF_8));
        }

        if (!lineSignatureValidator.validateSignature(json, signature)) {
            sendError(resp, "Invalid API signature");
            return null;
        }

        final CallbackRequest callbackRequest = objectMapper.readValue(json, CallbackRequest.class);
        if (callbackRequest == null || callbackRequest.getEvents() == null) {
            sendError(resp, "Invalid content");
            return null;
        }
        return callbackRequest;
    }

    private static void sendError(HttpServletResponse resp, String message) throws IOException {
        log.warn(message);
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                       message);
        resp.setContentType("text/plain; charset=utf-8");
        resp.getWriter().write(message);
    }
}
