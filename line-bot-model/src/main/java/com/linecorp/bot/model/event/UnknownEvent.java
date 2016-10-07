package com.linecorp.bot.model.event;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.linecorp.bot.model.event.source.Source;

import lombok.Value;

/**
 * Fallback event type for {@link Event}.
 */
@Value
public class UnknownEvent implements Event {
    /**
     * Type of the event.
     */
    private final String type;

    /**
     * JSON object which contains the source of the event
     */
    private final Source source;

    /**
     * Time of the event
     */
    private final Instant timestamp;

    public UnknownEvent(
            @JsonProperty("type") String type,
            @JsonProperty("source") Source source,
            @JsonProperty("timestamp") Instant timestamp
    ) {
        this.type = type;
        this.source = source;
        this.timestamp = timestamp;
    }
}
