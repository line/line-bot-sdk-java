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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.isA;

import java.util.concurrent.ExecutionException;

import org.hamcrest.CustomTypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import com.linecorp.bot.client.exception.BadRequestException;
import com.linecorp.bot.client.exception.ForbiddenException;
import com.linecorp.bot.client.exception.LineMessagingException;
import com.linecorp.bot.client.exception.LineServerException;
import com.linecorp.bot.client.exception.TooManyRequestsException;
import com.linecorp.bot.client.exception.UnauthorizedException;
import com.linecorp.bot.model.error.ErrorResponse;

import lombok.SneakyThrows;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class LineMessagingClientImplWiremockTest extends AbstractWiremockTest {
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test(timeout = ASYNC_TEST_TIMEOUT)
    public void status400BadRequestTest() throws Exception {
        final ErrorResponse errorResponse =
                new ErrorResponse("Problem with the request", null);

        // Mocking
        mocking(400, errorResponse);

        // Expect
        expectedException.expect(ExecutionException.class);
        expectedException.expectCause(isA(BadRequestException.class));
        expectedException.expectCause(errorResponseIs(errorResponse));

        // Do
        lineMessagingClient.getMessageContent("TOKEN").get();
    }

    @Test(timeout = ASYNC_TEST_TIMEOUT)
    public void status401UnauthorizedTest() throws Exception {
        final ErrorResponse errorResponse =
                new ErrorResponse("Valid Channel access token is not specified", null);

        // Mocking
        mocking(401, errorResponse);

        // Expect
        expectedException.expect(ExecutionException.class);
        expectedException.expectCause(isA(UnauthorizedException.class));
        expectedException.expectCause(errorResponseIs(errorResponse));

        // Do
        lineMessagingClient.getMessageContent("TOKEN").get();
    }

    @Test(timeout = ASYNC_TEST_TIMEOUT)
    public void status403ForbiddenTest() throws Exception {
        final ErrorResponse errorResponse =
                new ErrorResponse("Not authorized to use the API.", null);

        // Mocking
        mocking(403, errorResponse);

        // Expect
        expectedException.expect(ExecutionException.class);
        expectedException.expectCause(isA(ForbiddenException.class));
        expectedException.expectCause(errorResponseIs(errorResponse));

        // Do
        lineMessagingClient.getMessageContent("TOKEN").get();
    }

    @Test(timeout = ASYNC_TEST_TIMEOUT)
    public void status429TooManyRequestsTest() throws Exception {
        final ErrorResponse errorResponse =
                new ErrorResponse("Exceeded the rate limit for API calls", null);

        // Mocking
        mocking(429, errorResponse);

        // Expect
        expectedException.expect(ExecutionException.class);
        expectedException.expectCause(isA(TooManyRequestsException.class));
        expectedException.expectCause(errorResponseIs(errorResponse));

        // Do
        lineMessagingClient.getMessageContent("TOKEN").get();
    }

    @Test(timeout = ASYNC_TEST_TIMEOUT)
    public void status500InternalServerErrorTest() throws Exception {
        final ErrorResponse errorResponse =
                new ErrorResponse("Error on the internal server", null);

        // Mocking
        mocking(500, errorResponse);

        // Expect
        expectedException.expect(ExecutionException.class);
        expectedException.expectCause(isA(LineServerException.class));
        expectedException.expectCause(errorResponseIs(errorResponse));

        // Do
        lineMessagingClient.getMessageContent("TOKEN").get();
    }

    private CustomTypeSafeMatcher<LineMessagingException> errorResponseIs(final ErrorResponse errorResponse) {
        return new CustomTypeSafeMatcher<LineMessagingException>("Error Response") {
            @Override
            protected boolean matchesSafely(LineMessagingException item) {
                assertThat(item.getErrorResponse())
                        .isEqualTo(errorResponse);

                return true;
            }
        };
    }
}
