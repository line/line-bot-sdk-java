package com.linecorp.bot.model.event.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Value;

@Value
@JsonTypeName("location")
public class LocationMessageContent implements MessageContent {
    private final String id;
    private final String title;
    private final String address;
    private final double latitude;
    private final double longitude;

    @JsonCreator
    public LocationMessageContent(
            @JsonProperty("id") String id,
            @JsonProperty("title") String title,
            @JsonProperty("address") String address,
            @JsonProperty("latitude") Double latitude,
            @JsonProperty("longitude") Double longitude) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
