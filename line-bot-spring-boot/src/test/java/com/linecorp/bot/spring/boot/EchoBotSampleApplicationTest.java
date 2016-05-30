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
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.linecorp.bot.client.LineBotClient;
import com.linecorp.bot.client.exception.LineBotAPIException;
import com.linecorp.bot.model.callback.Event;
import com.linecorp.bot.model.content.AddedAsFriendOperation;
import com.linecorp.bot.model.content.Content;
import com.linecorp.bot.model.content.TextContent;
import com.linecorp.bot.spring.boot.annotation.LineBotMessages;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(EchoBotSampleApplicationTest.class)
@WebAppConfiguration
public class EchoBotSampleApplicationTest {

    static {
        System.setProperty("line.bot.channelId", "4649");
        System.setProperty("line.bot.channelMid", "uXXXXX");
        System.setProperty("line.bot.channelSecret", "SEEECRET");
    }

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @RestController
    @Slf4j
    public static class MyController {
        @Autowired
        private LineBotClient lineBotClient;

        @RequestMapping("/callback")
        public void callback(@NonNull @LineBotMessages List<Event> events) throws IOException, LineBotAPIException {
            log.info("Got request: {}", events);

            for (Event event : events) {
                this.handleEvent(event);
            }
        }

        private void handleEvent(Event event) throws IOException, LineBotAPIException {
            Content content = event.getContent();
            if (content instanceof TextContent) {
                String mid = ((TextContent) content).getFrom();
                String text = ((TextContent) content).getText();
                lineBotClient.sendText(mid, text);
            } else if (content instanceof AddedAsFriendOperation){
                String mid = ((AddedAsFriendOperation) content).getMid();
                String opType = ((AddedAsFriendOperation) content).getOpType().name();
                lineBotClient.sendText(mid, opType);
            }
        }
    }

    @Autowired
    private LineBotClient lineBotClient;

    public static class Configuration {
        @Mock
        private LineBotClient lineBotClient;

        public Configuration() {
            MockitoAnnotations.initMocks(this);
        }

        @Bean
        public LineBotClient lineBotClient() {
            return lineBotClient;
        }
    }

    @Before
    public void before() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                                      .build();
    }

    @Test
    public void missingSignatureTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/callback")
                                              .content("{}"))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(content().string(containsString("Missing 'X-Line-ChannelSignature' header")));
    }

    @Test
    public void validCallbackTest() throws Exception {
        InputStream resource = getClass().getClassLoader().getResourceAsStream("callback-request.json");
        byte[] json = IOUtils.toByteArray(resource);

        when(lineBotClient.validateSignature(json, "SIGN")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/callback")
                                              .header("X-Line-ChannelSignature", "SIGN")
                                              .content(json))
               .andDo(print())
               .andExpect(status().isOk());

        verify(lineBotClient).sendText("uff2aec188e58752ee1fb0f9507c6529a", "Hello, BOT API Server!");
        verify(lineBotClient).sendText("u464471c59f5eefe815a19be11f210147", "ADDED_AS_FRIEND");
    }
}
