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

package com.linecorp.bot.model.event;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonTypeName;

import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.event.things.ThingsContent;

import lombok.Value;

/**
 * Event object for when a user detects a LINE Things.
 */
@Value
@JsonTypeName("things")
public class ThingsEvent implements Event, ReplyEvent {
    /**
     * Token for replying to this event.
     */
    private final String replyToken;

    /**
     * JSON object which contains the source of the event.
     */
    private final Source source;

    /**
     * Content of the things event.
     */
    private final ThingsContent things;

    /**
     * Time of the event.
     */
    private final Instant timestamp;

}
