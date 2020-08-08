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

package com.linecorp.bot.model.event.things.result;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = ScenarioResult.ScenarioResultBuilder.class)
public class ScenarioResult {
    @JsonPOJOBuilder(withPrefix = "")
    public static class ScenarioResultBuilder {
        // Providing builder instead of public constructor. Class body is filled by lombok.
    }

    String scenarioId;
    long revision;
    Instant startTime;
    Instant endTime;
    String resultCode;
    List<ActionResult> actionResults;
    String bleNotificationPayload;
    String errorReason;
}
