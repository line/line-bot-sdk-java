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
import java.util.Map;
import java.util.concurrent.Callable;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.model.Broadcast;
import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.GetNumberOfFollowersResponse;
import com.linecorp.bot.model.response.GetNumberOfMessageDeliveriesResponse;
import com.linecorp.bot.model.response.NumberOfMessagesResponse;

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

    @Before
    public void setUp() throws IOException {
        Assume.assumeTrue(TEST_RESOURCE != null);

        final Map<?, ?> map = new ObjectMapper()
                .convertValue(new Yaml().load(TEST_RESOURCE.openStream()), Map.class);

        target = LineMessagingClient
                .builder((String) map.get("token"))
                .apiEndPoint((String) map.get("endpoint"))
                .build();

        userId = (String) map.get("userId");
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
}
