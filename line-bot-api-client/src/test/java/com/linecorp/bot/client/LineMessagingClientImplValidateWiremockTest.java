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
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import com.linecorp.bot.model.Broadcast;
import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.Narrowcast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.narrowcast.Filter;

public class LineMessagingClientImplValidateWiremockTest extends AbstractWiremockTest {
    @Override
    @BeforeEach
    public void setUpWireMock() {
        // https://stackoverflow.com/questions/48864716/before-beforeeach-inheritance-behaviour-change-junit4-junit5
        super.setUpWireMock();
    }

    @Timeout(ASYNC_TEST_TIMEOUT)
    @Test
    public void validateReplySuccessTest() throws ExecutionException, InterruptedException {
        // Mocking
        stubFor(post(urlEqualTo("/v2/bot/message/validate/reply"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("{}")));

        // Do
        lineMessagingClient.validateReply(
                new ReplyMessage("AAAAA", TextMessage.builder()
                        .text("Hello").build())
        ).get();
    }

    @Timeout(ASYNC_TEST_TIMEOUT)
    @Test
    public void validatePushSuccessTest() throws ExecutionException, InterruptedException {
        // Mocking
        stubFor(post(urlEqualTo("/v2/bot/message/validate/push"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("{}")));

        // Do
        lineMessagingClient.validatePush(
                new PushMessage("AAAAA", TextMessage.builder()
                        .text("Hello").build())
        ).get();
    }

    @Timeout(ASYNC_TEST_TIMEOUT)
    @Test
    public void validateMulticastSuccessTest() throws ExecutionException, InterruptedException {
        // Mocking
        stubFor(post(urlEqualTo("/v2/bot/message/validate/multicast"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("{}")));

        // Do
        lineMessagingClient.validateMulticast(
                new Multicast(Collections.singleton("AAAAA"), TextMessage.builder()
                        .text("Hello").build())
        ).get();
    }

    @Timeout(ASYNC_TEST_TIMEOUT)
    @Test
    public void validateNarrowcastSuccessTest() throws ExecutionException, InterruptedException {
        // Mocking
        stubFor(post(urlEqualTo("/v2/bot/message/validate/narrowcast"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("{}")));

        // Do
        lineMessagingClient.validateNarrowcast(
                new Narrowcast(TextMessage.builder()
                        .text("Hello").build(), Filter.builder().build())
        ).get();
    }

    @Timeout(ASYNC_TEST_TIMEOUT)
    @Test
    public void validateBroadcastSuccessTest() throws ExecutionException, InterruptedException {
        // Mocking
        stubFor(post(urlEqualTo("/v2/bot/message/validate/broadcast"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("{}")));

        // Do
        lineMessagingClient.validateBroadcast(
                new Broadcast(TextMessage.builder()
                        .text("Hello").build())
        ).get();
    }

    @Timeout(ASYNC_TEST_TIMEOUT)
    @Test
    public void validateReplyFailTest() {
        // Mocking
        stubFor(post(urlEqualTo("/v2/bot/message/validate/reply"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("content-type", "application/json")
                        .withBody(
                                """
                                        {
                                          "message": "The request body has 1 error(s)",
                                          "details": [
                                            {
                                              "message": "Length must be between 0 and 5000",
                                              "property": "messages[0].text"
                                            }
                                          ]
                                        }
                                        """)));

        // Do
        assertThatThrownBy(() -> lineMessagingClient.validateReply(
                new ReplyMessage("AAAAA", TextMessage.builder()
                .text("Hello").build())).get())
                .isInstanceOf(ExecutionException.class)
                .hasRootCauseInstanceOf(Exception.class)
                .hasRootCauseMessage("The request body has 1 error(s) : "
                        + "ErrorResponse(requestId=null, message=The request body has 1 error(s),"
                        + " details=[ErrorDetail(message=Length must be between 0 and 5000,"
                        + " property=messages[0].text)])");
    }
}
