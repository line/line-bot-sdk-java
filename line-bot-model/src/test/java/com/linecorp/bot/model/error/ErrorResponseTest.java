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

package com.linecorp.bot.model.error;

import static java.lang.ClassLoader.getSystemResourceAsStream;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ErrorResponseTest {
    @Test
    public void simpleErrorResponseTest() throws IOException {
        // Do
        final ErrorResponse result = new ObjectMapper()
                .readValue(getSystemResourceAsStream("error/error401.json"),
                           ErrorResponse.class);

        // Verify
        assertThat(result.getMessage()).contains("Authentication failed");
        assertThat(result.getDetails()).isNotNull().isEmpty();
    }

    @Test
    public void complexErrorResponseTest() throws IOException {
        // Do
        final ErrorResponse result = new ObjectMapper()
                .readValue(getSystemResourceAsStream("error/error_with_detail.json"),
                           ErrorResponse.class);

        // Verify
        assertThat(result.getMessage()).isEqualTo("The request body has 2 error(s)");
        assertThat(result.getDetails()).containsExactly(
                new ErrorDetail("May not be empty",
                                "messages[0].text"),
                new ErrorDetail("Must be one of the following values: " +
                                "[text, image, video, audio, location, sticker, richmessage, template, imagemap]",
                                "messages[1].type"));
    }
}
