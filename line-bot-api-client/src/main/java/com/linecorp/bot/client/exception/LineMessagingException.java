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

package com.linecorp.bot.client.exception;

import com.linecorp.bot.model.error.ErrorResponse;

import lombok.Getter;

@Getter
public abstract class LineMessagingException extends Exception {
    static final long SERIAL_VERSION_UID = 0x001_003; // 1.3.x
    private static final long serialVersionUID = SERIAL_VERSION_UID;

    /**
     * Original error response from server.
     *
     * Null when error response is not exist.
     */
    private final ErrorResponse errorResponse;

    LineMessagingException(final String message, final ErrorResponse errorResponse,
                           final Throwable cause) {
        super(message, cause);
        this.errorResponse = errorResponse;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + errorResponse;
    }
}
