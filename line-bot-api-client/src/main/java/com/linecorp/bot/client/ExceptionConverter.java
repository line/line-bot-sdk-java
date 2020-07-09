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

package com.linecorp.bot.client;

import static java.util.Collections.singletonMap;

import java.io.IOException;
import java.util.function.Function;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import com.linecorp.bot.client.exception.BadRequestException;
import com.linecorp.bot.client.exception.ConflictException;
import com.linecorp.bot.client.exception.ForbiddenException;
import com.linecorp.bot.client.exception.GeneralLineMessagingException;
import com.linecorp.bot.client.exception.LineMessagingException;
import com.linecorp.bot.client.exception.LineServerException;
import com.linecorp.bot.client.exception.NotFoundException;
import com.linecorp.bot.client.exception.TooManyRequestsException;
import com.linecorp.bot.client.exception.UnauthorizedException;
import com.linecorp.bot.model.error.ErrorResponse;

import okhttp3.ResponseBody;
import retrofit2.Response;

class ExceptionConverter implements Function<Response<?>, LineMessagingException> {
    public static final ObjectReader OBJECT_READER = new ObjectMapper().readerFor(ErrorResponse.class);

    @Override
    public LineMessagingException apply(Response<?> response) {
        final String requestId = response.headers().get("x-line-request-id");
        try {
            return applyInternal(requestId, response);
        } catch (Exception e) {
            final ErrorResponse errorResponse = new ErrorResponse(requestId, null, null);
            return new GeneralLineMessagingException(e.getMessage(), errorResponse, e);
        }
    }

    private static LineMessagingException applyInternal(final String requestId, final Response<?> response)
            throws IOException {
        final int code = response.code();
        final ResponseBody responseBody = response.errorBody();

        final ErrorResponse errorResponse = OBJECT_READER
                .with(new InjectableValues.Std(singletonMap("requestId", requestId)))
                .readValue(responseBody.byteStream());

        switch (code) {
            case 400:
                return new BadRequestException(
                        errorResponse.getMessage(), errorResponse);
            case 401:
                return new UnauthorizedException(
                        errorResponse.getMessage(), errorResponse);
            case 403:
                return new ForbiddenException(
                        errorResponse.getMessage(), errorResponse);
            case 404:
                return new NotFoundException(
                        errorResponse.getMessage(), errorResponse);
            case 409:
                return new ConflictException(
                        errorResponse.getMessage(), errorResponse);
            case 429:
                return new TooManyRequestsException(
                        errorResponse.getMessage(), errorResponse);
            case 500:
                return new LineServerException(
                        errorResponse.getMessage(), errorResponse);
        }

        return new GeneralLineMessagingException(errorResponse.getMessage(), errorResponse, null);
    }
}
