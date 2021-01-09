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

package com.linecorp.bot.model.manageaudience;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;

/**
 * A general {@link Exception} for LINE manage audience API.
 */
@Getter
@ToString
@SuppressWarnings("serial")
public class ManageAudienceException extends RuntimeException {
    /**
     * Error summary.
     */
    private final String message;

    /**
     * A description of the error.
     */
    private final String details;

    @JsonCreator
    public ManageAudienceException(@JsonProperty("message") String message,
                                   @JsonProperty("details") String details) {
        this.message = message;
        this.details = details;
    }

    public ManageAudienceException(String message) {
        super(message);
        this.message = message;
        this.details = null;
    }

    public ManageAudienceException(String message, Throwable t) {
        super(message, t);
        this.message = message;
        this.details = t.getMessage();
    }
}
