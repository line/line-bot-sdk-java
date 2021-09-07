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

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.client.exception.BadRequestException;
import com.linecorp.bot.client.exception.ForbiddenException;
import com.linecorp.bot.client.exception.LineServerException;
import com.linecorp.bot.client.exception.NotFoundException;
import com.linecorp.bot.client.exception.TooManyRequestsException;
import com.linecorp.bot.client.exception.UnauthorizedException;
import com.linecorp.bot.model.error.ErrorResponse;

public class LineMessagingClientImplWiremockTest extends AbstractWiremockTest {
    static final ErrorResponse ERROR_RESPONSE =
            new ErrorResponse(null, "Error Message", null);
    private static final String ERROR_MESSAGE
            = "Error Message : ErrorResponse(requestId=null, message=Error Message, details=[])";

    @ParameterizedTest
    @Timeout(ASYNC_TEST_TIMEOUT)
    @MethodSource("statusCodeHandlerTestSource")
    public void statusCodeHandlerTest(int statusCode, Class<? extends Throwable> expectedClass)
            throws Exception {
        // Mocking
        stubFor(get(urlEqualTo("/v2/bot/message/TOKEN/content"))
                        .willReturn(aResponse()
                                            .withStatus(statusCode)
                                            .withBody(new ObjectMapper().writeValueAsString(
                                                    ERROR_RESPONSE))));

        // Do
        assertThatThrownBy(() -> lineBlobClient.getMessageContent("TOKEN").get())
                .isInstanceOf(ExecutionException.class)
                .hasRootCauseInstanceOf(expectedClass)
                .hasRootCauseMessage(ERROR_MESSAGE);
    }

    public static Stream<Arguments> statusCodeHandlerTestSource() {
        return Stream.of(
                arguments(400, BadRequestException.class),
                arguments(401, UnauthorizedException.class),
                arguments(403, ForbiddenException.class),
                arguments(404, NotFoundException.class),
                arguments(429, TooManyRequestsException.class),
                arguments(500, LineServerException.class)
        );
    }
}
