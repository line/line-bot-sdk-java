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

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.linecorp.bot.client.exception.BadRequestException;
import com.linecorp.bot.client.exception.ForbiddenException;
import com.linecorp.bot.client.exception.LineServerException;
import com.linecorp.bot.client.exception.NotFoundException;
import com.linecorp.bot.client.exception.TooManyRequestsException;
import com.linecorp.bot.client.exception.UnauthorizedException;
import com.linecorp.bot.model.error.ErrorResponse;

@RunWith(Parameterized.class)
public class LineMessagingClientImplWiremockTest extends AbstractWiremockTest {
    static final ErrorResponse ERROR_RESPONSE =
            new ErrorResponse(null, "Error Message", null);
    private static final String ERROR_MESSAGE
            = "Error Message : ErrorResponse(requestId=null, message=Error Message, details=[])";

    @Parameters(name = "{index}: status={0}, expected={1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { 400, BadRequestException.class },
                { 401, UnauthorizedException.class },
                { 403, ForbiddenException.class },
                { 404, NotFoundException.class },
                { 429, TooManyRequestsException.class },
                { 500, LineServerException.class }
        });
    }

    private final int statusCode;
    private final Class<? extends Throwable> expectedClass;

    public LineMessagingClientImplWiremockTest(int statusCode, Class<? extends Throwable> expectedClass) {
        this.statusCode = statusCode;
        this.expectedClass = expectedClass;
    }

    @Test(timeout = ASYNC_TEST_TIMEOUT)
    public void statusCodeHandlerTest() throws Exception {
        // Mocking
        mocking(statusCode, ERROR_RESPONSE);

        // Do
        assertThatThrownBy(() -> lineBlobClient.getMessageContent("TOKEN").get())
                .isInstanceOf(ExecutionException.class)
                .hasRootCauseInstanceOf(expectedClass)
                .hasRootCauseMessage(ERROR_MESSAGE);
    }
}
