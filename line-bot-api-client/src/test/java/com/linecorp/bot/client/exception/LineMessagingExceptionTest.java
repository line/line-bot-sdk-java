/*
 * Copyright 2018 LINE Corporation
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

package com.linecorp.bot.client.exception;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.linecorp.bot.model.error.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LineMessagingExceptionTest {
    @Test
    public void errorResponseIncludedInMessageTest() throws Exception {
        final ErrorResponse errorResponse = new ErrorResponse("requestId_in_response", null, emptyList());
        final BadRequestException exception = new BadRequestException("Message", errorResponse);

        String systemOut = tapSystemOut(() -> log.error("", exception));

        assertThat(systemOut)
                .contains("requestId_in_response")
                .contains(errorResponse.toString());
    }
}
