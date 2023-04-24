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

package com.linecorp.bot.messagingapidemoapp.controller;

import java.io.File;
import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.linecorp.bot.audience.client.ManageAudienceBlobClient;
import com.linecorp.bot.audience.client.ManageAudienceClient;
import com.linecorp.bot.audience.model.AddAudienceToAudienceGroupRequest;
import com.linecorp.bot.audience.model.Audience;
import com.linecorp.bot.audience.model.AudienceGroup;
import com.linecorp.bot.audience.model.CreateAudienceGroupRequest;
import com.linecorp.bot.audience.model.CreateClickBasedAudienceGroupRequest;
import com.linecorp.bot.audience.model.CreateImpBasedAudienceGroupRequest;
import com.linecorp.bot.audience.model.GetAudienceDataResponse;
import com.linecorp.bot.audience.model.UpdateAudienceGroupDescriptionRequest;
import com.linecorp.bot.client.base.UploadFile;

import io.micrometer.common.util.StringUtils;

@Controller
public class ManageAudienceController {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ManageAudienceController.class);
    private final ManageAudienceClient client;
    private final ManageAudienceBlobClient blobClient;

    public ManageAudienceController(ManageAudienceClient client, ManageAudienceBlobClient blobClient) {
        this.client = client;
        this.blobClient = blobClient;
    }

    @GetMapping("/manage_audience/")
    public CompletableFuture<String> list(
            Model model,
            @RequestParam(value = "page", defaultValue = "1") long page
    ) {
        return client.getAudienceGroups(page, null, null, 40L, null,
                        null)
                .thenApply(response -> {
                    model.addAttribute("response", response.body());
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
                .thenApply(result -> {
                    GetAudienceDataResponse response = result.body();
                    model.addAttribute("audienceGroup", response.audienceGroup());
                    model.addAttribute("jobs", response.jobs());
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

        CreateAudienceGroupRequest request = new CreateAudienceGroupRequest(
                description,
                isIfaAudience,
                uploadDescription,
                audiencesList
        );
        return client.createAudienceGroup(request)
                .thenApply(
                        result -> new RedirectView("/manage_audience/" + result.body().audienceGroupId()));
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
                description, isIfaAudience, uploadDescription,
                UploadFile.fromFile(convFile)
        ).thenApply(
                result -> new RedirectView("/manage_audience/" + result.body().audienceGroupId())
        ).whenComplete((a, b) -> {
            boolean deleted = convFile.delete();
            log.info("Deleted temporary file: {}", deleted);
        });
    }

    @GetMapping("/manage_audience/update_description/{audienceGroupId}")
    public CompletableFuture<String> updateDescription(@PathVariable Long audienceGroupId,
                                                       Model model) {
        return client.getAudienceData(audienceGroupId)
                .thenApply(result -> {
                    model.addAttribute("audienceGroup", result.body().audienceGroup());
                    return "manage_audience/update_description";
                });
    }

    @PostMapping("/manage_audience/update_description/{audienceGroupId}")
    public CompletableFuture<RedirectView> postUpdateDescription(@PathVariable Long audienceGroupId,
                                                                 @RequestParam String description) {
        UpdateAudienceGroupDescriptionRequest request = new UpdateAudienceGroupDescriptionRequest(
                description
        );
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
        CreateImpBasedAudienceGroupRequest request = new CreateImpBasedAudienceGroupRequest(
                description,
                requestId
        );
        return client.createImpBasedAudienceGroup(request)
                .thenApply(
                        result -> new RedirectView("/manage_audience/" + result.body().audienceGroupId()));
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
        CreateClickBasedAudienceGroupRequest request = new CreateClickBasedAudienceGroupRequest(
                description,
                requestId,
                null
        );
        return client
                .createClickBasedAudienceGroup(request)
                .thenApply(
                        result -> new RedirectView("/manage_audience/" + result.body().audienceGroupId())
                );
    }

    @GetMapping("/manage_audience/add_audience/{audienceGroupId}")
    public CompletableFuture<String> addAudience(@PathVariable Long audienceGroupId,
                                                 Model model) {
        return client.getAudienceData(audienceGroupId)
                .thenApply(result -> {
                    AudienceGroup audienceGroup = result.body().audienceGroup();
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
        AddAudienceToAudienceGroupRequest request = new AddAudienceToAudienceGroupRequest(
                audienceGroupId,
                uploadDescription,
                audienceList
        );
        return client.addAudienceToAudienceGroup(request)
                .thenApply(it -> new RedirectView("/manage_audience/" + audienceGroupId));
    }

    @GetMapping("/manage_audience/add_audience_by_file/{audienceGroupId}")
    public CompletableFuture<String> addAudienceByFile(@PathVariable Long audienceGroupId,
                                                       Model model) {
        return client.getAudienceData(audienceGroupId)
                .thenApply(result -> {
                    AudienceGroup audienceGroup = result.body().audienceGroup();
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
                audienceGroupId, uploadDescription,
                UploadFile.fromFile(convFile)
        ).thenApply(
                it -> new RedirectView("/manage_audience/" + audienceGroupId)
        ).whenComplete((a, b) -> {
            boolean deleted = convFile.delete();
            log.info("Deleted temporary file: {}", deleted);
        });
    }
}

