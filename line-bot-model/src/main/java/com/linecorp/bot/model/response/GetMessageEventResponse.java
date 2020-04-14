/*
 * Copyright 2020 LINE Corporation
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
 *
 */

package com.linecorp.bot.model.response;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;

/**
 * Response of the getting user interaction statistics API.
 */
@Value
@Builder
@JsonDeserialize(builder = GetMessageEventResponse.GetMessageEventResponseBuilder.class)
public class GetMessageEventResponse {
    Overview overview;
    List<Message> messages;
    List<Click> clicks;

    /**
     * Summary of message statistics.
     */
    @Value
    @Builder
    @JsonDeserialize(builder = Overview.OverviewBuilder.class)
    public static class Overview {
        /**
         * Request ID.
         */
        String requestId;

        /**
         * UNIX timestamp for message delivery time.
         */
        Long timestamp;

        /**
         * Number of messages delivered. This property shows values of less than 20. However, if all messages
         * have not been sent, it will be null.
         */
        Long delivered;

        /**
         * Number of people who opened the message, meaning they displayed at least 1 bubble.
         */
        Long uniqueImpression;

        /**
         * Number of people who opened any URL in the message.
         */
        Long uniqueClick;

        /**
         * Number of people who started playing any video or audio in the message.
         */
        Long uniqueMediaPlayed;

        /**
         * Number of people who played the entirety of any video or audio in the message.
         */
        Long uniqueMediaPlayed100Percent;

        @JsonPOJOBuilder(withPrefix = "")
        public static class OverviewBuilder {
            // Filled by lombok
        }
    }

    @Value
    @Builder
    @JsonDeserialize(builder = Message.MessageBuilder.class)
    public static class Message {
        /**
         * Bubble's serial number.
         */
        Long seq;

        /**
         * Number of times the bubble was displayed.
         */
        Long impression;

        /**
         * Number of times audio or video in the bubble started playing.
         */
        Long mediaPlayed;

        /**
         * Number of times audio or video in the bubble was played from start to 25%.
         */
        Long mediaPlayed25Percent;

        /**
         * Number of times audio or video in the bubble was played from start to 50%.
         */
        Long mediaPlayed50Percent;

        /**
         * Number of times audio or video in the bubble was played from start to 75%.
         */
        Long mediaPlayed75Percent;

        /**
         * Number of times audio or video in the bubble was played in its entirety.
         */
        Long mediaPlayed100Percent;

        /**
         * Number of people that started playing audio or video in the bubble.
         */
        Long uniqueMediaPlayed;

        /**
         * Number of people that played audio or video in the bubble from start to 25%.
         */
        Long uniqueMediaPlayed25Percent;

        /**
         * Number of people that played audio or video in the bubble from start to 50%.
         */
        Long uniqueMediaPlayed50Percent;

        /**
         * Number of people that played audio or video in the bubble from start to 75%.
         */
        Long uniqueMediaPlayed75Percent;

        /**
         * Number of people that played audio or video in the bubble in its entirety.
         */
        Long uniqueMediaPlayed100Percent;

        @JsonPOJOBuilder(withPrefix = "")
        public static class MessageBuilder {
            // Filled by lombok
        }
    }

    /**
     * Array of information about opened URLs in the message.
     */
    @Value
    @Builder
    @JsonDeserialize(builder = Click.ClickBuilder.class)
    public static class Click {
        /**
         * The URL's serial number.
         */
        Long seq;

        /**
         * URL.
         */
        String url;

        /**
         * Number of times the URL was opened.
         */
        Long click;

        /**
         * Number of people that opened the URL.
         */
        Long uniqueClick;

        /**
         * Number of people who opened this url through any link in the message. If a message contains two
         * links to the same URL and a user opens both links, they're counted only once.
         */
        Long uniqueClickOfRequest;

        @JsonPOJOBuilder(withPrefix = "")
        public static class ClickBuilder {
            // Filled by lombok
        }
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class GetMessageEventResponseBuilder {
        // Filled by lombok
    }
}
