package com.linecorp.bot.messagingapidemoapp.controller.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import com.linecorp.bot.model.Narrowcast;
import com.linecorp.bot.model.Narrowcast.AgeDemographicFilter;
import com.linecorp.bot.model.Narrowcast.AgeDemographicFilter.Age;
import com.linecorp.bot.model.Narrowcast.AppTypeDemographicFilter;
import com.linecorp.bot.model.Narrowcast.AppTypeDemographicFilter.AppType;
import com.linecorp.bot.model.Narrowcast.AreaDemographicFilter;
import com.linecorp.bot.model.Narrowcast.AreaDemographicFilter.AreaCode;
import com.linecorp.bot.model.Narrowcast.DemographicFilter;
import com.linecorp.bot.model.Narrowcast.Filter;
import com.linecorp.bot.model.Narrowcast.GenderDemographicFilter;
import com.linecorp.bot.model.Narrowcast.GenderDemographicFilter.Gender;
import com.linecorp.bot.model.Narrowcast.Limit;
import com.linecorp.bot.model.Narrowcast.OperatorDemographicFilter;
import com.linecorp.bot.model.Narrowcast.SubscriptionPeriodDemographicFilter.SubscriptionPeriod;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.model.response.NarrowcastProgressResponse;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@AllArgsConstructor
public class NarrowcastController {
    private final LineMessagingClient client;
    private final MessageHelper messageHelper;

    @GetMapping("/message/narrowcast")
    public String narrowcast(Model model) {
        model.addAttribute("ages", Age.values());
        model.addAttribute("appTypes", AppType.values());
        model.addAttribute("areaCodes", AreaCode.values());
        model.addAttribute("subscriptionPeriods", SubscriptionPeriod.values());

        return "message/narrowcast/form";
    }

    @PostMapping("/message/narrowcast")
    public RedirectView pushNarrowcast(
            @RequestParam String[] messages,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String ageGte,
            @RequestParam(required = false) String ageLt,
            @RequestParam(required = false) String appType,
            @RequestParam(required = false) String[] areaCode,
            @RequestParam(required = false) Integer maxLimit,
            @RequestParam Boolean notificationDisabled
    ) throws ExecutionException, InterruptedException {
        List<Message> messageList = messageHelper.buildMessages(messages);

        List<DemographicFilter> condition = new ArrayList<>();

        if (gender != null) {
            condition.add(new GenderDemographicFilter(Gender.valueOf(gender)));
        }
        if (StringUtils.isNotBlank(ageGte) || StringUtils.isNotBlank(ageLt)) {
            log.info("ageGte={} ageLt={}", ageGte, ageLt);
            AgeDemographicFilter filter = new AgeDemographicFilter(
                    StringUtils.isBlank(ageGte) ? null : Age.valueOf(ageGte),
                    StringUtils.isBlank(ageLt) ? null : Age.valueOf(ageLt)
            );
            condition.add(filter);
        }
        if (StringUtils.isNotBlank(appType)) {
            condition.add(new AppTypeDemographicFilter(AppType.valueOf(appType)));
        }
        if (areaCode != null && areaCode.length > 0) {
            condition.add(new AreaDemographicFilter(
                    Arrays.stream(areaCode)
                          .map(AreaCode::valueOf)
                          .collect(Collectors.toList())
            ));
        }

        Narrowcast narrowcast = new Narrowcast(
                messageList,
                null,
                // TODO audience group id targeting
//                new LogicalOperatorRecipient(Collections.emptyList(), null,
//                                             null),
                new Filter(
                        new OperatorDemographicFilter(
                                condition,
                                null,
                                null
                        )
                ),
                maxLimit == null ? null : new Limit(maxLimit),
                notificationDisabled
        );
        BotApiResponse botApiResponse = client.narrowcast(narrowcast).get();

        log.info("Sent narrowcast={}", botApiResponse);
        return new RedirectView("/message/narrowcast/" + botApiResponse.getRequestId());
    }

    @GetMapping("/message/narrowcast/{requestId}")
    public String narrowcastProgress(@PathVariable String requestId, Model model)
            throws ExecutionException, InterruptedException {
        model.addAttribute("requestId", requestId);

        NarrowcastProgressResponse progress = client.getNarrowcastProgress(
                requestId).get();
        model.addAttribute("progress", progress);
        return "message/narrowcast/progress";
    }

}
