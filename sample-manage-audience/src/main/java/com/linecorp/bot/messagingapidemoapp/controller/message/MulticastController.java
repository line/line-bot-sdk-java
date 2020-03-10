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

package com.linecorp.bot.messagingapidemoapp.controller.message;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.message.Message;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@AllArgsConstructor
@Slf4j
public class MulticastController {
    private final LineMessagingClient client;
    private final MessageHelper messageHelper;

    @GetMapping("/message/multicast")
    public String multicast() {
        return "message/multicast/form";
    }

    @PostMapping("/message/multicast")
    public CompletableFuture<RedirectView> postMulticast(@RequestParam String to,
                                                         @RequestParam String[] messages,
                                                         @RequestParam Boolean notificationDisabled)
            throws ExecutionException, InterruptedException {
        Set<String> toList = Arrays.stream(to.split("\r?\n"))
                                   .filter(StringUtils::isNotBlank)
                                   .collect(Collectors.toSet());

        List<Message> messageList = messageHelper.buildMessages(messages);
        return client.multicast(
                new Multicast(toList, messageList, notificationDisabled))
                     .thenApply(response -> new RedirectView("/message/multicast/" + response.getRequestId()));
    }

    @GetMapping("/message/multicast/{requestId}")
    public String result(Model model, @PathVariable String requestId) {
        model.addAttribute("requestId", requestId);
        return "message/multicast/result";
    }
}
