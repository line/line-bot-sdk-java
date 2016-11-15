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

import static okhttp3.MediaType.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.linecorp.bot.client.LineMessagingClientImpl.CallbackAdaptor;
import com.linecorp.bot.client.exception.GeneralLineMessagingException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class CallbackAdaptorTest {
    private CallbackAdaptor<Object> target;

    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public final Timeout timeoutRule = Timeout.seconds(1);

    @Mock
    private Call<Object> call;

    @Before
    public void setUp() throws Exception {
        target = new CallbackAdaptor<>();
    }

    @Test
    public void onResponseSuccessfullyTest() throws Exception {
        final Object value = new Object();
        Response<Object> response = Response.success(value);

        // Do
        target.onResponse(call, response);

        // Verify
        assertThat(target).isCompletedWithValue(value);
    }

    @Test
    public void onResponseWithErrorTest() throws Exception {
        Response<Object> response =
                Response.error(400, ResponseBody.create(parse("application/json"), "{}"));

        // Do
        target.onResponse(call, response);

        // Verify
        assertThat(target).isCompletedExceptionally();
    }

    @Test
    public void onFailureTest() throws Exception {
        Throwable t = mock(Throwable.class);
        when(t.getMessage()).thenReturn("Message");

        // Do
        target.onFailure(call, t);

        // Verify
        assertThat(target).isCompletedExceptionally();
        assertThat(target.handle((ignored, e) -> e).get())
                .isInstanceOf(GeneralLineMessagingException.class)
                .withFailMessage("Message");
    }
}
