package com.linecorp.bot.model.message;

import lombok.Value;

@Value
public class LocationMessage implements Message {

    private static final String TYPE = "location";

    private String title;
    private String address;
    private double latitude;
    private double longitude;

    @Override
    public String getType() {
        return TYPE;
    }
}
