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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.response.GetNumberOfMessageDeliveriesResponse.GetNumberOfMessageDeliveriesResponseBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = GetNumberOfMessageDeliveriesResponseBuilder.class)
public class GetNumberOfMessageDeliveriesResponse {
    /**
     * A status of current calculation.
     */
    public enum Status {
        /**
         * Calculation has finished; the numbers are up-to-date.
         */
        @JsonProperty("ready")
        READY,
        /**
         * We haven't finished calculating the number of sent messages for the specified date. Calculation
         * usually takes about a day. Please try again later.
         */
        @JsonProperty("unready")
        UNREADY,
        /**
         * The specified date is earlier than the date on which we first started calculating sent messages
         * (March 1, 2017).
         */
        @JsonProperty("out_of_service")
        OUT_OF_SERVICE
    }

    /**
     * Calculation status.
     */
    Status status;

    /**
     * Number of push messages sent to all of this LINE official account's friends (broadcast messages).
     */
    Long broadcast;

    /**
     * Number of push messages sent to some of this LINE official account's friends, based on specific
     * attributes (targeted/segmented messages).
     */
    Long targeting;

    /**
     * Number of auto-response messages sent.
     */
    Long autoResponse;

    /**
     * Number of greeting messages sent.
     */
    Long welcomeResponse;

    /**
     * Number of messages sent from LINE Official Account Manager Chat screen.
     */
    Long chat;

    /**
     * Number of broadcast messages sent with the Send broadcast message Messaging API operation.
     */
    Long apiBroadcast;

    /**
     * Number of push messages sent with the Send push message Messaging API operation.
     */
    Long apiPush;

    /**
     * Number of multicast messages sent with the Send multicast message Messaging API operation.
     */
    Long apiMulticast;

    /**
     * Number of replies sent with the Send reply message Messaging API operation.
     */
    Long apiReply;

    @JsonPOJOBuilder(withPrefix = "")
    public static class GetNumberOfMessageDeliveriesResponseBuilder {
        // Filled by lombok
    }

}
