package com.linecorp.bot.model.manageaudience.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Value;

@Value
public final class UploadAudienceGroupRequest {
    private final String description;
    private final Boolean isIfaAudience;
    private final String uploadDescription;
    private final List<Audience> audiences;

    @JsonCreator
    public UploadAudienceGroupRequest(String description, Boolean isIfaAudience, String uploadDescription,
                                      List<Audience> audiences) {
        this.description = description;
        this.isIfaAudience = isIfaAudience;
        this.uploadDescription = uploadDescription;
        this.audiences = audiences;
    }

    @Value
    public static class Audience {
        String id;
    }
}
