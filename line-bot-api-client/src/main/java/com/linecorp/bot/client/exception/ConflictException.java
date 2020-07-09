/*
 * Copyright 2020 LINE Corporation
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

package com.linecorp.bot.client.exception;

import com.linecorp.bot.model.error.ErrorResponse;

/**
 * <p>Messaging API returns `409 Conflict`.</p>
 *
 * <p>Typically, Messaging API returns `409 Conflict` when call API with `X-Line-Retry-Key` and it's conflicted.
 * </p>
 * <p>See <a href="https://developers.line.biz/en/docs/messaging-api/retrying-api-request/#summary">Retrying a
 * failed API request</a></p>
 */
public class ConflictException extends LineMessagingException {
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    public ConflictException(
            final String message,
            final ErrorResponse errorResponse) {
        super(message, errorResponse, null);
    }
}
