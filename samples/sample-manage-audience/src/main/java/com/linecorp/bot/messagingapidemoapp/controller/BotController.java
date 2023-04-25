/*
 * Copyright 2023 LINE Corporation
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

package com.linecorp.bot.messagingapidemoapp.controller;

import java.net.URI;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.linecorp.bot.messaging.client.MessagingApiClient;
import com.linecorp.bot.messaging.client.MessagingApiClientException;
import com.linecorp.bot.messaging.model.BotInfoResponse;
import com.linecorp.bot.messaging.model.GetWebhookEndpointResponse;
import com.linecorp.bot.messaging.model.SetWebhookEndpointRequest;
import com.linecorp.bot.messaging.model.TestWebhookEndpointRequest;
import com.linecorp.bot.messaging.model.TestWebhookEndpointResponse;

@Controller
public class BotController {
    MessagingApiClient client;

    public BotController(MessagingApiClient client) {
        this.client = client;
    }

    @GetMapping("/bot/info")
    public String info(Model model) throws ExecutionException, InterruptedException {
        BotInfoResponse botInfo = client.getBotInfo().get().body();
        model.addAttribute("botInfo", botInfo);
        return "bot/info";
    }

    @GetMapping("/bot/webhook")
    public String webhook(Model model) throws InterruptedException, ExecutionException {
        try {
            GetWebhookEndpointResponse webhook = client.getWebhookEndpoint().get().body();
            model.addAttribute("webhook", webhook);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof MessagingApiClientException) {
                model.addAttribute("exception", e.getCause());
            } else {
                throw e;
            }
        }
        return "bot/get_webhook";
    }

    @PostMapping("/bot/set_webhook")
    public String setWebhook(@RequestParam("url") URI uri) {
        SetWebhookEndpointRequest request = new SetWebhookEndpointRequest(uri);
        client.setWebhookEndpoint(
                request
        ).join();
        return "redirect:/bot/webhook";
    }

    @PostMapping("/bot/test_webhook")
    public String testWebhook(Model model,
                              @RequestParam(value = "url", required = false) URI uri) {
        TestWebhookEndpointResponse response = client.testWebhookEndpoint(
                new TestWebhookEndpointRequest(uri)
        ).join().body();
        model.addAttribute("response", response);
        return "bot/test_webhook";
    }

}
