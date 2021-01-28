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

import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.ManageAudienceClient;
import com.linecorp.bot.model.Narrowcast;
import com.linecorp.bot.model.manageaudience.AudienceGroupStatus;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.narrowcast.Filter;
import com.linecorp.bot.model.narrowcast.Limit;
import com.linecorp.bot.model.narrowcast.filter.AgeDemographicFilter;
import com.linecorp.bot.model.narrowcast.filter.AgeDemographicFilter.Age;
import com.linecorp.bot.model.narrowcast.filter.AppTypeDemographicFilter;
import com.linecorp.bot.model.narrowcast.filter.AppTypeDemographicFilter.AppType;
import com.linecorp.bot.model.narrowcast.filter.AreaDemographicFilter;
import com.linecorp.bot.model.narrowcast.filter.AreaDemographicFilter.AreaCode;
import com.linecorp.bot.model.narrowcast.filter.DemographicFilter;
import com.linecorp.bot.model.narrowcast.filter.GenderDemographicFilter;
import com.linecorp.bot.model.narrowcast.filter.GenderDemographicFilter.Gender;
import com.linecorp.bot.model.narrowcast.filter.OperatorDemographicFilter;
import com.linecorp.bot.model.narrowcast.filter.SubscriptionPeriodDemographicFilter.SubscriptionPeriod;
import com.linecorp.bot.model.narrowcast.recipient.Recipient;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@AllArgsConstructor
public class NarrowcastController {
    private final ManageAudienceClient client;
    private final LineMessagingClient messagingClient;
    private final MessageHelper messageHelper;
    private final ObjectMapper objectMapper;

    @GetMapping("/message/narrowcast")
    public CompletableFuture<String> narrowcast(Model model) {
        model.addAttribute("ages", Age.values());
        model.addAttribute("appTypes", AppType.values());
        model.addAttribute("areaCodes", AreaCode.values());
        model.addAttribute("subscriptionPeriods", SubscriptionPeriod.values());
        return client.getAudienceGroups(
                1L, null, AudienceGroupStatus.READY, 40L, true,
                null)
                     .thenApply(response -> {
                         model.addAttribute("audienceGroups", response.getAudienceGroups());
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
            condition.add(GenderDemographicFilter.builder()
                                                 .oneOf(singletonList(Gender.valueOf(gender)))
                                                 .build());
        }
        if (StringUtils.isNotBlank(ageGte) || StringUtils.isNotBlank(ageLt)) {
            log.info("ageGte={} ageLt={}", ageGte, ageLt);
            AgeDemographicFilter filter = AgeDemographicFilter
                    .builder()
                    .gte(StringUtils.isBlank(ageGte) ? null : Age.valueOf(ageGte))
                    .lt(StringUtils.isBlank(ageLt) ? null : Age.valueOf(ageLt))
                    .build();
            condition.add(filter);
        }
        if (StringUtils.isNotBlank(appType)) {
            condition.add(AppTypeDemographicFilter.builder()
                                                  .oneOf(singletonList(AppType.valueOf(appType)))
                                                  .build());
        }
        if (areaCode != null && areaCode.length > 0) {
            condition.add(
                    AreaDemographicFilter
                            .builder()
                            .oneOf(
                                    Arrays.stream(areaCode)
                                          .map(AreaCode::valueOf)
                                          .collect(Collectors.toList())
                            ).build()
            );
        }
        Recipient recipientObject = StringUtils.isNotBlank(recipient)
                                    ? objectMapper.readValue(recipient, Recipient.class)
                                    : null;

        Narrowcast narrowcast = new Narrowcast(
                messageList,
                recipientObject,
                Filter.builder()
                      .demographic(
                              OperatorDemographicFilter.builder()
                                                       .and(condition)
                                                       .build()
                      ).build(),
                maxLimit == null ? null : Limit.builder().max(maxLimit).build(),
                notificationDisabled
        );
        return messagingClient.narrowcast(narrowcast).thenApply(
                response -> new RedirectView("/message/narrowcast/" + response.getRequestId())
        );
    }

    @GetMapping("/message/narrowcast/{requestId}")
    public CompletableFuture<String> narrowcastProgress(@PathVariable String requestId, Model model) {
        model.addAttribute("requestId", requestId);

        return messagingClient.getNarrowcastProgress(requestId)
                              .thenApply(response -> {
                                  model.addAttribute("progress", response);
                                  return "message/narrowcast/progress";

                              });
    }

}
