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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class ScenarioResult {
    private final String scenarioId;
    private final long revision;
    private final Instant startTime;
    private final Instant endTime;
    private final String resultCode;
    private final List<ActionResult> actionResults;
    private final String bleNotificationPayload;
    private final String errorReason;

    @JsonCreator
    ScenarioResult(
            @JsonProperty("scenarioId") String scenarioId,
            @JsonProperty("revision") long revision,
            @JsonProperty("startTime") Instant startTime,
            @JsonProperty("endTime") Instant endTime,
            @JsonProperty("resultCode") String resultCode,
            @JsonProperty("actionResults") List<ActionResult> actionResults,
            @JsonProperty("bleNotificationPayload") String bleNotificationPayload,
            @JsonProperty("errorReason") String errorReason
    ) {
        this.scenarioId = scenarioId;
        this.revision = revision;
        this.startTime = startTime;
        this.endTime = endTime;
        this.resultCode = resultCode;
        this.actionResults = actionResults;
        this.bleNotificationPayload = bleNotificationPayload;
        this.errorReason = errorReason;
    }
}
