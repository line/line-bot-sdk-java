/*
 * Copyright 2019 LINE Corporation
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

import static java.util.Collections.singleton;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import com.linecorp.bot.model.Broadcast;
import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.Narrowcast;
import com.linecorp.bot.model.Narrowcast.AgeDemographicFilter;
import com.linecorp.bot.model.Narrowcast.AgeDemographicFilter.Age;
import com.linecorp.bot.model.Narrowcast.AppTypeDemographicFilter;
import com.linecorp.bot.model.Narrowcast.AppTypeDemographicFilter.AppType;
import com.linecorp.bot.model.Narrowcast.Filter;
import com.linecorp.bot.model.Narrowcast.GenderDemographicFilter;
import com.linecorp.bot.model.Narrowcast.GenderDemographicFilter.Gender;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.manageaudience.request.AddAudienceToAudienceGroupRequest;
import com.linecorp.bot.model.manageaudience.request.Audience;
import com.linecorp.bot.model.manageaudience.request.CreateAudienceGroupRequest;
import com.linecorp.bot.model.manageaudience.request.CreateClickBasedAudienceGroupRequest;
import com.linecorp.bot.model.manageaudience.request.CreateImpBasedAudienceGroupRequest;
import com.linecorp.bot.model.manageaudience.response.CreateAudienceGroupResponse;
import com.linecorp.bot.model.manageaudience.response.CreateClickBasedAudienceGroupResponse;
import com.linecorp.bot.model.manageaudience.response.CreateImpBasedAudienceGroupResponse;
import com.linecorp.bot.model.manageaudience.response.GetAudienceDataResponse;
import com.linecorp.bot.model.manageaudience.response.GetAudienceGroupsResponse;
import com.linecorp.bot.model.manageaudience.response.GetAudienceGroupsResponse.AudienceGroup;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.model.response.GetNumberOfFollowersResponse;
import com.linecorp.bot.model.response.GetNumberOfMessageDeliveriesResponse;
import com.linecorp.bot.model.response.NarrowcastProgressResponse;
import com.linecorp.bot.model.response.NarrowcastProgressResponse.Phase;
import com.linecorp.bot.model.response.NumberOfMessagesResponse;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

/**
 * Integration test of {@link LineMessagingClient}.
 *
 * <p>To run this test, please put config file resources/integration_test_settings.yml.
 */
@Slf4j
public class LineMessagingClientImplIntegrationTest {
    public static final URL TEST_RESOURCE = ClassLoader.getSystemResource("integration_test_settings.yml");
    private LineMessagingClient target;
    private String userId;
    private IntegrationTestSettings settings;

    @Before
    public void setUp() throws IOException {
        // Do not run all test cases in this class when src/test/resources/integration_test_settings.yml doesn't
        // exist.
        Assume.assumeTrue(TEST_RESOURCE != null);

        settings = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .convertValue(new Yaml().load(TEST_RESOURCE.openStream()), IntegrationTestSettings.class);

        target = LineMessagingClient
                .builder(settings.token)
                .apiEndPoint(settings.endpoint)
                .build();

        userId = settings.userId;
    }

    @Value
    public static final class IntegrationTestSettings {
        private final String token;
        private final String endpoint;
        private final String userId;
        private final List<String> audienceIfas;
        private final String retargetingRequestId;

        @JsonCreator
        public IntegrationTestSettings(@JsonProperty("token") String token,
                                       @JsonProperty("endpoint") String endpoint,
                                       @JsonProperty("userId") String userId,
                                       @JsonProperty("audienceIfas") List<String> audienceIfas,
                                       @JsonProperty("retargetingRequestId") String retargetingRequestId) {
            this.token = token;
            this.endpoint = endpoint;
            this.userId = userId;
            this.audienceIfas = audienceIfas;
            this.retargetingRequestId = retargetingRequestId;
        }
    }

    private static void testApiCall(Callable<Object> f) throws Exception {
        final Object response = f.call();
        log.info(response.toString());
    }

    @Test
    public void broadcast() throws Exception {
        testApiCall(
                () -> target.broadcast(new Broadcast(new TextMessage("Broadcast"), true)).get()
        );
        testApiCall(
                () -> target.broadcast(new Broadcast(new TextMessage("Broadcast"))).get()
        );
    }

    @Test
    public void narrowcastGender() throws Exception {
        testNarrowcast(new Narrowcast(new TextMessage("Narrowcast test(gender=male)"), new Filter(
                new GenderDemographicFilter(Gender.MALE)
        )));
    }

