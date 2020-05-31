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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.assertj.core.util.Sets;
import org.junit.Before;
import org.junit.Test;

import com.linecorp.bot.client.exception.ConflictException;
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
    private LineMessagingClient target;
    private IntegrationTestSettings settings;

    @Before
    public void setUp() throws IOException {
        settings = IntegrationTestSettingsLoader.load();
        target = LineMessagingClientFactory.create(settings);
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
    public void broadcastWithRetryKey() throws Exception {
        UUID retryKey = UUID.randomUUID();
        String message = "Broadcast at " + LocalDateTime.now().toString();

        testApiCall(
                () -> target.broadcast(retryKey, new Broadcast(new TextMessage(message), false)).get()
        );

        // second api call with same retryKey cause ConflictException.
        assertThatThrownBy(
                () -> target.broadcast(
                        retryKey,
                        new Broadcast(new TextMessage(message), false)
                ).get()
        ).isInstanceOf(ExecutionException.class)
         .getCause()
         .isInstanceOf(ConflictException.class);
    }

    @Test
    public void multicast() throws Exception {
        testApiCall(
                () -> target.multicast(
                        new Multicast(singleton(settings.getUserId()), new TextMessage("Multicast"), true))
                            .get()
        );
        testApiCall(
                () -> target
                        .multicast(new Multicast(singleton(settings.getUserId()), new TextMessage("Multicast")))
                        .get()
        );
    }

    @Test
    public void multicastWithRetryKey() throws Exception {
        UUID retryKey = UUID.randomUUID();
        String message = "multicast at " + LocalDateTime.now().toString();

        testApiCall(
                () -> target.multicast(
                        retryKey,
                        new Multicast(singleton(settings.getUserId()), new TextMessage(message), true))
                            .get()
        );

        // second api call with same retryKey cause ConflictException.
        assertThatThrownBy(
                () -> target.multicast(
                        retryKey,
                        new Multicast(Sets.newLinkedHashSet(settings.getUserId()),
                                      new TextMessage(message),
                                      false)
                ).get()
        ).isInstanceOf(ExecutionException.class)
         .getCause()
         .isInstanceOf(ConflictException.class);
    }

    @Test
    public void pushMessage() throws Exception {
        testApiCall(
                () -> target.pushMessage(new PushMessage(settings.getUserId(), new TextMessage("Push"), true))
                            .get()
        );
        testApiCall(
                () -> target.pushMessage(new PushMessage(settings.getUserId(), new TextMessage("Push"))).get()
        );
    }

    @Test
    public void pushMessageWithRetryKey() throws Exception {
        UUID retryKey = UUID.randomUUID();
        String message = "Push at " + LocalDateTime.now().toString();

        // first api call
        testApiCall(
                () -> target.pushMessage(
                        retryKey,
                        new PushMessage(settings.getUserId(), new TextMessage(message), true))
                            .get());

        // second api call with same retryKey cause ConflictException.
        assertThatThrownBy(
                () -> target.pushMessage(
                        retryKey,
                        new PushMessage(settings.getUserId(), new TextMessage(message), true)
                ).get()
        ).isInstanceOf(ExecutionException.class)
         .getCause()
         .isInstanceOf(ConflictException.class);
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
