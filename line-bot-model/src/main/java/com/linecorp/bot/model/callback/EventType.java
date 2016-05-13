package com.linecorp.bot.model.callback;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EventType {

    Message("138311609000106303"),
    Operation("138311609100106403");

    private final String value;

    EventType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static EventType of(String value) {
        for (EventType eventType : values()) {
            if (Objects.equals(eventType.value, value)) {
                return eventType;
            }
        }
        throw new IllegalArgumentException("Invalid EventType: " + value);
    }
}
