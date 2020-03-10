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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.linecorp.bot.client.LineMessagingClient;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReportController {
    private final LineMessagingClient client;
    private static final DateTimeFormatter PATTERN = DateTimeFormatter.ofPattern("uuuuMMdd");

    @GetMapping("/message/report/quota")
    public CompletableFuture<String> show(Model model) {
        CompletableFuture<Void> quotaFuture = client
                .getMessageQuota()
                .thenAccept(messageQuota -> model.addAttribute("quota", messageQuota));
        CompletableFuture<Void> totalUsageFuture = client
                .getMessageQuotaConsumption()
                .thenAccept(
                        response -> model.addAttribute("totalUsage", response.getTotalUsage()));

        return CompletableFuture.allOf(quotaFuture, totalUsageFuture)
                                .thenApply(it -> "message/report/quota");
    }

    @GetMapping("/message/report/sent")
    public CompletableFuture<String> sent(Model model, @RequestParam(required = false) String date) {
        if (date == null) {
            date = LocalDate.now().format(PATTERN);
        } else {
            date = date.replaceAll("-", "");
        }

        model.addAttribute("date", LocalDate.parse(date, PATTERN)
                                            .format(DateTimeFormatter.ISO_DATE));

        return CompletableFuture.allOf(
                client.getNumberOfSentReplyMessages(date).thenApply(
                        it -> model.addAttribute("reply", it)
                ),
                client.getNumberOfSentPushMessages(date).thenApply(
                        it -> model.addAttribute("push", it)
                ),
                client.getNumberOfSentBroadcastMessages(date).thenApply(
                        it -> model.addAttribute("broadcast", it)
                ),
                client.getNumberOfSentMulticastMessages(date).thenApply(
                        it -> model.addAttribute("multicast", it)
                )
        ).thenApply(it -> "message/report/sent");
    }
}
