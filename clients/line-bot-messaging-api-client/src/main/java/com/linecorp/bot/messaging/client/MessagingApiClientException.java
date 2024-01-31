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

package com.linecorp.bot.messaging.client;

import java.util.Collections;
import java.util.List;

import com.linecorp.bot.client.base.exception.AbstractLineClientException;
import com.linecorp.bot.messaging.model.ErrorDetail;
import com.linecorp.bot.messaging.model.SentMessage;

import okhttp3.Response;

/**
 * A general {@link Exception} for LINE Messaging API.
 */
@SuppressWarnings("serial")
public class MessagingApiClientException extends AbstractLineClientException {
    /**
     * An error summary.
     */
    private final String error;

    /**
     * Details of the error. Not returned in certain situations.
     */
    private final List<ErrorDetail> details;

    /**
     * Array of sent messages.
     */
    private final List<SentMessage> sentMessages;

    public MessagingApiClientException(Response response, String error, List<ErrorDetail> details, List<SentMessage> sentMessages) {
        super(response, "error='" + error + "' details='" + details + "' sentMessages='" + sentMessages + "'");
        this.error = error;
        this.details = details;
        this.sentMessages = sentMessages;
    }

    public String getError() {
        return error;
    }

    public List<ErrorDetail> getDetails() {
        if (details == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(details);
    }

    public List<SentMessage> getSentMessages() {
        if (sentMessages == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(sentMessages);
    }
}
