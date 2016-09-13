package com.linecorp.bot.model.response;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class BotApiResponse {
    private final String message;
    private final List<String> details;

    public BotApiResponse(
            @JsonProperty("message") String message,
            @JsonProperty("details") List<String> details
    ) {
        this.message = message;
        this.details = details == null ? Collections.emptyList() : details;
    }
}
