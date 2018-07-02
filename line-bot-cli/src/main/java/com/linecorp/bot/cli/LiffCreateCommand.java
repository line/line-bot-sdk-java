/*
 * Copyright 2018 LINE Corporation
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

package com.linecorp.bot.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.linecorp.bot.cli.arguments.PayloadProvider;
import com.linecorp.bot.client.ChannelManagementSyncClient;
import com.linecorp.bot.liff.LiffView;
import com.linecorp.bot.liff.request.LiffAppAddRequest;
import com.linecorp.bot.liff.response.LiffAppAddResponse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(name = "command", havingValue = "liff-create")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LiffCreateCommand implements CliCommand {
    private ChannelManagementSyncClient channelManagementClient;
    private PayloadProvider payloadProvider;

    @Override
    public void execute() {
        final LiffView liffView = payloadProvider.read(LiffView.class);
        log.info("Request View : {}", liffView);

        final LiffAppAddRequest liffAppAddRequest = new LiffAppAddRequest(liffView);

        final LiffAppAddResponse liffAppAddResponse;
        try {
            liffAppAddResponse = channelManagementClient.addLiffApp(liffAppAddRequest);
        } catch (Exception e) {
            log.error("Failed : {}", e.getMessage());
            return;
        }
        log.info("Successfully finished. Response : {}", liffAppAddResponse);
    }
}
