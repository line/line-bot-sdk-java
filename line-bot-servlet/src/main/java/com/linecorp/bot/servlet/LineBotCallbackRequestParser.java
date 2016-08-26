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

import com.linecorp.bot.client.LineBotAPIHeaders;
import com.linecorp.bot.client.LineBotClient;
import com.linecorp.bot.client.exception.LineBotAPIException;
import com.linecorp.bot.client.exception.LineBotAPIJsonProcessingException;
import com.linecorp.bot.model.v2.event.CallbackRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LineBotCallbackRequestParser {
    private final LineBotClient lineBotClient;

    public LineBotCallbackRequestParser(LineBotClient lineBotClient) {
        this.lineBotClient = lineBotClient;
    }

    public CallbackRequest handle(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // validate signature
        String signature = req.getHeader(LineBotAPIHeaders.X_LINE_CHANNEL_SIGNATURE);
        if (signature == null || signature.length() == 0) {
            sendError(resp, "Missing 'X-Line-ChannelSignature' header");
            return null;
        }

        final byte[] json = IOUtils.toByteArray(req.getInputStream());
        if (log.isDebugEnabled()) {
            log.debug("got: {}", new String(json, StandardCharsets.UTF_8));
        }

        try {
            if (!lineBotClient.validateSignature(json, signature)) {
                sendError(resp, "Invalid API signature");
                return null;
            }
        } catch (LineBotAPIException e) {
            log.warn("Invalid API signature", e);
            sendError(resp, "Internal serve error: " + e.getMessage());
            return null;
        }

        try {
            return lineBotClient.readCallbackRequest(json);
        } catch (LineBotAPIJsonProcessingException e) {
            sendError(resp, "Invalid Callback");
            return null;
        }
    }

    private void sendError(HttpServletResponse resp, String message) throws IOException {
        log.warn(message);
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                       message);
        resp.setContentType("text/plain; charset=utf-8");
        resp.getWriter().write(message);
    }
}
