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

package com.linecorp.bot.client.base;

import static java.util.Objects.requireNonNull;

import java.io.IOException;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.jackson.ModelObjectMapper;

import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class AbstractExceptionBuilder<E> implements ExceptionBuilder {
    private final ObjectMapper objectMapper = ModelObjectMapper.createNewObjectMapper();
    private final Class<E> errorBodyClass;

    public AbstractExceptionBuilder(Class<E> errorBodyClass) {
        this.errorBodyClass = errorBodyClass;
    }

    public IOException build(Response response) throws IOException {
        try {
            E errorBody = this.parseErrorBody(response);
            return this.buildException(response, errorBody);
        } catch (StreamReadException | DatabindException e) {
            return new LineClientJsonParseException(response, e);
        }
    }

    protected abstract IOException buildException(Response response, E errorBody);

    protected E parseErrorBody(Response response) throws IOException, StreamReadException, DatabindException {
        ResponseBody body = requireNonNull(response.body(), "response.body");
        return objectMapper.readValue(
                requireNonNull(body.byteStream()),
                errorBodyClass
        );
    }
}
