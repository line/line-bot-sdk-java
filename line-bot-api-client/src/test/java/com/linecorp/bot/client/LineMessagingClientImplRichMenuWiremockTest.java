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

package com.linecorp.bot.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import com.linecorp.bot.model.response.BotApiResponse;

public class LineMessagingClientImplRichMenuWiremockTest extends AbstractWiremockTest {
    public static final BotApiResponseBody SUCCESS_BODY = new BotApiResponseBody("", emptyList());
    public static final BotApiResponse SUCCESS = SUCCESS_BODY.withRequestId("REQUEST_ID");

    @Test
    @Timeout(ASYNC_TEST_TIMEOUT)
    public void status200WithoutBodyTest() throws Exception {
        // Mocking
        stubFor(delete(urlEqualTo("/v2/bot/richmenu/RICH_MENU_ID")).willReturn(
                aResponse()
                        .withStatus(200)
                        .withHeader("x-line-request-id", "REQUEST_ID")
        ));

        // Do
        final BotApiResponse botApiResponse = lineMessagingClient.deleteRichMenu("RICH_MENU_ID").get();
        assertThat(botApiResponse).isEqualTo(SUCCESS);
    }

    @Test
    @Timeout(ASYNC_TEST_TIMEOUT)
    public void status200WithBodyTest() throws Exception {
        // Mocking
        stubFor(delete(urlEqualTo("/v2/bot/richmenu/RICH_MENU_ID")).willReturn(
                aResponse()
                        .withStatus(200)
                        .withHeader("x-line-request-id", "REQUEST_ID")
                        .withBody("{}")
        ));

        // Do
        final BotApiResponse botApiResponse = lineMessagingClient.deleteRichMenu("RICH_MENU_ID")
                                                                 .get();
        assertThat(botApiResponse).isEqualTo(SUCCESS);
    }
}
