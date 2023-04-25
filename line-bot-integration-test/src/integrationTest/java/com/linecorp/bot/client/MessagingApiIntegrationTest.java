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

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import com.linecorp.bot.client.utils.IntegrationTestSettings;
import com.linecorp.bot.client.utils.IntegrationTestSettingsLoader;
import com.linecorp.bot.messaging.client.MessagingApiClient;
import com.linecorp.bot.messaging.model.BotInfoResponse;
import com.linecorp.bot.messaging.model.BroadcastRequest;
import com.linecorp.bot.messaging.model.MulticastRequest;
import com.linecorp.bot.messaging.model.NumberOfMessagesResponse;
import com.linecorp.bot.messaging.model.PushMessageRequest;
import com.linecorp.bot.messaging.model.TextMessage;
import com.linecorp.bot.messaging.model.ValidateMessageRequest;

/**
 * Integration test of {@link com.linecorp.bot.messaging.client.MessagingApiClient}.
 *
 * <p>To run this test, please put config file resources/integration_test_settings.yml.
 */
public class MessagingApiIntegrationTest {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(MessagingApiIntegrationTest.class);

    private MessagingApiClient target;
    private IntegrationTestSettings settings;

    @BeforeEach
    public void setUp() throws IOException {
        settings = IntegrationTestSettingsLoader.load();
        target = buildClient(settings, MessagingApiClient.builder(settings.token()));
    }

    private static void testApiCall(Callable<Object> f) throws Exception {
        final Object response = f.call();
        if (response == null) {
            log.info("null");
        } else {
            log.info(response.toString());
        }
    }

    @Test
    public void broadcast() throws Exception {
        testApiCall(
                () -> target.broadcast(null, new BroadcastRequest(List.of(new TextMessage("Broadcast")), true)).get()
        );
        testApiCall(
                () -> target.broadcast(null, new BroadcastRequest(List.of(new TextMessage("Broadcast")), false)).get()
        );
    }

    @Test
    public void multicast() throws Exception {
        testApiCall(
                () -> target.multicast(
                                null, new MulticastRequest(List.of(new TextMessage("Multicast")), List.of(settings.userId()), true, null))
                        .get()
        );
        testApiCall(
                () -> target.multicast(null, new MulticastRequest(
                                List.of(new TextMessage("Multicast")),
                                List.of(settings.userId()),
                                false,
                                null))
                        .get()
        );
    }

    @Test
    public void pushMessage() throws Exception {
        testApiCall(
                () -> target.pushMessage(null, new PushMessageRequest(settings.userId(), List.of(new TextMessage("Push")), true, null))
                        .get()
        );
        testApiCall(
                () -> target.pushMessage(null, new PushMessageRequest(settings.userId(), List.of(new TextMessage("Push")), false, null)).get()
        );
    }

    @Test
    public void getNumberOfSentBroadcastMessages() throws Exception {
        final NumberOfMessagesResponse getNumberOfSentBroadcastMessages =
                target.getNumberOfSentBroadcastMessages(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))).get().body();

        log.info(getNumberOfSentBroadcastMessages.toString());
    }

    @Test
    public void getBotInfo() throws Exception {
        final BotInfoResponse botInfoResponse = target.getBotInfo().get().body();

        log.info(botInfoResponse.toString());
    }

    @Test
    public void validateReply() throws Exception {
        testApiCall(
                () -> target.validateReply(new ValidateMessageRequest(List.of(new TextMessage("Push"))))
                        .get()
        );

        String longText = new String(new char[10000]).replace('\0', 'a');
        Assertions.assertThatThrownBy(() -> {
            testApiCall(
                    () -> target.validateReply(new ValidateMessageRequest(List.of(new TextMessage(longText)))).get()
            );
        }).isInstanceOf(ExecutionException.class);
    }

    @Test
    public void validatePush() throws Exception {
        testApiCall(
                () -> target.validatePush(new ValidateMessageRequest(List.of(new TextMessage("Push"))))
                        .get()
        );

        String longText = new String(new char[10000]).replace('\0', 'a');
        Assertions.assertThatThrownBy(() -> {
            testApiCall(
                    () -> target.validatePush(new ValidateMessageRequest(List.of(new TextMessage(longText)))).get()
            );
        }).isInstanceOf(ExecutionException.class);
    }

    @Test
    public void validateMulticast() throws Exception {
        testApiCall(
                () -> target.validateMulticast(new ValidateMessageRequest(List.of(new TextMessage("Push"))))
                        .get()
        );

        String longText = new String(new char[10000]).replace('\0', 'a');
        Assertions.assertThatThrownBy(() -> {
            testApiCall(
                    () -> target.validateMulticast(new ValidateMessageRequest(List.of(new TextMessage(longText)))).get()
            );
        }).isInstanceOf(ExecutionException.class);
    }

    @Test
    public void validateNarrowcast() throws Exception {
        testApiCall(
                () -> target.validateNarrowcast(new ValidateMessageRequest(
                                List.of(new TextMessage("Push"))))
                        .get()
        );

        String longText = new String(new char[10000]).replace('\0', 'a');
        Assertions.assertThatThrownBy(() -> {
            testApiCall(
                    () -> target.validateNarrowcast(new ValidateMessageRequest(List.of(new TextMessage(longText))))
                            .get()
            );
        }).isInstanceOf(ExecutionException.class);
    }

    @Test
    public void validateBroadcast() throws Exception {
        testApiCall(
                () -> target.validateBroadcast(new ValidateMessageRequest(List.of(new TextMessage("Push"))))
                        .get()
        );

        String longText = new String(new char[10000]).replace('\0', 'a');
        Assertions.assertThatThrownBy(() -> {
            testApiCall(
                    () -> target.validateBroadcast(new ValidateMessageRequest(List.of(new TextMessage(longText))))
                            .get()
            );
        }).isInstanceOf(ExecutionException.class);
    }
}
