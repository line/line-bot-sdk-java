package com.linecorp.bot.model.event;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import com.linecorp.bot.model.event.source.Source;

import lombok.Builder;
import lombok.Value;

/**
 * Event object for when the user unsends a message in a group or room.
 */
@JsonTypeName("unsend")
@Value
@Builder(toBuilder = true)
@JsonDeserialize(builder = UnsendEvent.UnsendEventBuilder.class)
public class UnsendEvent implements Event {
    @JsonPOJOBuilder(withPrefix = "")
    public static class UnsendEventBuilder {
        // Providing builder instead of public constructor. Class body is filled by lombok.
    }

    /**
     * JSON object which contains the source of the event.
     */
    Source source;

    /**
     * Time of the event.
     */
    Instant timestamp;

    /**
     * Channel state.
     * <dl>
     * <dt>active</dt>
     * <dd>The channel is active. You can send a reply message or push message from the bot server that received
     * this webhook event.</dd>
     * <dt>standby (under development)</dt>
     * <dd>The channel is waiting. The bot server that received this webhook event shouldn't send any messages.
     * </dd>
     * </dl>
     */
    EventMode mode;

    UnsendDetail unsend;

    @Value
    @Builder(toBuilder = true)
    @JsonDeserialize(builder = UnsendDetail.UnsendDetailBuilder.class)
    public static class UnsendDetail {
        @JsonPOJOBuilder(withPrefix = "")
        public static class UnsendDetailBuilder {
            // Providing builder instead of public constructor. Class body is filled by lombok.
        }

        /**
         * The message ID of the unsent message.
         */
        String messageId;
    }
}
