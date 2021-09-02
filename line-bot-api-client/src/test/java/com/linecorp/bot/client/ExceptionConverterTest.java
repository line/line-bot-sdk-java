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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.linecorp.bot.client.exception.GeneralLineMessagingException;
import com.linecorp.bot.client.exception.LineMessagingException;
import com.linecorp.bot.client.exception.UnauthorizedException;

import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response.Builder;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class ExceptionConverterTest {
    private final ExceptionConverter target = new ExceptionConverter();

    @Test
    public void convertTest() {
        final ResponseBody responseBody =
                ResponseBody.create(MediaType.parse("application/json"),
                                    "{}");
        final LineMessagingException result =
                target.apply(Response.error(401, responseBody));

        assertThat(result)
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    public void convertUnknownExceptionTest() {
        final ResponseBody responseBody =
                ResponseBody.create(MediaType.parse("application/json"),
                                    "{}");
        final LineMessagingException result =
                target.apply(Response.error(999, responseBody));

        assertThat(result)
                .isInstanceOf(GeneralLineMessagingException.class);
    }

    @Test
    public void exceptionInConvertFallbackedTest() {
        final ResponseBody responseBody = mock(ResponseBody.class);
        when(responseBody.source()).thenThrow(new RuntimeException());

        final LineMessagingException result =
                target.apply(Response.error(401, responseBody));

        assertThat(result)
                .isInstanceOf(GeneralLineMessagingException.class);
    }

    @Test
    public void requestIdDeserializationTest() {
        final ResponseBody responseBody =
                ResponseBody.create(MediaType.parse("application/json"),
                                    "{\"message\":\"Invalid reply token\"}");

        final okhttp3.Response rawResponse = new Builder()
                .code(400)
                .message("")
                .request(new Request.Builder().get().url("https://api.line.me/v2/bot/message/reply").build())
                .addHeader("X-Line-Request-Id", "5ac44e02-e6be-49c3-a55f-6b2a29bc3aa4")
                .protocol(Protocol.HTTP_1_1)
                .build();

        // Precondition
        assertThat(rawResponse.header("X-Line-Request-Id")).isEqualTo("5ac44e02-e6be-49c3-a55f-6b2a29bc3aa4");

        // Do
        final LineMessagingException result =
                target.apply(Response.error(responseBody, rawResponse));

        // Verify
        assertThat(result.getErrorResponse().getRequestId()).isEqualTo("5ac44e02-e6be-49c3-a55f-6b2a29bc3aa4");
    }
}
