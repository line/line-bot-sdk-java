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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.concurrent.ExecutionException;

import org.hamcrest.CustomTypeSafeMatcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import com.linecorp.bot.client.exception.BadRequestException;
import com.linecorp.bot.client.exception.ForbiddenException;
import com.linecorp.bot.client.exception.LineMessagingException;
import com.linecorp.bot.client.exception.LineServerException;
import com.linecorp.bot.client.exception.NotFoundException;
import com.linecorp.bot.client.exception.TooManyRequestsException;
import com.linecorp.bot.client.exception.UnauthorizedException;
import com.linecorp.bot.model.error.ErrorResponse;

public class LineMessagingClientImplWiremockTest extends AbstractWiremockTest {
    static final ErrorResponse ERROR_RESPONSE =
            new ErrorResponse(null, "Error Message", null);

    @Test
    @Timeout(ASYNC_TEST_TIMEOUT)
    public void status400BadRequestTest() throws Exception {
        // Mocking
        mocking(400, ERROR_RESPONSE);

        // Expect
        // TTTTTTTTTTT
//        expectedException.expect();
//        expectedException.expectCause(isA());
//        expectedException.expectCause(errorResponseIs(ERROR_RESPONSE));

        assertThatThrownBy(
                () -> lineBlobClient.getMessageContent("TOKEN").get()
        ).isInstanceOf(ExecutionException.class)
         .hasCauseInstanceOf(BadRequestException.class)
         .hasRootCauseMessage("TODO");
    }

    @Test
    @Timeout(ASYNC_TEST_TIMEOUT)
    public void status401UnauthorizedTest() throws Exception {
        // Mocking
        mocking(401, ERROR_RESPONSE);

        // Expect
//        expectedException.expect(ExecutionException.class);
//        expectedException.expectCause(isA(UnauthorizedException.class));
//        expectedException.expectCause(errorResponseIs(ERROR_RESPONSE));

        // Do
        assertThatThrownBy(
                () -> lineBlobClient.getMessageContent("TOKEN").get()
        )
                .isInstanceOf(ExecutionException.class)
                .hasCauseInstanceOf(UnauthorizedException.class)
                .hasRootCauseMessage("TOOD");
    }

    @Test
    @Timeout(ASYNC_TEST_TIMEOUT)
    public void status403ForbiddenTest() throws Exception {
        // Mocking
        mocking(403, ERROR_RESPONSE);

        // Expect
//        expectedException.expect();
//        expectedException.expectCause(isA());
//        expectedException.expectCause(errorResponseIs(ERROR_RESPONSE));

        // Do
        assertThatThrownBy(
                () -> lineBlobClient.getMessageContent("TOKEN").get()
        )
                .isInstanceOf(ExecutionException.class)
                .hasCauseInstanceOf(ForbiddenException.class)
                .hasRootCauseMessage("TODO");
    }

    @Test
    @Timeout(ASYNC_TEST_TIMEOUT)
    public void status404NotFoundTest() throws Exception {
        // Mocking
        mocking(404, ERROR_RESPONSE);

        // Expect
//        expectedException.expect();
//        expectedException.expectCause(isA(NotFoundException.class));
//        expectedException.expectCause(errorResponseIs(ERROR_RESPONSE));

        // Do
        assertThatThrownBy(() ->
                                   lineBlobClient.getMessageContent("TOKEN").get()
        ).isInstanceOf(ExecutionException.class)
         .hasRootCauseInstanceOf(NotFoundException.class)
         .hasRootCauseMessage("HAAHAHA");
    }

    @Test
    @Timeout(ASYNC_TEST_TIMEOUT)
    public void status429TooManyRequestsTest() throws Exception {
        // Mocking
        mocking(429, ERROR_RESPONSE);

        // Expect
//        expectedException.expect(ExecutionException.class);
//        expectedException.expectCause(isA());
//        expectedException.expectCause(errorResponseIs(ERROR_RESPONSE));

        // Do
        assertThatThrownBy(
                () -> lineBlobClient.getMessageContent("TOKEN").get()
        ).isInstanceOf(TooManyRequestsException.class)
         .hasRootCauseMessage("AAAA");
    }

    @Test
    @Timeout(ASYNC_TEST_TIMEOUT)
    public void status500InternalServerErrorTest() throws Exception {
        // Mocking
        mocking(500, ERROR_RESPONSE);

        // Expect
//        expectedException.expect(ExecutionException.class);
//        expectedException.expectCause(isA(LineServerException.class));
//        expectedException.expectCause(errorResponseIs(ERROR_RESPONSE));

        // Do
        assertThatThrownBy(
                () -> lineBlobClient.getMessageContent("TOKEN").get()
        ).isInstanceOf(ExecutionException.class)
         .hasRootCauseInstanceOf(LineServerException.class)
         .hasRootCauseMessage("TODO");
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
