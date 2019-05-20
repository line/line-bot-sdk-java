/*
 * Copyright 2019 LINE Corporation
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

package com.linecorp.bot.model.oauth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;

/**
 * A general {@link Exception} for LINE channel access token API.
 */
@Getter
@ToString
@SuppressWarnings("serial")
public class ChannelAccessTokenException extends RuntimeException {
    /**
     * An error summary.
     */
    private final String error;

    /**
     * Details of the error. Not returned in certain situations.
     */
    private final String errorDescription;

    @JsonCreator
    public ChannelAccessTokenException(@JsonProperty("error") String error,
                                       @JsonProperty("error_description") String errorDescription) {
        this.error = error;
        this.errorDescription = errorDescription;
    }

    public ChannelAccessTokenException(String error, String errorDescription, Throwable thrown) {
        super(error, thrown);
        this.error = error;
        this.errorDescription = errorDescription;
    }

    public ChannelAccessTokenException(String message) {
        super(message);
        this.error = message;
        this.errorDescription = null;
    }

    public ChannelAccessTokenException(String message, Throwable t) {
        super(message, t);
        error = message;
        this.errorDescription = null;
    }
}
