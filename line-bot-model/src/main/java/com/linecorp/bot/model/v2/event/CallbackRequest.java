package com.linecorp.bot.model.v2.event;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class CallbackRequest {
    private final List<Event> events;

    public CallbackRequest(@JsonProperty("events") List<Event> events) {
        this.events = events;
    }
}
