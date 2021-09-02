/*
 * Copyright 2021 LINE Corporation
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

package com.linecorp.bot.spring.boot.integration.destination_handler;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.getAllServeEvents;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.google.common.io.ByteStreams;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineBotDestination;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.linecorp.bot.spring.boot.integration.destination_handler.IntegrationTestWithDestinationHandler.MyController;

import lombok.extern.slf4j.Slf4j;

// integration test
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { IntegrationTestWithDestinationHandler.class, MyController.class })
@WebAppConfiguration
@SpringBootApplication
public class IntegrationTestWithDestinationHandler {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    private static WireMockServer server;

    @LineMessageHandler
    @Slf4j
    public static class MyController {
        @Autowired
        private LineMessagingClient lineMessagingClient;

        @EventMapping
        public TextMessage handleTextMessageEvent(@LineBotDestination String destination,
                                                  MessageEvent<TextMessageContent> event) throws Exception {
            log.info("Got request: destination={} {}", destination, event);
            TextMessageContent content = event.getMessage();
            String text = content.getText();
            return new TextMessage(text + " " + destination);
        }
    }

    @BeforeAll
    public static void beforeClass() {
        server = new WireMockServer(wireMockConfig().dynamicPort());
        server.start();
        WireMock.configureFor("localhost", server.port());

        System.setProperty("line.bot.apiEndPoint", server.baseUrl());
    }

    @AfterAll
    public static void afterClass() {
        server.stop();
    }

    @BeforeEach
    public void before() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                                      .build();
    }

    @Test
    public void validCallbackTest() throws Exception {
        stubFor(post(urlEqualTo("/v2/bot/message/reply"))
                        .willReturn(aResponse().withBody("{}")));

        String signature = "JrTTkLoW+Qj8pWajBMzZJ70O3katMjJKUlXaMFiIdkI=";

        InputStream resource = getClass().getClassLoader()
                                         .getResourceAsStream("callback-request-with-destination.json");
        assert resource != null;
        byte[] json = ByteStreams.toByteArray(resource);

        mockMvc.perform(MockMvcRequestBuilders.post("/callback")
                                              .header("X-Line-Signature", signature)
                                              .content(json))
               .andDo(print())
               .andExpect(status().isOk());

        // ReplyByReturnValueConsumer.reply sends request to messaging api, and doesn't wait the response.
        // In this test case, we should wait until wiremock receive the request.
        int maxRetries = 100;
        while (getAllServeEvents().size() == 0 && maxRetries > 0) {
            Thread.sleep(100);
            maxRetries--;
        }

        // Test request 1
        verify(postRequestedFor(urlEqualTo("/v2/bot/message/reply"))
                       .withHeader("Authorization", equalTo("Bearer TOKEN"))
                       .withRequestBody(equalTo(
                               "{\"replyToken\":\"nHuyWiB7yP5Zw52FIkcQobQuGDXCTA\","
                               + "\"messages\":["
                               + "{\"type\":\"text\","
                               + "\"text\":\"Hello, world! with destination"
                               + " U11111111111111111111111111111111\"}],"
                               + "\"notificationDisabled\":false}"
                       )));
    }
}
