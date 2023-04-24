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

import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.audience.client.ManageAudienceClient;
import com.linecorp.bot.audience.model.AudienceGroupStatus;
import com.linecorp.bot.insight.model.AppTypeTile;
import com.linecorp.bot.insight.model.SubscriptionPeriodTile;
import com.linecorp.bot.messaging.client.MessagingApiClient;
import com.linecorp.bot.messaging.model.AgeDemographic;
import com.linecorp.bot.messaging.model.AgeDemographicFilter;
import com.linecorp.bot.messaging.model.AppTypeDemographic;
import com.linecorp.bot.messaging.model.AppTypeDemographicFilter;
import com.linecorp.bot.messaging.model.AreaDemographic;
import com.linecorp.bot.messaging.model.AreaDemographicFilter;
import com.linecorp.bot.messaging.model.DemographicFilter;
import com.linecorp.bot.messaging.model.Filter;
import com.linecorp.bot.messaging.model.GenderDemographic;
import com.linecorp.bot.messaging.model.GenderDemographicFilter;
import com.linecorp.bot.messaging.model.Limit;
import com.linecorp.bot.messaging.model.Message;
import com.linecorp.bot.messaging.model.NarrowcastRequest;
import com.linecorp.bot.messaging.model.OperatorDemographicFilter;
import com.linecorp.bot.messaging.model.Recipient;

import io.micrometer.common.util.StringUtils;

@Controller
public class NarrowcastController {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(NarrowcastController.class);
    private final ManageAudienceClient client;
    private final MessagingApiClient messagingClient;
    private final MessageHelper messageHelper;
    private final ObjectMapper objectMapper;

    public NarrowcastController(
            ManageAudienceClient client,
            MessagingApiClient messagingClient,
            MessageHelper messageHelper,
            ObjectMapper objectMapper) {
        this.client = client;
        this.messagingClient = messagingClient;
        this.messageHelper = messageHelper;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/message/narrowcast")
    public CompletableFuture<String> narrowcast(Model model) {
        model.addAttribute("ages", AgeDemographic.values());
        model.addAttribute("appTypes", AppTypeTile.AppType.values());
        model.addAttribute("areaCodes", AreaDemographic.values());
        model.addAttribute("subscriptionPeriods", SubscriptionPeriodTile.SubscriptionPeriod.values());
        return client.getAudienceGroups(
                        1L, null, AudienceGroupStatus.READY, 40L, true,
                        null)
                .thenApply(result -> {
                    model.addAttribute("audienceGroups", result.body().audienceGroups());
                    return "message/narrowcast/form";
                });
    }

    @PostMapping("/message/narrowcast")
    public CompletableFuture<RedirectView> pushNarrowcast(
            @RequestParam String[] messages,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String ageGte,
            @RequestParam(required = false) String ageLt,
            @RequestParam(required = false) String appType,
            @RequestParam(required = false) String[] areaCode,
            @RequestParam(required = false) Integer maxLimit,
            @RequestParam(required = false) String recipient,
            @RequestParam Boolean notificationDisabled
    ) throws JsonProcessingException {
        List<Message> messageList = messageHelper.buildMessages(messages);

        List<DemographicFilter> condition = new ArrayList<>();

        if (gender != null) {
            condition.add(new GenderDemographicFilter(
                    singletonList(GenderDemographic.valueOf(gender))
            ));
        }
        if (StringUtils.isNotBlank(ageGte) || StringUtils.isNotBlank(ageLt)) {
            log.info("ageGte={} ageLt={}", ageGte, ageLt);
            AgeDemographicFilter filter = new AgeDemographicFilter(
                    StringUtils.isBlank(ageGte) ? null : AgeDemographic.valueOf(ageGte),
                    StringUtils.isBlank(ageLt) ? null : AgeDemographic.valueOf(ageLt)
            );
            condition.add(filter);
        }
        if (StringUtils.isNotBlank(appType)) {
            condition.add(new AppTypeDemographicFilter(
                    singletonList(AppTypeDemographic.valueOf(appType))
            ));
        }
        if (areaCode != null && areaCode.length > 0) {
            condition.add(
                    new AreaDemographicFilter(
                            Arrays.stream(areaCode)
                                    .map(AreaDemographic::valueOf)
                                    .collect(Collectors.toList())
                    )
            );
        }
        Recipient recipientObject = StringUtils.isNotBlank(recipient)
                ? objectMapper.readValue(recipient, Recipient.class)
                : null;

        NarrowcastRequest narrowcast = new NarrowcastRequest(
                messageList,
                recipientObject,
                new Filter(new OperatorDemographicFilter(condition, null, null)),
                maxLimit == null ? null : new Limit(maxLimit, false),
                notificationDisabled
        );
        return messagingClient.narrowcast(null, narrowcast).thenApply(
                response -> new RedirectView("/message/narrowcast/" + response.requestId())
        );
    }

    @GetMapping("/message/narrowcast/{requestId}")
    public CompletableFuture<String> narrowcastProgress(@PathVariable String requestId, Model model) {
        model.addAttribute("requestId", requestId);

        return messagingClient.getNarrowcastProgress(requestId)
                .thenApply(response -> {
                    model.addAttribute("progress", response.body());
                    return "message/narrowcast/progress";

                });
    }

}
