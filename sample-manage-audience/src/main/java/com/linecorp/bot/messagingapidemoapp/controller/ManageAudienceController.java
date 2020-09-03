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
 */

package com.linecorp.bot.messagingapidemoapp.controller;

import java.io.File;
import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.linecorp.bot.client.ManageAudienceBlobClient;
import com.linecorp.bot.client.ManageAudienceClient;
import com.linecorp.bot.model.manageaudience.AudienceGroup;
import com.linecorp.bot.model.manageaudience.request.AddAudienceToAudienceGroupRequest;
import com.linecorp.bot.model.manageaudience.request.Audience;
import com.linecorp.bot.model.manageaudience.request.CreateAudienceGroupRequest;
import com.linecorp.bot.model.manageaudience.request.CreateClickBasedAudienceGroupRequest;
import com.linecorp.bot.model.manageaudience.request.CreateImpBasedAudienceGroupRequest;
import com.linecorp.bot.model.manageaudience.request.UpdateAudienceGroupDescriptionRequest;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@AllArgsConstructor
@Slf4j
public class ManageAudienceController {
    private final ManageAudienceClient client;
    private final ManageAudienceBlobClient blobClient;

    @GetMapping("/manage_audience/")
    public CompletableFuture<String> list(
            Model model,
            @RequestParam(value = "page", defaultValue = "1") long page
    ) {
        return client.getAudienceGroups(page, null, null, 40L, null,
                                        null)
                     .thenApply(response -> {
                         model.addAttribute("response", response);
                         return "manage_audience/list";
                     });
    }

    @PostMapping("/manage_audience/{audienceGroupId}/delete")
    public CompletableFuture<RedirectView> delete(@PathVariable long audienceGroupId) {
        return client.deleteAudienceGroup(audienceGroupId)
                     .thenApply(response -> new RedirectView("/manage_audience/"));
    }

    @GetMapping("/manage_audience/{audienceGroupId}")
    public CompletableFuture<String> show(
            Model model,
            @PathVariable long audienceGroupId) {
        return client.getAudienceData(audienceGroupId)
                     .thenApply(response -> {
                         model.addAttribute("audienceGroup", response.getAudienceGroup());
                         model.addAttribute("jobs", response.getJobs());
                         return "manage_audience/show";
                     });
    }

    @GetMapping("/manage_audience/upload")
    public String upload() {
        return "manage_audience/upload";
    }

    @PostMapping("/manage_audience/upload")
    public CompletableFuture<RedirectView> postUpload(
            @RequestParam String description,
            @RequestParam Boolean isIfaAudience,
            @RequestParam(required = false) String uploadDescription,
            @RequestParam String audiences
    ) {
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
        return client.createAudienceGroup(request)
                     .thenApply(
                             response -> new RedirectView("/manage_audience/" + response.getAudienceGroupId()));
    }

    @GetMapping("/manage_audience/upload_by_file")
    public String uploadByFile() {
        return "manage_audience/upload_by_file";
    }

    @PostMapping("/manage_audience/upload_by_file")
    public CompletableFuture<RedirectView> postUploadByFile(
            @RequestParam String description,
            @RequestParam Boolean isIfaAudience,
            @RequestParam(required = false) String uploadDescription,
            @RequestParam MultipartFile file
    ) throws IOException {
        File convFile = File.createTempFile("temp", ".dat");
        convFile.deleteOnExit();
        file.transferTo(convFile);

        return blobClient.createAudienceForUploadingUserIds(
                description, isIfaAudience, uploadDescription, convFile
        ).thenApply(
                response -> new RedirectView("/manage_audience/" + response.getAudienceGroupId())
        ).whenComplete((a, b) -> {
            boolean deleted = convFile.delete();
            log.info("Deleted temporary file: {}", deleted);
        });
    }

    @GetMapping("/manage_audience/update_description/{audienceGroupId}")
    public CompletableFuture<String> updateDescription(@PathVariable Long audienceGroupId,
                                                       Model model) {
        return client.getAudienceData(audienceGroupId)
                     .thenApply(response -> {
                         model.addAttribute("audienceGroup", response.getAudienceGroup());
                         return "manage_audience/update_description";
                     });
    }

