package com.linecorp.bot.model.message.imagemap;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = As.PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
                      @JsonSubTypes.Type(MessageImageMapAction.class),
                      @JsonSubTypes.Type(URIImageMapAction.class)
              })
public interface ImageMapAction {
    ImageMapArea getArea();
}
