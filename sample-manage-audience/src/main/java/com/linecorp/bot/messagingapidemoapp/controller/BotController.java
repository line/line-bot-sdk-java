/*
 * Copyright 2020 LINE Corporation
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
 *
 */

package com.linecorp.bot.messagingapidemoapp.controller;

import java.net.URI;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.exception.NotFoundException;
import com.linecorp.bot.model.request.SetWebhookEndpointRequest;
import com.linecorp.bot.model.request.TestWebhookEndpointRequest;
import com.linecorp.bot.model.request.TestWebhookEndpointRequest.TestWebhookEndpointRequestBuilder;
import com.linecorp.bot.model.response.BotInfoResponse;
import com.linecorp.bot.model.response.GetWebhookEndpointResponse;
import com.linecorp.bot.model.response.SetWebhookEndpointResponse;
import com.linecorp.bot.model.response.TestWebhookEndpointResponse;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class BotController {
    LineMessagingClient client;

    @GetMapping("/bot/info")
    public String info(Model model) throws ExecutionException, InterruptedException {
        BotInfoResponse botInfo = client.getBotInfo().get();
        model.addAttribute("botInfo", botInfo);
        return "bot/info";
    }

    @GetMapping("/bot/webhook")
    public String webhook(Model model) throws InterruptedException, ExecutionException {
        try {
            GetWebhookEndpointResponse webhook = client.getWebhookEndpoint().get();
            model.addAttribute("webhook", webhook);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof NotFoundException) {
                model.addAttribute("notFoundException", e.getCause());
            } else {
                throw e;
            }
        }
        return "bot/get_webhook";
    }

    @PostMapping("/bot/set_webhook")
    public String setWebhook(Model model,
                             @RequestParam("url") URI uri) {
        SetWebhookEndpointRequest request = SetWebhookEndpointRequest.builder()
                                                                     .endpoint(uri)
                                                                     .build();
        SetWebhookEndpointResponse response = client.setWebhookEndpoint(
                request
        ).join();
        model.addAttribute("response", response);
        return "redirect:/bot/webhook";
    }

    @PostMapping("/bot/test_webhook")
    public String testWebhook(Model model,
                              @RequestParam(value = "url", required = false) URI uri) {
        TestWebhookEndpointRequestBuilder builder = TestWebhookEndpointRequest.builder();
        if (uri != null) {
            builder.endpoint(uri);
        }
        TestWebhookEndpointRequest testWebhookEndpointRequest = builder.build();
        TestWebhookEndpointResponse response = client.testWebhookEndpoint(
                testWebhookEndpointRequest
        ).join();
        model.addAttribute("response", response);
        return "bot/test_webhook";
    }

}
