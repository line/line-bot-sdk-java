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

import static com.google.common.util.concurrent.Futures.getUnchecked;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.richmenu.RichMenuListResponse;
import com.linecorp.bot.model.richmenu.RichMenuResponse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(name = "command", havingValue = "richmenu-list")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RichMenuListCommand implements CliCommand {
    private LineMessagingClient lineMessagingClient;

    @Override
    public void execute() {
        final RichMenuListResponse richMenuListResponse = getUnchecked(lineMessagingClient.getRichMenuList());
        log.info("Successfully finished.");

        final List<RichMenuResponse> richMenus = richMenuListResponse.getRichMenus();
        log.info("You have {} RichMenues", richMenus.size());
        richMenus.forEach(richMenuResponse -> {
            log.info("{}", richMenuResponse);
        });
    }
}
