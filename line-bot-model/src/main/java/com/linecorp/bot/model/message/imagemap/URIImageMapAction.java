package com.linecorp.bot.model.message.imagemap;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Value;

@Value
@JsonTypeName("uri")
public class URIImageMapAction implements ImageMapAction {
    @JsonProperty("linkUri")
    private final String linkUri;

    @JsonProperty("area")
    private final ImageMapArea area;

    public URIImageMapAction(@JsonProperty("linkUri") String linkUri,
                             @JsonProperty("area") ImageMapArea area) {
        this.linkUri = linkUri;
        this.area = area;
    }
}
