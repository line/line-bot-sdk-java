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

package com.linecorp.bot.webhook.model;

@SuppressWarnings("DanglingJavadoc")
public record UnknownEvent(
        String type,
        /** Get event source. */
        Source source,

        /** Time of the event. */
        Long timestamp,

        /**
         * Channel state.
         * <dl>
         * <dt>active</dt>
         * <dd>The channel is active. You can send a reply message or push message
         * from the bot server that received this webhook event.</dd>
         * <dt>standby (under development)</dt>
         * <dd>The channel is waiting. The bot server that received this webhook event
         * shouldn't send any messages.
         * </dd>
         * </dl>
         */
        EventMode mode,

        /**
         * Webhook Event ID. An ID that uniquely identifies a webhook event. This is a string in ULID format.
         */
        String webhookEventId,

        /**
         * Get delivery context.
         */
        DeliveryContext deliveryContext

) implements Event {
}

