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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.linecorp.bot.client.LineMessagingClientImpl.ResponseBodyCallbackAdaptor;
import com.linecorp.bot.client.exception.GeneralLineMessagingException;
import com.linecorp.bot.client.exception.UnauthorizedException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class ResponseBodyCallbackAdaptorTest {
    private ResponseBodyCallbackAdaptor target;

    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public final Timeout timeoutRule = Timeout.seconds(1);

    @Mock
    private Call<ResponseBody> call;

    @Before
    public void setUp() throws Exception {
        target = new ResponseBodyCallbackAdaptor();
    }

    @Test
    public void onResponseSuccessTest() throws Exception {
        Response<ResponseBody> response =
                Response.success(ResponseBody.create(MediaType.parse("image/jpeg"),
                                                     ""));

        // Do
        target.onResponse(call, response);

        // Verify
        assertThat(target).isCompleted();
        final MessageContentResponse messageContentResponse = target.get();
        assertThat(messageContentResponse.getLength()).isEqualTo(0);
        assertThat(messageContentResponse.getStream())
                .hasSameContentAs(new ByteArrayInputStream(new byte[] {}));
        assertThat(messageContentResponse.getAllHeaders())
                .isEmpty();
    }

    @Test
    public void onResponseFailTest() throws Exception {
        Response<ResponseBody> response =
                Response.error(401, ResponseBody.create(MediaType.parse("text/javascript"),
                                                        "{}"));

        // Do
        target.onResponse(call, response);

        // Verify
        assertThat(target).isCompletedExceptionally();

        final Throwable t = target.handle((ignored, e) -> e).get();
        assertThat(t).isInstanceOf(UnauthorizedException.class);
    }

    @Test
    public void onFailureTest() throws Exception {
        IOException t = mock(IOException.class);
        when(t.getMessage()).thenReturn("ResponseBodyCallbackAdaptorTest Failed");

        // Do
        target.onFailure(call, t);

        // Verify
        assertThat(target).isCompletedExceptionally();
        assertThat(target.handle((ignored, e) -> e).get())
                .isInstanceOf(GeneralLineMessagingException.class)
                .hasMessageContaining("ResponseBodyCallbackAdaptorTest Failed");
    }
}
