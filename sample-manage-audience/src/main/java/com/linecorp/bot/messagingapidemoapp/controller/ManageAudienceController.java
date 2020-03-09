package com.linecorp.bot.messagingapidemoapp.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.manageaudience.response.GetAudienceDataResponse;
import com.linecorp.bot.model.manageaudience.response.GetAudienceGroupsResponse;
import com.linecorp.bot.model.response.BotApiResponse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@AllArgsConstructor
@Slf4j
public class ManageAudienceController {
    private final LineMessagingClient client;

    @GetMapping("/manage_audience/")
    public String list(
            Model model,
            @RequestParam(value = "page", defaultValue = "1") long page
    ) throws ExecutionException, InterruptedException {
        GetAudienceGroupsResponse audienceGroups
                = client.getAudienceGroups(page, null, null, 40L, null,
                null)
                        .get();
        model.addAttribute("audienceGroups", audienceGroups);

        return "manage_audience/list"; // show src/main/resources/templates/manage_audience/list.ftlh
    }

    @PostMapping("/manage_audience/{audienceGroupId}/delete")
    public RedirectView delete(Model model,
                               @PathVariable long audienceGroupId)
            throws ExecutionException, InterruptedException {
        BotApiResponse apiResponse = client.deleteAudienceGroup(audienceGroupId).get();
        log.info("response={}", apiResponse);
        return new RedirectView("/manage_audience/");
    }

    @GetMapping("/manage_audience/{audienceGroupId}")
    public String show(
            Model model,
            @PathVariable long audienceGroupId) throws ExecutionException, InterruptedException {
        GetAudienceDataResponse audienceDataResponse
                = client.getAudienceData(audienceGroupId)
                        .get();
        model.addAttribute("audienceGroup", audienceDataResponse.getAudienceGroup());
        model.addAttribute("jobs", audienceDataResponse.getJobs());

        return "manage_audience/show"; // show src/main/resources/templates/manage_audience/quota.ftlh
    }
}
