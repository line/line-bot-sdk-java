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

package com.linecorp.bot.spring.boot.integration.destination;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;
import java.util.List;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.google.common.io.ByteStreams;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.LineBotDestination;
import com.linecorp.bot.spring.boot.annotation.LineBotMessages;
import com.linecorp.bot.spring.boot.integration.destination.IntegrationTestWithDestination.MyController;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

// integration test
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { IntegrationTestWithDestination.class, MyController.class },
        properties = "line.bot.handler.enabled=false")
@WebAppConfiguration
@SpringBootApplication
public class IntegrationTestWithDestination {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    private static WireMockServer server;

    @RestController
    @Slf4j
    public static class MyController {
        @Autowired
        private LineMessagingClient lineMessagingClient;

        @PostMapping("/callback")
        public void callback(@LineBotDestination String destination,
                             @NonNull @LineBotMessages List<Event> events) throws Exception {
            log.info("Got request: destination={} {}", destination, events);

            for (Event event : events) {
                this.handleEvent(destination, event);
            }
        }

        private void handleEvent(String destination, Event event) throws Exception {
            if (event instanceof MessageEvent) {
                MessageContent content = ((MessageEvent) event).getMessage();
                if (content instanceof TextMessageContent) {
                    String text = ((TextMessageContent) content).getText();
                    lineMessagingClient.replyMessage(
                                               new ReplyMessage(((MessageEvent) event).getReplyToken(),
                                                                new TextMessage(text + " " + destination)))
                                       .get();
                }
            }
        }
    }

    @BeforeAll
    public static void beforeClass() {
        server = new WireMockServer(wireMockConfig().dynamicPort());
        server.start();
        WireMock.configureFor("localhost", server.port());

        System.setProperty("line.bot.apiEndPoint", server.url("/"));
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
        stubFor(post(urlEqualTo("/v2/bot/message/reply")).willReturn(
                aResponse()
                        .withStatus(200)
                        .withBody("{}}")
        ));

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
