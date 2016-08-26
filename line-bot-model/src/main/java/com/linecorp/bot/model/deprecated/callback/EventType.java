package com.linecorp.bot.model.deprecated.callback;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EventType {

    Message(Constants.MESSAGE),
    Operation(Constants.OPERATION);

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

    public interface Constants {
        String MESSAGE = "138311609000106303";
        String OPERATION = "138311609100106403";
    }

}
