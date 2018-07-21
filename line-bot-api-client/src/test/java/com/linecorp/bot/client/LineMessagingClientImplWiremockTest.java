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
import static org.hamcrest.CoreMatchers.isA;

import java.util.concurrent.ExecutionException;

import org.hamcrest.CustomTypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.client.exception.BadRequestException;
import com.linecorp.bot.client.exception.ForbiddenException;
import com.linecorp.bot.client.exception.LineMessagingException;
import com.linecorp.bot.client.exception.LineServerException;
import com.linecorp.bot.client.exception.NotFoundException;
import com.linecorp.bot.client.exception.TooManyRequestsException;
import com.linecorp.bot.client.exception.UnauthorizedException;
import com.linecorp.bot.model.error.ErrorResponse;
import com.linecorp.bot.model.profile.UserProfileResponse;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

public class LineMessagingClientImplWiremockTest extends AbstractWiremockTest {
    static final ErrorResponse ERROR_RESPONSE =
            new ErrorResponse(null, "Error Message", null);

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test(timeout = ASYNC_TEST_TIMEOUT)
    public void status400BadRequestTest() throws Exception {
        // Mocking
        mocking(400, ERROR_RESPONSE);

        // Expect
        expectedException.expect(ExecutionException.class);
        expectedException.expectCause(isA(BadRequestException.class));
        expectedException.expectCause(errorResponseIs(ERROR_RESPONSE));

        // Do
        lineMessagingClient.getMessageContent("TOKEN").get();
    }

    @Test(timeout = ASYNC_TEST_TIMEOUT)
    public void status401UnauthorizedTest() throws Exception {
        // Mocking
        mocking(401, ERROR_RESPONSE);

        // Expect
        expectedException.expect(ExecutionException.class);
        expectedException.expectCause(isA(UnauthorizedException.class));
        expectedException.expectCause(errorResponseIs(ERROR_RESPONSE));

        // Do
        lineMessagingClient.getMessageContent("TOKEN").get();
    }

    @Test(timeout = ASYNC_TEST_TIMEOUT)
    public void status403ForbiddenTest() throws Exception {
        // Mocking
        mocking(403, ERROR_RESPONSE);

        // Expect
        expectedException.expect(ExecutionException.class);
        expectedException.expectCause(isA(ForbiddenException.class));
        expectedException.expectCause(errorResponseIs(ERROR_RESPONSE));

        // Do
        lineMessagingClient.getMessageContent("TOKEN").get();
    }

    @Test(timeout = ASYNC_TEST_TIMEOUT)
    public void status404NotFoundTest() throws Exception {
        // Mocking
        mocking(404, ERROR_RESPONSE);

        // Expect
        expectedException.expect(ExecutionException.class);
        expectedException.expectCause(isA(NotFoundException.class));
        expectedException.expectCause(errorResponseIs(ERROR_RESPONSE));

        // Do
        lineMessagingClient.getMessageContent("TOKEN").get();
    }

    @Test(timeout = ASYNC_TEST_TIMEOUT)
    public void status429TooManyRequestsTest() throws Exception {
        // Mocking
        mocking(429, ERROR_RESPONSE);

        // Expect
        expectedException.expect(ExecutionException.class);
        expectedException.expectCause(isA(TooManyRequestsException.class));
        expectedException.expectCause(errorResponseIs(ERROR_RESPONSE));

        // Do
        lineMessagingClient.getMessageContent("TOKEN").get();
    }

    @Test(timeout = ASYNC_TEST_TIMEOUT)
    public void status500InternalServerErrorTest() throws Exception {
        // Mocking
        mocking(500, ERROR_RESPONSE);

        // Expect
        expectedException.expect(ExecutionException.class);
        expectedException.expectCause(isA(LineServerException.class));
        expectedException.expectCause(errorResponseIs(ERROR_RESPONSE));

        // Do
        lineMessagingClient.getMessageContent("TOKEN").get();
    }

    @Test(timeout = ASYNC_TEST_TIMEOUT)
    public void relativeRequestTest() throws Exception {
        final String apiEndPoint =
                "http://" + mockWebServer.getHostName() + ':' + mockWebServer.getPort()
                + "/CanContainsRelative/";

        lineMessagingClient = LineMessagingClient
                .builder("SECRET")
                .apiEndPoint(apiEndPoint)
                .build();

        final UserProfileResponse profileResponseMock =
                new UserProfileResponse("name", "userId",
                                        "https://line.me/picture_url",
                                        "Status message");

        mockWebServer.enqueue(new MockResponse()
                                      .setResponseCode(200)
                                      .setBody(new ObjectMapper()
                                                       .writeValueAsString(profileResponseMock)));

        // Do
        final UserProfileResponse actualResponse =
                lineMessagingClient.getProfile("USER_TOKEN").get();

        // Verify
        final RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertThat(recordedRequest.getPath())
                .isEqualTo("/CanContainsRelative/v2/bot/profile/USER_TOKEN");
        assertThat(actualResponse).isEqualTo(profileResponseMock);
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
