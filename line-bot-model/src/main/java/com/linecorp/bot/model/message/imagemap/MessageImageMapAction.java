package com.linecorp.bot.model.message.imagemap;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class MessageImageMapAction implements ImageMapAction {
    @JsonProperty("text")
    private String text;

    @JsonProperty("area")
    private ImageMapArea area;

    public MessageImageMapAction(@JsonProperty("text") String text,
                                 @JsonProperty("area") ImageMapArea area) {
        this.text = text;
        this.area = area;
    }

    @Override
    public String getType() {
        return "uri";
    }
}
