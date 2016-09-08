package com.linecorp.bot.model.message;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Value;

@Value
@JsonTypeName("location")
public class LocationMessage implements Message {
    private String title;
    private String address;
    private double latitude;
    private double longitude;
}