    @PostMapping("/manage_audience/update_description/{audienceGroupId}")
    public CompletableFuture<RedirectView> postUpdateDescription(@PathVariable Long audienceGroupId,
                                                                 @RequestParam String description) {
        UpdateAudienceGroupDescriptionRequest request = UpdateAudienceGroupDescriptionRequest
                .builder()
                .description(description)
                .build();
        return client.updateAudienceGroupDescription(audienceGroupId, request)
                     .thenApply(response -> new RedirectView("/manage_audience/" + audienceGroupId));
    }

    // Create impression based audience group
    @GetMapping("/manage_audience/imp")
    public String imp(@RequestParam(required = false) String requestId, Model model) {
        model.addAttribute("requestId", requestId);
        return "manage_audience/imp";
    }

    @PostMapping("/manage_audience/imp")
    public CompletableFuture<RedirectView> postImp(@RequestParam String description,
                                                   @RequestParam String requestId) {
        CreateImpBasedAudienceGroupRequest request = CreateImpBasedAudienceGroupRequest
                .builder()
                .description(description)
                .requestId(requestId)
                .build();
        return client.createImpBasedAudienceGroup(request)
                     .thenApply(
                             response -> new RedirectView("/manage_audience/" + response.getAudienceGroupId()));
    }

    // Create click based audience group
    @GetMapping("/manage_audience/click")
    public String click(@RequestParam(required = false) String requestId, Model model) {
        model.addAttribute("requestId", requestId);
        return "manage_audience/click";
    }

    @PostMapping("/manage_audience/click")
    public CompletableFuture<RedirectView> postClick(@RequestParam String description,
                                                     @RequestParam String requestId) {
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

    @GetMapping("/manage_audience/add_audience/{audienceGroupId}")
    public CompletableFuture<String> addAudience(@PathVariable Long audienceGroupId,
                                                 Model model) {
        return client.getAudienceData(audienceGroupId)
                     .thenApply(response -> {
                         AudienceGroup audienceGroup = response.getAudienceGroup();
                         model.addAttribute("audienceGroup", audienceGroup);
                         return "manage_audience/add_audience";
                     });
    }

    @PostMapping("/manage_audience/add_audience/{audienceGroupId}")
    public CompletableFuture<RedirectView> postAddAudience(
            @PathVariable Long audienceGroupId,
            @RequestParam(required = false) String uploadDescription,
            @RequestParam String audiences) {
        List<Audience> audienceList = Arrays.stream(audiences.split("\r?\n"))
                                            .map(it -> it.replaceAll("\\s+", ""))
                                            .filter(it -> it.length() > 0)
                                            .map(Audience::new)
                                            .collect(Collectors.toList());
        AddAudienceToAudienceGroupRequest request = AddAudienceToAudienceGroupRequest
                .builder()
                .audienceGroupId(audienceGroupId)
                .audiences(audienceList)
                .uploadDescription(uploadDescription)
                .build();
        return client.addAudienceToAudienceGroup(request)
                     .thenApply(it -> new RedirectView("/manage_audience/" + audienceGroupId));
    }

    @GetMapping("/manage_audience/add_audience_by_file/{audienceGroupId}")
    public CompletableFuture<String> addAudienceByFile(@PathVariable Long audienceGroupId,
                                                       Model model) {
        return client.getAudienceData(audienceGroupId)
                     .thenApply(response -> {
                         AudienceGroup audienceGroup = response.getAudienceGroup();
                         model.addAttribute("audienceGroup", audienceGroup);
                         return "manage_audience/add_audience_by_file";
                     });
    }

    @PostMapping("/manage_audience/add_audience_by_file/{audienceGroupId}")
    public CompletableFuture<RedirectView> postAddAudienceByFile(
            @PathVariable Long audienceGroupId,
            @RequestParam(required = false) String uploadDescription,
            @RequestParam MultipartFile file) throws IOException {
        File convFile = File.createTempFile("temp", ".dat");
        convFile.deleteOnExit();
        file.transferTo(convFile);

        return blobClient.addUserIdsToAudience(
                audienceGroupId, uploadDescription, convFile
        ).thenApply(
                it -> new RedirectView("/manage_audience/" + audienceGroupId)
        ).whenComplete((a, b) -> {
            boolean deleted = convFile.delete();
            log.info("Deleted temporary file: {}", deleted);
        });
    }
}