    @Test
    public void narrowcastAge() throws Exception {
        testNarrowcast(new Narrowcast(new TextMessage("Narrowcast test(Age)"), new Filter(
                new AgeDemographicFilter(Age.AGE_15, Age.AGE_40)
        )));
    }

    @Test
    public void narrowcastAppType() throws Exception {
        testNarrowcast(new Narrowcast(new TextMessage("Narrowcast test(AppType)"), new Filter(
                new AppTypeDemographicFilter(AppType.IOS)
        )));
    }

    private void testNarrowcast(Narrowcast narrowcast) throws Exception {
        testApiCall(
                () -> {
                    BotApiResponse response = target.narrowcast(narrowcast).get();
                    log.info("Narrowcast response={}", response);
                    for (int i = 0; i < 10; i++) {
                        NarrowcastProgressResponse progressResponse = target.getNarrowcastProgress(
                                response.getRequestId()).get();
                        log.info("Progress={}", progressResponse);
                        if (progressResponse.getPhase() == Phase.SUCCEEDED
                            || progressResponse.getPhase() == Phase.FAILED) {
                            return progressResponse;
                        }
                        log.info("Progress response={}", progressResponse);
                        Thread.sleep(1000);
                    }
                    return null;
                }
        );
    }

    @Test
    public void multicast() throws Exception {
        testApiCall(
                () -> target.multicast(new Multicast(singleton(userId), new TextMessage("Multicast"), true))
                            .get()
        );
        testApiCall(
                () -> target.multicast(new Multicast(singleton(userId), new TextMessage("Multicast"))).get()
        );
    }

    @Test
    public void pushMessage() throws Exception {
        testApiCall(
                () -> target.pushMessage(new PushMessage(userId, new TextMessage("Push"), true)).get()
        );
        testApiCall(
                () -> target.pushMessage(new PushMessage(userId, new TextMessage("Push"))).get()
        );
    }

    @Test
    public void getNumberOfMessageDeliveries() throws Exception {
        final GetNumberOfMessageDeliveriesResponse getNumberOfMessageDeliveriesResponse =
                target.getNumberOfMessageDeliveries("20191231").get();

        log.info(getNumberOfMessageDeliveriesResponse.toString());
    }

    @Test
    public void getNumberOfSentBroadcastMessages() throws Exception {
        final NumberOfMessagesResponse getNumberOfSentBroadcastMessages =
                target.getNumberOfSentBroadcastMessages("20191231").get();

        log.info(getNumberOfSentBroadcastMessages.toString());
    }

    @Test
    public void getNumberOfFollowers() throws Exception {
        final GetNumberOfFollowersResponse getNumberOfFollowersResponse =
                target.getNumberOfFollowers("20191231").get();

        log.info(getNumberOfFollowersResponse.toString());
    }

    @Test
    public void createAudienceGroup() throws Exception {
        Assume.assumeFalse(settings.audienceIfas.isEmpty());

        CreateAudienceGroupResponse createResponse = target
                .createAudienceGroup(new CreateAudienceGroupRequest(
                        "test" + ThreadLocalRandom.current().nextInt(),
                        true,
                        "test",
                        settings.audienceIfas.stream()
                                             .map(Audience::new)
                                             .collect(Collectors.toList())
                ))
                .get();
        log.info(createResponse.toString());

        Long audienceGroupId = createResponse.getAudienceGroupId();

        BotApiResponse addResponse = target
                .addAudienceToAudienceGroup(
                        AddAudienceToAudienceGroupRequest
                                .builder()
                                .audienceGroupId(audienceGroupId)
                                .audiences(settings.audienceIfas.stream()
                                                                .map(Audience::new)
                                                                .collect(Collectors
                                                                                 .toList()))
                                .isIfaAudience(true)
                                .build()
                )
                .get();
        log.info(addResponse.toString());
    }

    @Test
    public void createClickBasedAudienceGroup() throws Exception {
        CreateClickBasedAudienceGroupResponse response = target
                .createClickBasedAudienceGroup(CreateClickBasedAudienceGroupRequest
                                                       .builder()
                                                       .description("test " + ThreadLocalRandom.current()
                                                                                               .nextDouble())
                                                       .requestId(settings.retargetingRequestId)
                                                       .build()
                )
                .get();
        log.info(response.toString());
    }

    @Test
    public void createImpBasedAudienceGroup() throws Exception {
        CreateImpBasedAudienceGroupResponse response = target
                .createImpBasedAudienceGroup(CreateImpBasedAudienceGroupRequest
                                                     .builder()
                                                     .description("test " + ThreadLocalRandom.current()
                                                                                             .nextDouble())
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
}
