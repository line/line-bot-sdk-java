package com.linecorp.bot.model.v2.event.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("location")
public class LocationMessageContent extends MessageContent {
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
        super(id);
        this.title = title;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
