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

package com.linecorp.bot.cli;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.response.demographics.GetFriendsDemographicsResponse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(name = "command", havingValue = "friend-demographics-get")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class FriendDemographicsGetCommand implements CliCommand {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Yaml YAML = new Yaml();

    private final LineMessagingClient lineMessagingClient;

    @Override
    public void execute() throws Exception {
        final GetFriendsDemographicsResponse friendsDemographics =
                lineMessagingClient.getFriendsDemographics().get();

        log.info("Successfully finished:\n{}",
                 YAML.dump(OBJECT_MAPPER.convertValue(friendsDemographics, Map.class)));
    }
}
