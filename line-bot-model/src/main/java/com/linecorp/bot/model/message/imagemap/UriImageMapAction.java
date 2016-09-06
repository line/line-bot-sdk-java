package com.linecorp.bot.model.message.imagemap;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class UriImageMapAction implements ImageMapAction {
    @JsonProperty("linkUri")
    private final String linkUri;

    @JsonProperty("area")
    private final ImageMapArea area;

    public UriImageMapAction(@JsonProperty("linkUri") String linkUri,
                             @JsonProperty("area") ImageMapArea area) {
        this.linkUri = linkUri;
        this.area = area;
    }

    @Override
    public String getType() {
        return "uri";
    }
}
