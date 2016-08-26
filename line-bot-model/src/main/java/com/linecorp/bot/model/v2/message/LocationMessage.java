package com.linecorp.bot.model.v2.message;

import lombok.Value;

@Value
public class LocationMessage implements Message {

    public static final String TYPE = "text";

    private String title;
    private String address;
    private double latitude;
    private double longitude;

    @Override
    public String getType() {
        return TYPE;
    }
}
