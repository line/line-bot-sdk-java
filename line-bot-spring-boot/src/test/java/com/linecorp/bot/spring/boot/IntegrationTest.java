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

package com.linecorp.bot.spring.boot;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.linecorp.bot.client.LineMessagingService;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.IntegrationTest.Configuration;
import com.linecorp.bot.spring.boot.IntegrationTest.MyController;
import com.linecorp.bot.spring.boot.annotation.LineBotMessages;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// integration test
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { IntegrationTest.class, Configuration.class, MyController.class })
@WebAppConfiguration
@SpringBootApplication
public class IntegrationTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private LineMessagingService lineMessagingService;

    private MockMvc mockMvc;

    @RestController
    @Slf4j
    public static class MyController {
        @Autowired
        private LineMessagingService lineMessagingService;

        @PostMapping("/callback")
        public void callback(@NonNull @LineBotMessages List<Event> events) throws IOException {
            log.info("Got request: {}", events);

            for (Event event : events) {
                this.handleEvent(event);
            }
        }

        private void handleEvent(Event event) {
            if (event instanceof MessageEvent) {
                MessageContent content = ((MessageEvent) event).getMessage();
                if (content instanceof TextMessageContent) {
                    String text = ((TextMessageContent) content).getText();
                    lineMessagingService.reply(
                            new ReplyMessage(((MessageEvent) event).getReplyToken(),
                                             new TextMessage(text)));
                }
            } else if (event instanceof FollowEvent) {
                lineMessagingService.reply(
                        new ReplyMessage(((FollowEvent) event).getReplyToken(),
                                         new TextMessage("follow")));
            }
        }
    }

    @Before
    public void before() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                                      .build();
    }

    @org.springframework.context.annotation.Configuration
    public static class Configuration {
        @Mock
        private LineMessagingService lineMessagingService;

        public Configuration() {
            MockitoAnnotations.initMocks(this);
        }

        @Bean
        public LineMessagingService lineMessagingService() {
            return lineMessagingService;
        }
    }

    @Test
    public void missingSignatureTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/callback")
                                              .content("{}"))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(content().string(containsString("Missing 'X-Line-Signature' header")));
    }

    @Test
    public void validCallbackTest() throws Exception {
        String signature = "ECezgIpQNUEp4OSHYd7xGSuFG7e66MLPkCkK1Y28XTU=";

        InputStream resource = getClass().getClassLoader().getResourceAsStream("callback-request.json");
        byte[] json = IOUtils.toByteArray(resource);

        Call<BotApiResponse> call = new Call<BotApiResponse>() {
            @Override
            public Response<BotApiResponse> execute() {
                return null;
            }

            @Override
            public void enqueue(Callback<BotApiResponse> callback) {

            }

            @Override
            public boolean isExecuted() {
                return false;
            }

            @Override
            public void cancel() {

            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public Call<BotApiResponse> clone() {
                return this;
            }

            @Override
            public Request request() {
                return null;
            }
        };
        when(lineMessagingService.reply(any(ReplyMessage.class)))
                .thenReturn(call);

        mockMvc.perform(MockMvcRequestBuilders.post("/callback")
                                              .header("X-Line-Signature", signature)
                                              .content(json))
               .andDo(print())
               .andExpect(status().isOk());

        verify(lineMessagingService).reply(
                new ReplyMessage("nHuyWiB7yP5Zw52FIkcQobQuGDXCTA",
                                 new TextMessage("Hello, world")));
        verify(lineMessagingService).reply(
                new ReplyMessage("nHuyWiB7yP5Zw52FIkcQobQuGDXCTA",
                                 new TextMessage("follow")));
    }
}
