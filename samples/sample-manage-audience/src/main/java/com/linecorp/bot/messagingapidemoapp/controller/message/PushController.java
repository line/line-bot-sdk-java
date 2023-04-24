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

package com.linecorp.bot.messagingapidemoapp.controller.message;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.linecorp.bot.messaging.client.MessagingApiClient;
import com.linecorp.bot.messaging.model.Message;
import com.linecorp.bot.messaging.model.PushMessageRequest;

@Controller
public class PushController {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(PushController.class);

    private final MessagingApiClient client;
    private final MessageHelper messageHelper;

    public PushController(MessagingApiClient client, MessageHelper messageHelper) {
        this.client = client;
        this.messageHelper = messageHelper;
    }

    @GetMapping("/message/push")
    public String push() {
        return "message/push/form";
    }

    @PostMapping("/message/push")
    public CompletableFuture<RedirectView> postPush(@RequestParam String to,
                                                    @RequestParam String[] messages,
                                                    @RequestParam Boolean notificationDisabled) {
        List<Message> messageList = messageHelper.buildMessages(messages);
        return client.pushMessage(
                        null,
                        new PushMessageRequest(to, messageList, notificationDisabled, null))
                .thenApply(response -> new RedirectView("/message/push/" + response.requestId()));
    }

    @GetMapping("/message/push/{requestId}")
    public String result(Model model, @PathVariable String requestId) {
        model.addAttribute("requestId", requestId);
        return "message/push/result";
    }
}
