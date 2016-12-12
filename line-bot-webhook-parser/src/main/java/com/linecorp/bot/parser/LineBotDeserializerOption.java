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

import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;

import com.linecorp.bot.model.event.Event;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class LineBotDeserializerOption {
    private final DeserializeExceptionHandler deserializeExceptionHandler;
    private final boolean skipNull;

    /**
     * event deserialize exception handler
     *
     * @param deserializeExceptionHandler deserialize exception handler
     * @param skipNull if exception handler return null, skip the event
     */
    public LineBotDeserializerOption(
            DeserializeExceptionHandler deserializeExceptionHandler,
            boolean skipNull
    ) {
        this.deserializeExceptionHandler = deserializeExceptionHandler;
        this.skipNull = skipNull;
    }

    /**
     * warn logging and skip unknown event option
     */
    public static LineBotDeserializerOption getDefault() {
        return new LineBotDeserializerOption(
                getWarnLoggingAndReturnNullHandler(),
                true
        );
    }

    /**
     * throw exception handler
     */
    public static DeserializeExceptionHandler getThrowExceptionHandler() {
        return th -> {
            if (th instanceof InvalidTypeIdException) {
                throw new LineBotWebhookParseException(
                        "Unknown `type` Found, Please Upgrade module: " + th.getMessage(), th);
            } else {
                throw new LineBotWebhookParseException(
                        "Failed to deserialize events. Please Upgrade module. " + th.getMessage(), th);
            }
        };
    }

    /**
     * warn logging and return null handler
     */
    public static DeserializeExceptionHandler getWarnLoggingAndReturnNullHandler() {
        return th -> {
            if (th instanceof InvalidTypeIdException) {
                log.warn("Unknown `type` Found, Please Upgrade module (this event is skipped): {}",
                         th.getMessage());
            } else {
                log.warn("Failed to deserialize, this event is skipped. Please Upgrade module: {}",
                         th.getMessage());
            }
            return null;
        };
    }

    @FunctionalInterface
    public interface DeserializeExceptionHandler {
        Event apply(Throwable th) throws LineBotWebhookParseException;
    }
}
