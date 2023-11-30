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

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.linecorp.bot.messaging.model.ErrorDetail;
import com.linecorp.bot.messaging.model.SentMessage;

public class MessagingApiClientExceptionTest {

    private Response response;

    @BeforeEach
    void setUp() {
        response = mock(Response.class);
        Request request = new Request.Builder().url("https://example.com/").build();

        when(response.request()).thenReturn(request);
        when(response.code()).thenReturn(200);

        Headers headers = Headers.of(
                "x-line-example", "headerValue",
                "x-line-request-id", "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
        );
        when(response.headers()).thenReturn(headers);
    }

    @Test
    void constructAndGetTest() {
        List<ErrorDetail> errorDetails = Collections.singletonList(new ErrorDetail("message", "property"));
        List<SentMessage> sentMessages = Collections.singletonList(new SentMessage("IDIDID", "QUOQUOQUO"));
        String error = "testError";

        MessagingApiClientException exception =
                new MessagingApiClientException(response, error, errorDetails, sentMessages);

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(
                "API returns error: code=" + response.code() +
                        " requestUrl=" + response.request().url() +
                        " x-line-example=headerValue x-line-request-id=xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx " +
                        "error='testError' " +
                        "details='[ErrorDetail[message=message, property=property]]'" +
                        " sentMessages='[SentMessage[id=IDIDID, quoteToken=QUOQUOQUO]]'");
        assertThat(exception.getError()).isEqualTo(error);
        assertThat(exception.getDetails()).isEqualTo(errorDetails);
        assertThat(exception.getSentMessages()).isEqualTo(sentMessages);
    }
}
