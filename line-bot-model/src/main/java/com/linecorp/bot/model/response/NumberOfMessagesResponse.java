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

package com.linecorp.bot.model.response;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class NumberOfMessagesResponse {
    public enum Status {
        /**
         * You can get the number of messages.
         */
        Ready,
        /**
         * The message counting process for the date specified in {@code date} has not been completed yet.
         * Retry your request later. Normally, the counting process is completed within the next day.
         */
        Unready,
        /**
         * The date specified in date is earlier than March 31, 2018, when the operation of the counting system
         * started.
         */
        OutOfService
    }

    Status status;
    long success;
}
