package com.linecorp.bot.model.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class BeaconContent {
    private final String hwid;

    public BeaconContent(@JsonProperty("hwid") String hwid) {
        this.hwid = hwid;
    }
}
