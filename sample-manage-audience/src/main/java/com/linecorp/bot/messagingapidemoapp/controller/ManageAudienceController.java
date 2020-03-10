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

import java.util.Arrays;
import java.util.List;
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
import com.linecorp.bot.model.manageaudience.request.Audience;
import com.linecorp.bot.model.manageaudience.request.CreateAudienceGroupRequest;
import com.linecorp.bot.model.manageaudience.request.CreateClickBasedAudienceGroupRequest;
import com.linecorp.bot.model.manageaudience.request.CreateImpBasedAudienceGroupRequest;
import com.linecorp.bot.model.manageaudience.request.UpdateAudienceGroupDescriptionRequest;
import com.linecorp.bot.model.manageaudience.response.CreateAudienceGroupResponse;
import com.linecorp.bot.model.manageaudience.response.CreateImpBasedAudienceGroupResponse;
import com.linecorp.bot.model.manageaudience.response.GetAudienceDataResponse;
import com.linecorp.bot.model.manageaudience.response.GetAudienceGroupsResponse;
import com.linecorp.bot.model.response.BotApiResponse;

import io.micrometer.core.instrument.util.StringUtils;
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

    @GetMapping("/manage_audience/upload")
    public String upload() {
        return "manage_audience/upload";
    }

    @PostMapping("/manage_audience/upload")
    public RedirectView postUpload(
            @RequestParam String description,
            @RequestParam Boolean isIfaAudience,
            @RequestParam(required = false) String uploadDescription,
            @RequestParam String audiences
    ) throws ExecutionException, InterruptedException {
        List<Audience> audiencesList = Arrays.stream(audiences.split("\r?\n"))
                                             .map(it -> it.replaceAll("\\s", ""))
                                             .filter(StringUtils::isNotBlank)
                                             .map(Audience::new)
                                             .collect(Collectors.toList());

        CreateAudienceGroupRequest request = CreateAudienceGroupRequest.builder()
                                                                       .description(description)
                                                                       .isIfaAudience(isIfaAudience)
                                                                       .uploadDescription(uploadDescription)
                                                                       .audiences(audiencesList)
                                                                       .build();
        CreateAudienceGroupResponse response = client.createAudienceGroup(request).get();

        return new RedirectView("/manage_audience/" + response.getAudienceGroupId());
    }

    @GetMapping("/manage_audience/updateDescription/{audienceGroupId}")
    public String updateDescription(@PathVariable Long audienceGroupId,
                                    Model model) throws ExecutionException, InterruptedException {
        GetAudienceDataResponse response = client.getAudienceData(audienceGroupId).get();
        model.addAttribute("audienceGroup", response.getAudienceGroup());
        return "manage_audience/updateDescription";
    }

    @PostMapping("/manage_audience/updateDescription/{audienceGroupId}")
    public RedirectView postUpdateDescription(@PathVariable Long audienceGroupId,
                                              @RequestParam String description)
            throws ExecutionException, InterruptedException {
        UpdateAudienceGroupDescriptionRequest request = UpdateAudienceGroupDescriptionRequest
                .builder()
                .description(description)
                .build();
        client.updateAudienceGroupDescription(audienceGroupId, request).get();
        return new RedirectView("/manage_audience/" + audienceGroupId);
    }

    // Create impression based audience group
    @GetMapping("/manage_audience/imp")
    public String imp(@RequestParam(required = false) String requestId, Model model) {
        model.addAttribute("requestId", requestId);
        return "manage_audience/imp";
    }

    @PostMapping("/manage_audience/imp")
    public RedirectView postImp(@RequestParam String description, @RequestParam String requestId)
            throws ExecutionException, InterruptedException {
        CreateImpBasedAudienceGroupRequest request = CreateImpBasedAudienceGroupRequest
                .builder()
                .description(description)
                .requestId(requestId)
                .build();
        CreateImpBasedAudienceGroupResponse response =
                client.createImpBasedAudienceGroup(request).get();

        return new RedirectView("/manage_audience/" + response.getAudienceGroupId());
    }

    // Create click based audience group
    @GetMapping("/manage_audience/click")
    public String click(@RequestParam(required = false) String requestId, Model model) {
        model.addAttribute("requestId", requestId);
        return "manage_audience/click";
    }

    @PostMapping("/manage_audience/click")
    public CompletableFuture<RedirectView> postClick(@RequestParam String description,
                                                     @RequestParam String requestId)
            throws ExecutionException, InterruptedException {
        CreateClickBasedAudienceGroupRequest request = CreateClickBasedAudienceGroupRequest
                .builder()
                .description(description)
                .requestId(requestId)
                .build();
        return client
                .createClickBasedAudienceGroup(request)
                .thenApply(
                        response -> new RedirectView("/manage_audience/" + response.getAudienceGroupId())
                );
    }
}

