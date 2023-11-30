/*
 * Copyright 2023 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.linecorp.bot.client.base.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;


public class AbstractLineClientExceptionTest {

    private Response response;

    @BeforeEach
    void setUp() {
        response = mock(Response.class);
        Request request = new Request.Builder().url("https://example.com").build();

        when(response.request()).thenReturn(request);
        when(response.code()).thenReturn(200);
        Headers headers = Headers.of(Map.of(
                "x-line-example", "headerValue",
                "x-line-request-id", "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
        ));
        when(response.headers()).thenReturn(headers);
    }

    @Test
    void constructorWithIoException() {
        IOException ioException = new IOException("test exception");
        AbstractLineClientException exception = new AbstractLineClientException(response, "test message", ioException);

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("API returns error: code=200 requestUrl=https://example.com/ x-line-example=headerValue x-line-request-id=xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx test message");        assertThat(exception.getCode()).isEqualTo(200);
        assertThat(exception.getCause()).isEqualTo(ioException);
    }

    @Test
    void constructorNoException() {
        AbstractLineClientException exception = new AbstractLineClientException(response, "test message");

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("API returns error: code=200 requestUrl=https://example.com/ x-line-example=headerValue x-line-request-id=xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx test message");        assertThat(exception.getCode()).isEqualTo(200);
        assertThat(exception.getCause()).isNull();
    }

    @Test
    void getRequestUrl() throws Exception {
        AbstractLineClientException exception = new AbstractLineClientException(response, "test message");
        assertThat(exception.getRequestUrl()).isEqualTo(new URI("https://example.com/").toURL());
    }

    @Test
    void getRequestId() {
        AbstractLineClientException exception = new AbstractLineClientException(response, "test message");
        assertThat(exception.getRequestId()).isEqualTo("xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx");
    }

    @Test
    void getHeader() {
        AbstractLineClientException exception = new AbstractLineClientException(response, "test message");
        assertThat(exception.getHeader("x-line-example")).isEqualTo("headerValue");
    }
}
