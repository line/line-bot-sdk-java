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
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.response.MessageQuotaResponse;
import com.linecorp.bot.model.response.NumberOfMessagesResponse;
import com.linecorp.bot.model.response.QuotaConsumptionResponse;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReportController {
    private final LineMessagingClient client;
    private static final DateTimeFormatter PATTERN = DateTimeFormatter.ofPattern("uuuuMMdd");

    @GetMapping("/message/report/quota")
    public String show(Model model) throws ExecutionException, InterruptedException {
        MessageQuotaResponse messageQuota = client.getMessageQuota().get();
        model.addAttribute("quota", messageQuota);

        QuotaConsumptionResponse quotaConsumptionResponse =
                client.getMessageQuotaConsumption().get();
        model.addAttribute("totalUsage", quotaConsumptionResponse.getTotalUsage());

        return "message/report/quota";
    }

    @GetMapping("/message/report/sent")
    public String sent(Model model, @RequestParam(required = false) String date)
            throws ExecutionException, InterruptedException {
        if (date == null) {
            date = LocalDate.now().format(PATTERN);
        } else {
            date = date.replaceAll("-", "");
        }

        model.addAttribute("date", LocalDate.parse(date, PATTERN)
                                            .format(DateTimeFormatter.ISO_DATE));

        NumberOfMessagesResponse reply =
                client.getNumberOfSentReplyMessages(date).get();
        NumberOfMessagesResponse push =
                client.getNumberOfSentPushMessages(date).get();
        NumberOfMessagesResponse broadcast =
                client.getNumberOfSentBroadcastMessages(date).get();
        NumberOfMessagesResponse multicast =
                client.getNumberOfSentMulticastMessages(date).get();

        model.addAttribute("reply", reply);
        model.addAttribute("push", push);
        model.addAttribute("broadcast", broadcast);
        model.addAttribute("multicast", multicast);

        return "message/report/sent";
    }
}
