package com.linecorp.bot.model.v2.response;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class BotApiResponse {
    private final String requestId;
    private final String message;
    private final List<String> details;

    public BotApiResponse(@JsonProperty("requestId") String requestId, @JsonProperty("message") String message,
                          @JsonProperty("details") List<String> details) {
        this.requestId = requestId;
        this.message = message;
        this.details = details == null ? Collections.emptyList() : details;
    }
}
