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

import com.linecorp.bot.jackson.ModelObjectMapper;

import okhttp3.Response;
import okhttp3.ResponseBody;
import tools.jackson.core.JacksonException;
import tools.jackson.core.exc.JacksonIOException;
import tools.jackson.core.exc.StreamConstraintsException;
import tools.jackson.databind.ObjectMapper;

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
        } catch (ErrorBodyParseException e) {
            return new LineClientJsonParseException(response, e);
        } catch (JacksonException e) {
            IOException failure = asIOException(e);
            if (failure instanceof ErrorBodyParseException) {
                return new LineClientJsonParseException(response, failure);
            }
            throw failure;
        }
    }

    protected abstract IOException buildException(Response response, E errorBody);

    protected E parseErrorBody(Response response) throws IOException {
        ResponseBody body = requireNonNull(response.body(), "response.body");
        try {
            return objectMapper.readValue(requireNonNull(body.byteStream()), errorBodyClass);
        } catch (JacksonException e) {
            throw asIOException(e);
        }
    }

    private static IOException asIOException(JacksonException exception) {
        if (exception instanceof JacksonIOException ioException) {
            return ioException.getCause();
        }
        // Constraints outside property binding previously escaped without a databinding wrapper.
        if (exception instanceof StreamConstraintsException && exception.getPath().isEmpty()) {
            return new IOException(exception.getMessage(), exception);
        }
        return new ErrorBodyParseException(exception);
    }

    // Keep parse failures catchable as IOException without misclassifying transport failures.
    @SuppressWarnings("serial")
    private static final class ErrorBodyParseException extends IOException {
        private ErrorBodyParseException(JacksonException cause) {
            super(cause.getMessage(), cause);
        }
    }
}
