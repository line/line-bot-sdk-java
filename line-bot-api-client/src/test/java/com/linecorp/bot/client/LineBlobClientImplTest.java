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

package com.linecorp.bot.client;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

import com.linecorp.bot.model.response.BotApiResponse;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@ExtendWith(MockitoExtension.class)
@Timeout(5)
public class LineBlobClientImplTest {
    private static final byte[] ZERO_BYTES = {};
    private static final String REQUEST_ID_FIXTURE = "REQUEST_ID_FIXTURE";
    private static final BotApiResponseBody BOT_API_SUCCESS_RESPONSE_BODY =
            new BotApiResponseBody("", emptyList());
    private static final BotApiResponse BOT_API_SUCCESS_RESPONSE =
            BOT_API_SUCCESS_RESPONSE_BODY.withRequestId(REQUEST_ID_FIXTURE);

    @Mock
    private LineBlobService retrofitMock;

    @InjectMocks
    private LineBlobClientImpl target;

    @Test
    public void getMessageContentTest() throws Exception {
        whenCall(retrofitMock.getMessageContent(any()),
                 ResponseBody.create(MediaType.parse("image/jpeg"), ZERO_BYTES));

        // Do
        final MessageContentResponse contentResponse = target.getMessageContent("ID").get();

        // Verify
        verify(retrofitMock, only()).getMessageContent("ID");
        assertThat(contentResponse.getLength()).isEqualTo(0);
        assertThat(contentResponse.getMimeType()).isEqualTo("image/jpeg");
    }

    @Test
    public void getRichMenuImageTest() throws Exception {
        whenCall(retrofitMock.getRichMenuImage(any()),
                 ResponseBody.create(MediaType.parse("image/jpeg"), ZERO_BYTES));

        // Do
        final MessageContentResponse messageContentResponse = target.getRichMenuImage("ID").get();

        // Verify
        verify(retrofitMock, only()).getRichMenuImage("ID");
        assertThat(messageContentResponse.getLength()).isZero();
    }

    @Test
    public void uploadRichMenuImageTest() throws Exception {
        whenCall(retrofitMock.uploadRichMenuImage(any(), any()),
                 null);

        // Do
        final BotApiResponse botApiResponse =
                target.setRichMenuImage("ID", "image/jpeg", ZERO_BYTES).get();

        // Verify
        verify(retrofitMock, only())
                .uploadRichMenuImage(eq("ID"), any());
        assertThat(botApiResponse).isEqualTo(BOT_API_SUCCESS_RESPONSE);

    }

    // Utility methods

    private static <T> void whenCall(Call<T> call, T value) {
        final OngoingStubbing<Call<T>> callOngoingStubbing = when(call);
        callOngoingStubbing.thenReturn(enqueue(value));
    }

    private static <T> Call<T> enqueue(T value) {
        return new Call<T>() {
            @Override
            public Response<T> execute() throws IOException {
                throw new UnsupportedOperationException();
            }

            @Override
            public void enqueue(Callback<T> callback) {
                final Headers headers = Headers.of(singletonMap("x-line-request-id", REQUEST_ID_FIXTURE));
                callback.onResponse(this, Response.success(value, headers));
            }

            @Override
            public boolean isExecuted() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void cancel() {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean isCanceled() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Call<T> clone() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Request request() {
                throw new UnsupportedOperationException();
            }

            @Override
            public okio.Timeout timeout() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
