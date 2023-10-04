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


package com.linecorp.bot.audience.client;

import static org.assertj.core.api.Assertions.assertThat;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathTemplate;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import com.linecorp.bot.client.base.BlobContent;
import com.linecorp.bot.client.base.UploadFile;

import java.net.URI;

import java.util.Map;


import com.linecorp.bot.audience.model.AddAudienceToAudienceGroupRequest;
import com.linecorp.bot.audience.model.AudienceGroupCreateRoute;
import com.linecorp.bot.audience.model.AudienceGroupStatus;
import com.linecorp.bot.audience.model.CreateAudienceGroupRequest;
import com.linecorp.bot.audience.model.CreateAudienceGroupResponse;
import com.linecorp.bot.audience.model.CreateClickBasedAudienceGroupRequest;
import com.linecorp.bot.audience.model.CreateClickBasedAudienceGroupResponse;
import com.linecorp.bot.audience.model.CreateImpBasedAudienceGroupRequest;
import com.linecorp.bot.audience.model.CreateImpBasedAudienceGroupResponse;
import com.linecorp.bot.audience.model.ErrorResponse;
import com.linecorp.bot.audience.model.GetAudienceDataResponse;
import com.linecorp.bot.audience.model.GetAudienceGroupAuthorityLevelResponse;
import com.linecorp.bot.audience.model.GetAudienceGroupsResponse;
import com.linecorp.bot.audience.model.UpdateAudienceGroupAuthorityLevelRequest;
import com.linecorp.bot.audience.model.UpdateAudienceGroupDescriptionRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.ocadotechnology.gembus.test.Arranger;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.github.tomakehurst.wiremock.WireMockServer;

/**
* API tests for ManageAudienceClient
*/
@Timeout(5)
public class ManageAudienceClientTest {
    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private WireMockServer wireMockServer;
    private ManageAudienceClient api;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());


        api = ManageAudienceClient.builder("MY_OWN_TOKEN")
            .apiEndPoint(URI.create(wireMockServer.baseUrl()))
            .build();
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void activateAudienceGroupTest() {
        stubFor(put(urlPathTemplate("/v2/bot/audienceGroup/{audienceGroupId}/activate")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            Long audienceGroupId = Arranger.some(Long.class);

        api.activateAudienceGroup(audienceGroupId).join().body();

        // TODO: test validations
    }

    @Test
    public void addAudienceToAudienceGroupTest() {
        stubFor(put(urlPathTemplate("/v2/bot/audienceGroup/upload")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            AddAudienceToAudienceGroupRequest addAudienceToAudienceGroupRequest = Arranger.some(AddAudienceToAudienceGroupRequest.class);

        api.addAudienceToAudienceGroup(addAudienceToAudienceGroupRequest).join().body();

        // TODO: test validations
    }

    @Test
    public void createAudienceGroupTest() {
        stubFor(post(urlPathTemplate("/v2/bot/audienceGroup/upload")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            CreateAudienceGroupRequest createAudienceGroupRequest = Arranger.some(CreateAudienceGroupRequest.class);

        CreateAudienceGroupResponse response = api.createAudienceGroup(createAudienceGroupRequest).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void createClickBasedAudienceGroupTest() {
        stubFor(post(urlPathTemplate("/v2/bot/audienceGroup/click")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            CreateClickBasedAudienceGroupRequest createClickBasedAudienceGroupRequest = Arranger.some(CreateClickBasedAudienceGroupRequest.class);

        CreateClickBasedAudienceGroupResponse response = api.createClickBasedAudienceGroup(createClickBasedAudienceGroupRequest).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void createImpBasedAudienceGroupTest() {
        stubFor(post(urlPathTemplate("/v2/bot/audienceGroup/imp")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            CreateImpBasedAudienceGroupRequest createImpBasedAudienceGroupRequest = Arranger.some(CreateImpBasedAudienceGroupRequest.class);

        CreateImpBasedAudienceGroupResponse response = api.createImpBasedAudienceGroup(createImpBasedAudienceGroupRequest).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void deleteAudienceGroupTest() {
        stubFor(delete(urlPathTemplate("/v2/bot/audienceGroup/{audienceGroupId}")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            Long audienceGroupId = Arranger.some(Long.class);

        api.deleteAudienceGroup(audienceGroupId).join().body();

        // TODO: test validations
    }

    @Test
    public void getAudienceDataTest() {
        stubFor(get(urlPathTemplate("/v2/bot/audienceGroup/{audienceGroupId}")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            Long audienceGroupId = Arranger.some(Long.class);

        GetAudienceDataResponse response = api.getAudienceData(audienceGroupId).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getAudienceGroupAuthorityLevelTest() {
        stubFor(get(urlPathTemplate("/v2/bot/audienceGroup/authorityLevel")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));


        GetAudienceGroupAuthorityLevelResponse response = api.getAudienceGroupAuthorityLevel().join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void getAudienceGroupsTest() {
        stubFor(get(urlPathTemplate("/v2/bot/audienceGroup/list")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            Long page = Arranger.some(Long.class);
            String description = Arranger.some(String.class);
            AudienceGroupStatus status = Arranger.some(AudienceGroupStatus.class);
            Long size = Arranger.some(Long.class);
            Boolean includesExternalPublicGroups = Arranger.some(Boolean.class);
            AudienceGroupCreateRoute createRoute = Arranger.some(AudienceGroupCreateRoute.class);

        GetAudienceGroupsResponse response = api.getAudienceGroups(page, description, status, size, includesExternalPublicGroups, createRoute).join().body();

        assertThat(response).isNotNull();
        // TODO: test validations
    }

    @Test
    public void updateAudienceGroupAuthorityLevelTest() {
        stubFor(put(urlPathTemplate("/v2/bot/audienceGroup/authorityLevel")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            UpdateAudienceGroupAuthorityLevelRequest updateAudienceGroupAuthorityLevelRequest = Arranger.some(UpdateAudienceGroupAuthorityLevelRequest.class);

        api.updateAudienceGroupAuthorityLevel(updateAudienceGroupAuthorityLevelRequest).join().body();

        // TODO: test validations
    }

    @Test
    public void updateAudienceGroupDescriptionTest() {
        stubFor(put(urlPathTemplate("/v2/bot/audienceGroup/{audienceGroupId}/updateDescription")).willReturn(
            aResponse()
                .withStatus(200)
                .withHeader("content-type", "application/json")
                .withBody("{}")));

            Long audienceGroupId = Arranger.some(Long.class);
            UpdateAudienceGroupDescriptionRequest updateAudienceGroupDescriptionRequest = Arranger.some(UpdateAudienceGroupDescriptionRequest.class);

        api.updateAudienceGroupDescription(audienceGroupId, updateAudienceGroupDescriptionRequest).join().body();

        // TODO: test validations
    }

}
