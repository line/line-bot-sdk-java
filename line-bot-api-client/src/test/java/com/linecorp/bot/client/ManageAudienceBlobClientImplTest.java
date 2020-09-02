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

package com.linecorp.bot.client;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.assertj.core.util.Files;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.OngoingStubbing;

import com.linecorp.bot.model.manageaudience.response.CreateAudienceForUploadingUserIdsResponse;

import okhttp3.Headers;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageAudienceBlobClientImplTest {
    private static final String REQUEST_ID_FIXTURE = "REQUEST_ID_FIXTURE";

    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public final Timeout timeoutRule = Timeout.seconds(5);

    @Mock
    private ManageAudienceBlobService retrofitMock;

    @InjectMocks
    private ManageAudienceBlobClientImpl target;

    @Test
    public void createAudienceForUploadingUserIds() throws Exception {
        CreateAudienceForUploadingUserIdsResponse response =
                CreateAudienceForUploadingUserIdsResponse.builder()
                                                         .build();

        whenCall(retrofitMock.createAudienceForUploadingUserIds(
                any()), response);

        File tmpFile = Files.newTemporaryFile();
        tmpFile.deleteOnExit();
        final CreateAudienceForUploadingUserIdsResponse actual =
                target.createAudienceForUploadingUserIds(
                        "Hello",
                        false,
                        "UPLOAD!",
                        tmpFile
                )
                      .get();
        // TODO
//        verify(retrofitMock, only()).createAudienceForUploadingUserIds(
//                "Hello", false, "UPLOAD!", tmpFile);
        assertThat(actual).isEqualTo(response);
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
