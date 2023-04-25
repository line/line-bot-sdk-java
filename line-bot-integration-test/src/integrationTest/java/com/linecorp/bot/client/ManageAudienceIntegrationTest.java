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

package com.linecorp.bot.client;

import static com.linecorp.bot.client.utils.ClientBuilder.buildClient;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import com.linecorp.bot.audience.client.ManageAudienceClient;
import com.linecorp.bot.audience.model.AddAudienceToAudienceGroupRequest;
import com.linecorp.bot.audience.model.Audience;
import com.linecorp.bot.audience.model.AudienceGroup;
import com.linecorp.bot.audience.model.AudienceGroupAuthorityLevel;
import com.linecorp.bot.audience.model.AudienceGroupCreateRoute;
import com.linecorp.bot.audience.model.CreateAudienceGroupRequest;
import com.linecorp.bot.audience.model.CreateAudienceGroupResponse;
import com.linecorp.bot.audience.model.CreateClickBasedAudienceGroupRequest;
import com.linecorp.bot.audience.model.CreateClickBasedAudienceGroupResponse;
import com.linecorp.bot.audience.model.CreateImpBasedAudienceGroupRequest;
import com.linecorp.bot.audience.model.CreateImpBasedAudienceGroupResponse;
import com.linecorp.bot.audience.model.GetAudienceDataResponse;
import com.linecorp.bot.audience.model.GetAudienceGroupAuthorityLevelResponse;
import com.linecorp.bot.audience.model.GetAudienceGroupsResponse;
import com.linecorp.bot.audience.model.UpdateAudienceGroupAuthorityLevelRequest;
import com.linecorp.bot.audience.model.UpdateAudienceGroupDescriptionRequest;
import com.linecorp.bot.client.base.Result;
import com.linecorp.bot.client.utils.IntegrationTestSettings;
import com.linecorp.bot.client.utils.IntegrationTestSettingsLoader;

public class ManageAudienceIntegrationTest {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ManageAudienceIntegrationTest.class);
    private ManageAudienceClient target;
    private IntegrationTestSettings settings;

    @BeforeEach
    public void setUp() throws IOException {
        settings = IntegrationTestSettingsLoader.load();
        target = buildClient(settings, ManageAudienceClient.builder(settings.token()));
    }

    @Test
    public void createAudienceGroup() throws Exception {
        CreateAudienceGroupResponse createResponse = target
                .createAudienceGroup(new CreateAudienceGroupRequest(
                                "test" + UUID.randomUUID(),
                                true,
                                "test",
                                settings.audienceIfas().stream()
                                        .map(Audience::new)
                                        .collect(Collectors.toList())
                        )
                ).get().body();
        log.info(createResponse.toString());

        long audienceGroupId = createResponse.audienceGroupId();

        Result<Void> addResponse = target
                .addAudienceToAudienceGroup(
                        new AddAudienceToAudienceGroupRequest(
                                audienceGroupId,
                                null,
                                settings.audienceIfas().stream()
                                        .map(Audience::new)
                                        .collect(Collectors.toList())
                        )
                )
                .get();
        log.info(addResponse.toString());

        Result<Void> updateResponse = target.updateAudienceGroupDescription(
                audienceGroupId,
                new UpdateAudienceGroupDescriptionRequest(
                        "Hello" + UUID.randomUUID()
                )
        ).get();
        log.info(updateResponse.toString());

        Result<Void> deleteResponse = target.deleteAudienceGroup(audienceGroupId).get();
        log.info(deleteResponse.toString());
    }

    @Test
    public void createClickBasedAudienceGroup() throws Exception {
        CreateClickBasedAudienceGroupResponse response = target
                .createClickBasedAudienceGroup(new CreateClickBasedAudienceGroupRequest(
                                "test " + UUID.randomUUID(),
                                settings.retargetingRequestId(),
                                null
                        )
                )
                .get().body();
        log.info(response.toString());
    }

    @Test
    public void createImpBasedAudienceGroup() throws Exception {
        CreateImpBasedAudienceGroupResponse response = target
                .createImpBasedAudienceGroup(new CreateImpBasedAudienceGroupRequest(
                                "test " + UUID.randomUUID(),
                        settings.retargetingRequestId()
                        )
                )
                .get().body();
        log.info(response.toString());
    }

    @Test
    public void getAudienceGroups() throws ExecutionException, InterruptedException {
        GetAudienceGroupsResponse response = target
                .getAudienceGroups(1L, null, null, 40L,
                        false, AudienceGroupCreateRoute.OA_MANAGER)
                .get().body();
        assertThat(response.page()).isEqualTo(1L);
        assertThat(response.size()).isEqualTo(40L);
        assertThat(response.totalCount()).isNotNull();
        log.info(response.toString());

        List<AudienceGroup> audienceGroups = response.audienceGroups();
        for (AudienceGroup audienceGroup : audienceGroups) {
            GetAudienceDataResponse dataResponse = target.getAudienceData(
                    audienceGroup.audienceGroupId()).get().body();
            assertThat(dataResponse.audienceGroup())
                    .isNotNull();
            assertThat(dataResponse.audienceGroup().audienceGroupId())
                    .isEqualTo(audienceGroup.audienceGroupId());
            log.info("id={} data={}", audienceGroup.audienceGroupId(), dataResponse);
        }
    }

    @Test
    public void getAudienceGroupAuthorityLevel() throws ExecutionException, InterruptedException {
        GetAudienceGroupAuthorityLevelResponse response = target
                .getAudienceGroupAuthorityLevel()
                .get().body();
        log.info(response.toString());

        AudienceGroupAuthorityLevel origLevel = response.authorityLevel();
        AudienceGroupAuthorityLevel inverted = origLevel == AudienceGroupAuthorityLevel.PRIVATE
                ? AudienceGroupAuthorityLevel.PUBLIC
                : AudienceGroupAuthorityLevel.PRIVATE;

        Result<Void> invertResponse = target.updateAudienceGroupAuthorityLevel(
                new UpdateAudienceGroupAuthorityLevelRequest(inverted)).get();
        log.info(invertResponse.toString());

        Result<Void> revertResponse = target.updateAudienceGroupAuthorityLevel(
                new UpdateAudienceGroupAuthorityLevelRequest(origLevel)).get();
        log.info(revertResponse.toString());
    }
}
