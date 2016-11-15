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

package com.linecorp.bot.model.error;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

/**
 * Error response from LINE Messaging Server.
 *
 * @see <a href="https://devdocs.line.me/#error-response">//devdocs.line.me/#error-response</a>
 */
@Value
public class ErrorResponse {
    /** Summary or details of the error. */
    String message;

    /**
     * Details of the error.
     *
     * In this class, always non-null but can be empty.
     */
    List<ErrorDetail> details;

    public ErrorResponse(
            @JsonProperty("message") final String message,
            @JsonProperty("details") final List<ErrorDetail> details) {
        this.message = message;
        this.details = details != null ? details : Collections.emptyList();
    }
}
