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

package com.linecorp.bot.client;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.linecorp.bot.model.manageaudience.AudienceGroupAuthorityLevel;
import com.linecorp.bot.model.manageaudience.request.AddAudienceToAudienceGroupRequest;
import com.linecorp.bot.model.manageaudience.request.Audience;
import com.linecorp.bot.model.manageaudience.request.CreateAudienceGroupRequest;
import com.linecorp.bot.model.manageaudience.request.CreateClickBasedAudienceGroupRequest;
import com.linecorp.bot.model.manageaudience.request.CreateImpBasedAudienceGroupRequest;
import com.linecorp.bot.model.manageaudience.request.UpdateAudienceGroupAuthorityLevelRequest;
import com.linecorp.bot.model.manageaudience.request.UpdateAudienceGroupDescriptionRequest;
import com.linecorp.bot.model.manageaudience.response.CreateAudienceGroupResponse;
import com.linecorp.bot.model.manageaudience.response.CreateClickBasedAudienceGroupResponse;
import com.linecorp.bot.model.manageaudience.response.CreateImpBasedAudienceGroupResponse;
import com.linecorp.bot.model.manageaudience.response.GetAudienceDataResponse;
import com.linecorp.bot.model.manageaudience.response.GetAudienceGroupAuthorityLevelResponse;
import com.linecorp.bot.model.manageaudience.response.GetAudienceGroupsResponse;
import com.linecorp.bot.model.manageaudience.response.GetAudienceGroupsResponse.AudienceGroup;
import com.linecorp.bot.model.response.BotApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ManageAudienceIntegrationTest {
    private LineMessagingClient target;
    private IntegrationTestSettings settings;

    @Before
    public void setUp() throws IOException {
        settings = IntegrationTestSettingsLoader.load();

        target = LineMessagingClient
                .builder(settings.token)
                .apiEndPoint(URI.create(settings.endpoint))
                .build();
    }

    @Test
    public void createAudienceGroup() throws Exception {
        Assume.assumeTrue(settings.audienceIfas != null);
        Assume.assumeFalse(settings.audienceIfas.isEmpty());

        CreateAudienceGroupResponse createResponse = target
                .createAudienceGroup(CreateAudienceGroupRequest
                                             .builder()
                                             .description("test" + UUID.randomUUID())
                                             .isIfaAudience(true)
                                             .uploadDescription("test")
                                             .audiences(
                                                     settings.audienceIfas.stream()
                                                                          .map(Audience::new)
                                                                          .collect(Collectors.toList())
                                             ).build()
                ).get();
        log.info(createResponse.toString());

        long audienceGroupId = createResponse.getAudienceGroupId();

        BotApiResponse addResponse = target
                .addAudienceToAudienceGroup(
                        AddAudienceToAudienceGroupRequest
                                .builder()
                                .audienceGroupId(audienceGroupId)
                                .audiences(settings.audienceIfas.stream()
                                                                .map(Audience::new)
                                                                .collect(Collectors
                                                                                 .toList()))
                                .build()
                )
                .get();
        log.info(addResponse.toString());

        BotApiResponse updateResponse = target.updateAudienceGroupDescription(
                audienceGroupId,
                UpdateAudienceGroupDescriptionRequest
                        .builder()
                        .description("Hello" + UUID.randomUUID())
                        .build()
        ).get();
        log.info(updateResponse.toString());

        BotApiResponse deleteResponse = target.deleteAudienceGroup(audienceGroupId).get();
        log.info(deleteResponse.toString());
    }

    @Test
    public void createClickBasedAudienceGroup() throws Exception {
        Assume.assumeNotNull(settings.retargetingRequestId);

        CreateClickBasedAudienceGroupResponse response = target
                .createClickBasedAudienceGroup(CreateClickBasedAudienceGroupRequest
                                                       .builder()
                                                       .description("test " + UUID.randomUUID())
                                                       .requestId(settings.retargetingRequestId)
                                                       .build()
                )
                .get();
        log.info(response.toString());
    }

    @Test
    public void createImpBasedAudienceGroup() throws Exception {
        Assume.assumeNotNull(settings.retargetingRequestId);

        CreateImpBasedAudienceGroupResponse response = target
                .createImpBasedAudienceGroup(CreateImpBasedAudienceGroupRequest
                                                     .builder()
                                                     .description("test " + UUID.randomUUID())
                                                     .requestId(settings.retargetingRequestId)
                                                     .build()
                )
                .get();
        log.info(response.toString());
    }

    @Test
    public void getAudienceGroups() throws ExecutionException, InterruptedException {
        GetAudienceGroupsResponse response = target
                .getAudienceGroups(1L, null, null, 40L)
                .get();
        log.info(response.toString());

        List<AudienceGroup> audienceGroups = response.getAudienceGroups();
        for (AudienceGroup audienceGroup : audienceGroups) {
            GetAudienceDataResponse dataResponse = target.getAudienceData(
                    audienceGroup.getAudienceGroupId()).get();
            log.info("id={} data={}", audienceGroup.getAudienceGroupId(), dataResponse);
        }
    }

    @Test
    public void getAudienceGroupAuthorityLevel() throws ExecutionException, InterruptedException {
        GetAudienceGroupAuthorityLevelResponse response = target
                .getAudienceGroupAuthorityLevel()
                .get();
        log.info(response.toString());

        AudienceGroupAuthorityLevel origLevel = response.getAuthorityLevel();
        AudienceGroupAuthorityLevel inverted = origLevel == AudienceGroupAuthorityLevel.PRIVATE
                                               ? AudienceGroupAuthorityLevel.PUBLIC
                                               : AudienceGroupAuthorityLevel.PRIVATE;

        BotApiResponse invertResponse = target.updateAudienceGroupAuthorityLevel(
                UpdateAudienceGroupAuthorityLevelRequest
                        .builder()
                        .authorityLevel(inverted)
                        .build()).get();
        log.info(invertResponse.toString());

        BotApiResponse revertResponse = target.updateAudienceGroupAuthorityLevel(
                UpdateAudienceGroupAuthorityLevelRequest
                        .builder()
                        .authorityLevel(origLevel)
                        .build()).get();
        log.info(revertResponse.toString());
    }
}
